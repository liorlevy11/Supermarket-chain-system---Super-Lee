package ServiceLayer.ServiceObjects;

import BussinessLayer.Managers.TransportManager;
import BussinessLayer.Objects.Driver;
import BussinessLayer.Objects.Transport;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;


public class TransportService {

    private final TransportManager transportManager;

    public TransportService(){
        this.transportManager=TransportManager.getInstance();
    }

    //transportOperations
    public void AddTransport(LocalDateTime date, int driverDocNum, int idsource , int idTruck, double totalWeight, List<Driver> potentialDrivers)  {
        transportManager.CreateTransport(date, driverDocNum, idsource,idTruck,totalWeight,potentialDrivers);
    }

    public TransportS getTransport(int id){
        return new TransportS(transportManager.getTransport(id));
    }


    public void deleteTransport(int idTransport) {
        transportManager.deleteTransport(idTransport);
    }

    public HashMap<Integer , Transport> DisplayTransport() {
        return transportManager.getTransports();
    }

    public void UpdateTransportDate(int idTransport, LocalDateTime date) {
        transportManager.UpdateTransportDate(idTransport, date);
    }


//    public void UpdateTransportLeavingTime(int idTransport, LocalTime leavingTime) {
//        transportManager.UpdateTransportLeavingTime(idTransport, leavingTime);
//    }

    public void UpdateTransportDriverDocNum(int idTransport, int DriverDocNum) {
        transportManager.UpdateTransportDriverDocNum(idTransport, DriverDocNum);
    }

    public void UpdateTransportSource(int idTransport, int idSite) {
        transportManager.UpdateTransportSource(idTransport, idSite);

    }
    public void UpdateTransportTruck(int idTransport, int idTruck) {
        transportManager.UpdateTransportTruck(idTransport,idTruck);
    }
    public void UpdateTransportDriver(int idTransport, Driver driver) {
        transportManager.UpdateTransportDriver(idTransport,driver);
    }
    public void addTransportDestination(int idTransport, int idSite)  {
        transportManager.addDestination(idTransport, idSite);

    }
    public void UpdateTransportTotalWeight(int idTransport, double totalWeight) {
        transportManager.UpdateTransportTotalWeight(idTransport, totalWeight);
    }

    public void RemoveTransportDestination(int idTransport, int idSite) {
        transportManager.RemoveTransportDestination(idTransport, idSite);
    }

    public HashMap<Integer, TransportS> DisplayTransports() {
        HashMap<Integer, TransportS> transportS=new HashMap<>();
        HashMap<Integer, Transport> transports=transportManager.getTransports();
        for(Transport t:transports.values()){
            //System.out.println(t);
            transportS.put(t.getIdTransport(),new TransportS(t));

        }
        return transportS;
    }

    public HashMap<Integer, TransportS> DisplayOldTransport() {
        HashMap<Integer, TransportS> transportS=new HashMap<>();
        HashMap<Integer, Transport> transports=transportManager.getTransports();
        for(Transport t:transports.values()){
            if(t.happened())
                transportS.put(t.getIdTransport(),new TransportS(t));
        }
        return transportS;
    }
    public HashMap<Integer, TransportS> DisplayFutureTransport(int maxDay) {
        HashMap<Integer, TransportS> transportS=new HashMap<>();
        HashMap<Integer, Transport> transports=transportManager.getTransports();
        for(Transport t:transports.values()){
            if(t.happenedInTime(maxDay, t.getDate()))
                transportS.put(t.getIdTransport(),new TransportS(t));
        }
        return transportS;
    }

//    public void LoadData() throws ParseException {
//        transportManager.LoadData();
//    }

    public void DelData(){
        transportManager.DelData();
    }


    public void startingProgram() {
        transportManager.startingProgram();
    }

    public void deleteTransportByOrderID(int orderID) {
        this.transportManager.deleteTransportByOrderID(orderID);
    }
}


