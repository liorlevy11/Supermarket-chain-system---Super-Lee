package BussinessLayer.Managers;
import BussinessLayer.Objects.Product;
import BussinessLayer.Objects.*;
import DataAccessLayer.*;
import DataAccessLayer.Dal.OrderDAO;
import DataAccessLayer.Dal.ProductDAO;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;

import java.util.*;


public class TransportManager {

    private static TransportManager transportManager = null;
    private static Integer week = 7;
//    private final HashMap<Integer, Transport> transports;

    private TransportDesDAO transportDesDAO;
    private TransportDAO transportDAO;
    private TruckManager truckManager;
    private SiteManager siteManager;
    private ProductManager productManager;
    //private SiteDAO siteDAO ;
    //private EmployeeDAO employeeDAO;
    // private TruckDAO truckDAO;

    private TransportManager() {
        transportDesDAO = new TransportDesDAO();
        transportDAO = new TransportDAO();
        truckManager = TruckManager.getInstance();
        siteManager = SiteManager.getInstance();
    }

    // Static method
    // Static method to create instance of Singleton class
    public static TransportManager getInstance() {
        if (transportManager == null)
            transportManager = new TransportManager();

        return transportManager;
    }

    public HashMap<Integer, Transport> getTransports() {
        List<Transport> transportList = transportDAO.SelectAllTransports();
        HashMap<Integer, Transport> transports = new HashMap<>();
        for (Transport transport : transportList) {
            loadTransport(transport);
            transports.put(transport.getIdTransport(), transport);
        }
        return transports;
    }

    public void setIsReceived(int transportID,boolean isReceived){
        transportDAO.setIsReceived(transportID,true);
    }

    public boolean createTransportForOrder(Order order, LocalDateTime dateTime, Map<Integer, Integer> productsCount, List<Integer> suppliers) {
        LocalDateTime transportDate = null;
        Driver transportDriver = null;
        Truck transportTruck = null;
        double totalWeight = calculateTotalWeight(productsCount);
        LocalDateTime dateTime1 = dateTime.withHour(8);
        LocalDateTime dateTime2 = dateTime.withHour(00);
        //check if transport can be done today

        boolean found = false;
        for (int i = 0; i < week & !found; i++) {

            //check transport for morning shift
            if (dateTime1.isAfter(dateTime)) { //make sure transport is after order happen
                List<Driver> availableDriversMorning = EmployeesManager.getInstance().getAvailableDriver(dateTime1);
                HashMap<Integer, Truck> availableTruckMorning = TruckManager.getInstance().displayAvailableTrucksW(dateTime1, totalWeight);
                for (Driver driver : availableDriversMorning) {
                    for (Truck truck : availableTruckMorning.values()) {
                        if (driver.driverCanDrive(truck.getTypeOfLicence())) {
                            transportDate = dateTime1;
                            transportDriver = driver;
                            transportTruck = truck;
                            found = true;
                        }
                        if (found)
                            break;
                    }
                    if (found)
                        break;
                }
            }
            //check transport for evening shift
            if (dateTime2.isAfter(dateTime)) { //make sure transport is after order happen
                List<Driver> availableDriversEv = EmployeesManager.getInstance().getAvailableDriver(dateTime2);
                HashMap<Integer, Truck> availableTruckEv = TruckManager.getInstance().displayAvailableTrucksW(dateTime2, totalWeight);
                for (Driver driver : availableDriversEv) {
                    for (Truck truck : availableTruckEv.values()) {
                        if (driver.driverCanDrive(truck.getTypeOfLicence())) {
                            transportDate = dateTime2;
                            transportDriver = driver;
                            transportTruck = truck;
                            found = true;
                        }
                        if (found)
                            break;
                    }
                    if (found)
                        break;
                }
            }
            if(found)
                break;
            dateTime1 = dateTime1.plusDays(1);
            dateTime2 = dateTime2.plusDays(1);
        }
        if (found) {
            List<Driver> drivers = new LinkedList<>();
            drivers.add(transportDriver);
            //// the site with id 0 is super lee
            Integer idSource = suppliers.get(0);
            Transport transport = CreateTransportWithReturn(transportDate, order.getOrderId(), idSource, transportTruck.getIdTrack(), totalWeight, drivers, order.getOrderId());
            if (transport != null)
                for (int i = 1; i < suppliers.size(); i++)
                    transport.addDestination(siteManager.getSite(suppliers.get(i)));
            addDestination(transport.getIdTransport(),0);
            //transport.addDestination(siteManager.getSite(0));
        } else {
            String DateInString = dateTime.format(DateTimeFormatter
                    .ofLocalizedDate(FormatStyle.MEDIUM));
            String message = "Please try to arrange the drivers shifts for order number " + order.getOrderId() + ",that was created at " + DateInString + ".";
            String message2 = "Has been sent message to the PersonalManager , you may have to change the date of the order number " + order.getOrderId();
            EmployeesManager.getInstance().addMessage("PersonalManager", message, false);
            EmployeesManager.getInstance().addMessage("TransportManager", message2, false);

        }
        return found;
    }

    public double calculateTotalWeight(Map<Integer, Integer> productsCount) {
        double result = 0.0;
        for (Integer idProduct : productsCount.keySet()) {
            Product product = ProductDAO.getInstance().getProductById(idProduct);
            result = result + product.getWeight() * productsCount.get(idProduct);
        }
        return result;
    }



    public void loadTransport(Transport transport) {
        transport.setSource(siteManager.getSite(transportDAO.getTransportSourceID(transport.getIdTransport())));
        transport.setValidTruck(truckManager.getTruck(transportDAO.getTransportTruckID(transport.getIdTransport())));
        transport.setValidDriver(EmployeesManager.getInstance().getDriver(transportDAO.getTransportDriverID(transport.getIdTransport())));
        setTransportDes(transport);
    }

    public void setTransportDes(Transport transport) {
        List<Integer> l = transportDesDAO.SelectTransportDestanations(transport.getIdTransport());
        List<Site> des = new ArrayList<>();
        for (Integer id : l) {
            des.add(siteManager.getSite(id));
        }
        transport.setDestinations(des);
    }

    public Transport getTransport(int idTransport) {
        boolean fromdb = true;
        if (transportDAO.inCache(idTransport))
            fromdb = false;
        Transport transport = transportDAO.getTransport(idTransport);
        if (fromdb)
            loadTransport(transport);
        return transport;
    }
    public Transport getOrderOfTransport(int orderid){
        return transportDAO.getTransportByOrderID(orderid);
    }

    public void CreateTransport(LocalDateTime date, int driverDocNum, int idSource, int idTruck, double totalWeight, List<Driver> potentialDrivers, int idOrder) {
        checkIfTheDriverDocNumAlreadyExist(driverDocNum);
        Transport transport = new Transport(date, driverDocNum, SiteManager.getInstance().getSite(idSource)
                , TruckManager.getInstance().getTruck((idTruck)), potentialDrivers, totalWeight, idOrder);
        transportDAO.Insert(transport);
    }

    public Transport CreateTransportWithReturn(LocalDateTime date, int driverDocNum, int idSource, int idTruck, double totalWeight, List<Driver> potentialDrivers, int idOrder) {
        checkIfTheDriverDocNumAlreadyExist(driverDocNum);
        Transport transport = new Transport(date, driverDocNum, SiteManager.getInstance().getSite(idSource)
                , TruckManager.getInstance().getTruck((idTruck)), potentialDrivers, totalWeight, idOrder);
        transportDAO.Insert(transport);
        return transport;
    }

    public void CreateTransport(LocalDateTime date, int driverDocNum, int idSource, int idTruck, double totalWeight, List<Driver> potentialDrivers) {
        checkIfTheDriverDocNumAlreadyExist(driverDocNum);
        Transport transport = new Transport(date, driverDocNum, SiteManager.getInstance().getSite(idSource)
                , TruckManager.getInstance().getTruck((idTruck)), potentialDrivers, totalWeight, -1);
        transportDAO.Insert(transport);
    }

    public void checkIfTheDriverDocNumAlreadyExist(int driverDocNum) {
        if (transportDAO.ExistDdnNumber(driverDocNum))
            throw new IllegalArgumentException("Ddn: " + driverDocNum + " already exist in our system");
    }

    public void deleteTransport(int idTransport) {
        Transport transport = getTransport(idTransport);
        if (transport != null)
        {
            if (transport.happened())
                throw new IllegalArgumentException("can not erase this transport ,the transport already happened");

            transport.getDriver().removeTransport(transport);
            transport.getTruck().RemWorkday(transport.getDate());
            EmployeesManager.getInstance().updatedriveravgdistance(getTransport(idTransport).getDriver());
            transportDesDAO.Delete(transport); //delete all destanations from transportdes
            transportDAO.Delete(transport); //delete transports
        }
        else
            throw new IllegalArgumentException("there is no transport with this id");
    }

    public Truck getTruck(int idTransport) {
        return truckManager.getTruck(getTransport(idTransport).getTruckID());
    }

    public Site getSite(int idTransport) {
        return siteManager.getSite(getTransport(idTransport).getSiteID());
    }

    public void RemoveTransportDestination(int idTransport, int idSite) {
        getTransport(idTransport).RemoveTransportDestination(SiteManager.getInstance().getSite(idSite));
        EmployeesManager.getInstance().updatedriveravgdistance(getTransport(idTransport).getDriver());
        transportDesDAO.removeTransportDestination(idTransport, idSite);
    }

    public void addDestination(int idTransport, int idSite) {
        getTransport(idTransport).addDestination(SiteManager.getInstance().getSite(idSite));
        EmployeesManager.getInstance().updatedriveravgdistance(getTransport(idTransport).getDriver());
        transportDesDAO.addDestination(idTransport, idSite);
    }

    public void UpdateTransportTruck(int idTransport, int idTruck) {
        getTransport(idTransport).setTruck(truckManager.getTruck(idTruck));
        transportDAO.setTruckID(idTransport, idTruck);
    }

    public void UpdateTransportDriver(int idTransport, Driver driver) {
        Driver driver1 = getTransport(idTransport).getDriver();
        getTransport(idTransport).setDriver(driver);
        transportDAO.setDriverID(idTransport, driver.getId());
        EmployeesManager.getInstance().updatedriveravgdistance(getTransport(idTransport).getDriver());
        EmployeesManager.getInstance().updatedriveravgdistance(driver1);
    }

    public void UpdateTransportDate(int idTransport, LocalDateTime date) {
        getTransport(idTransport).UpdateTransportDate(date);
        transportDAO.setDate(idTransport, date.toString());
    }

    public void UpdateTransportTotalWeight(int idTransport, double totalWeight) {
        getTransport(idTransport).setTotalWeight(totalWeight);
        transportDAO.setTotalWeight(idTransport, totalWeight);
    }

    public void UpdateTransportDriverDocNum(int idTransport, int driverDocNum) {
        getTransport(idTransport).UpdateTransportDriverDocNum(driverDocNum);
        transportDAO.setDdn(idTransport, driverDocNum);
    }

    public void UpdateTransportSource(int idTransport, int idSite) {
        getTransport(idTransport).UpdateTransportSource(siteManager.getSite(idSite));
        transportDAO.setSourceID(idTransport, idSite);
    }

    public void startingProgram() {
        resetCounter();
    }

    public void resetCounter() {
        Transport.setCounter(transportDAO.getMaxid() + 1);
    }

    public List<LocalDateTime> getDriverTransports(int idDriver) {
        return transportDAO.getDriverTransports(idDriver);
    }

    public List<LocalDateTime> getTruckTransports(int idTrack) {
        return transportDAO.getTruckTransports(idTrack);
    }

    public List<Transport> getTransportByDate(java.util.Date today) {
        return transportDAO.SelectAllTransportsOfDate(today);
    }

    public void DelData() {
        transportDAO.resetCache();
        transportDesDAO.resetCache();
        transportDesDAO.Delete();
        transportDAO.Delete();
    }

//    public void LoadData() throws ParseException {
//        LocalDateTime date3 = LocalDateTime.of(2022, 6, 30,18,00);
//        LocalDateTime date4 = LocalDateTime.of(2022, 6, 25,18,00);
//        List<Driver> drivers= Facade.getInstance().getAvailableDriver(date3);
//        CreateTransport(date3,100,1,1,1.0,drivers,1);
//        drivers=Facade.getInstance().getAvailableDriver(date3);
//        CreateTransport(date3,101,1,6,1.0,drivers,2);
//        drivers=Facade.getInstance().getAvailableDriver(date4);
//        CreateTransport(date4,102,1,5,1.0,drivers,3);
//        drivers=Facade.getInstance().getAvailableDriver(date4);
//        CreateTransport(date4,103,1,1,1.0,drivers,4);
//        addDestination(2,3);
//        addDestination(2,5);
//        addDestination(3,3);
//        addDestination(3,5);
//    }

    public boolean checkIfTransportForOrderExist(int orderID){
        return transportDAO.isTransportForOrder(orderID);
    }

    public void deleteTransportByOrderID(int orderID) {
        if(checkIfTransportForOrderExist(orderID)) {
            Transport transport = getTransportByOrderID(orderID);
                deleteTransport(transport.getIdTransport());
        }
    }

    public Transport getTransportByOrderID(int orderID) {
        Transport transport = transportDAO.getTransportByOrderID(orderID);
        loadTransport(transport);
        return transport;
    }
}