package ServiceLayer.ModulesServices;

import java.text.ParseException;

import BussinessLayer.Objects.ShiftType;
import ServiceLayer.ServiceObjects.DataService;
import ServiceLayer.ServiceObjects.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


public class EmploTransportService {
    private final ServiceModelEmployees employees;
    private final ServiceModelTransform transform;
    private final DataService dataService;
    private static EmploTransportService instance;

    private EmploTransportService() {
        dataService = new DataService();
        employees = new ServiceModelEmployees();
        transform = new ServiceModelTransform();
    }

    public static EmploTransportService getInstance() {
        if (instance == null)
            instance = new EmploTransportService();
        return instance;
    }

    public void AddSite(int idSite, String contactPerson, String phoneNumber, String address, String shippingArea) {
        transform.AddSite(idSite, contactPerson, phoneNumber, address, shippingArea);
    }

    public HashMap<Integer, SiteS> DisplaySites() {
        return transform.DisplaySites();
    }

    public SiteS getSite(int id) {
        employees.TransportManager();
        return transform.getSite(id);
    }

    public void UpdateSiteContactPerson(int idSite, String contactPerson) {
        employees.TransportManager();
        transform.UpdateSiteContactPerson(idSite, contactPerson);
    }

    public void UpdateSitePhoneNumber(int idSite, String phoneNumber) {
        employees.TransportManager();
        transform.UpdateSitePhoneNumber(idSite, phoneNumber);
    }

    public void UpdateSiteAddress(int idSite, String address) {
        employees.TransportManager();
        transform.UpdateSiteAddress(idSite, address);
    }

    public void UpdateSiteShippingArea(int idSite, String shippingArea) {
        employees.TransportManager();
        transform.UpdateSiteShippingArea(idSite, shippingArea);
    }

    //truckOperations
    //add Truck
    //truckManager
    public void AddTruck(String licenseNumber, String model, double truckWeight, double maxWeight, String typeOfLicense) {
        employees.TransportManager();
        transform.AddTruck(licenseNumber, model, truckWeight, maxWeight, typeOfLicense);
    }

    public HashMap<Integer, TruckS> DisplayTrucks() {
        return transform.DisplayTrucks();
    }

    public TruckS getTruck(int id) {
        employees.TransportManager();
        return transform.getTruck(id);
    }

    public void UpdateTruckLicenseNumber(int idTruck, String licenseNumber) {
        employees.TransportManager();
        transform.UpdateTruckLicenseNumber(idTruck, licenseNumber);
    }

    public void UpdateTruckModel(int idTruck, String model) {
        employees.TransportManager();
        transform.UpdateTruckModel(idTruck, model);
    }

    public void UpdateTruckWeight(int idTruck, double weight) {
        employees.TransportManager();
        transform.UpdateTruckWeight(idTruck, weight);
    }

    public void UpdateTruckMaxWeight(int idTruck, double maxWeight) {
        employees.TransportManager();
        transform.UpdateTruckMaxWeight(idTruck, maxWeight);
    }

    public void UpdateTruckLicenseType(int idTruck, String typeOfLicense) {
        employees.TransportManager();
        transform.UpdateTruckLicenseType(idTruck, typeOfLicense);
    }

    public HashMap<Integer, TruckS> displayAvailableTrucks(LocalDateTime date) {
        employees.TransportManager();
        return transform.displayAvailableTrucks(date);
    }

    //transportOperations
    //TransportManager
    //addTransport
    public void AddTransport(LocalDateTime date, int driverDocNum, int idsource, int idTruck, double totalWeight) {
        employees.TransportManager();
        transform.AddTransport(date, driverDocNum, idsource, idTruck, totalWeight, employees.getAvailableDriver(date));
    }

    public TransportS getTransport(int id) {
        employees.TransportManager();
        return transform.getTransport(id);
    }

    public HashMap<Integer, TransportS> DisplayTransports() {
        return transform.DisplayTransports();
    }

    public void deleteTransport(int idTransport) {
        employees.TransportManager();
        transform.deleteTransport(idTransport);
    }


    public void UpdateTransportDate(int idTransport, LocalDateTime date) {
        employees.TransportManager();
        transform.UpdateTransportDate(idTransport, date);
    }

    public void UpdateTransportDriverDocNum(int idTransport, int DriverDocNum) {
        employees.TransportManager();
        transform.UpdateTransportDriverDocNum(idTransport, DriverDocNum);
    }

    public void UpdateTransportTotalWeight(int idTransport, double totalWeight) {
        employees.TransportManager();
        transform.UpdateTransportTotalWeight(idTransport, totalWeight);
    }

    public void UpdateTransportSource(int idTransport, int idSite) {
        employees.TransportManager();
        transform.UpdateTransportSource(idTransport, idSite);
    }

    public void RemoveTransportDestination(int idTransport, int idDestination) {
        employees.TransportManager();
        transform.RemoveTransportDestination(idTransport, idDestination);
    }

    public void addTransportDestination(int idTransport, int idDestination) {
        employees.TransportManager();
        transform.addTransportDestination(idTransport, idDestination);
    }

    public void UpdateTransportDriver(int idTransport, int idDriver) {
        employees.TransportManager();
        transform.UpdateTransportDriver(idTransport, employees.getDriver(idDriver));
    }

    public void UpdateTransportTruck(int idTransport, int idTruck) {
        employees.TransportManager();
        transform.UpdateTransportTruck(idTransport, idTruck);
    }

    public HashMap<Integer, TransportS> DisplayOldTransport() {
        employees.TransportManager();
        return transform.DisplayOldTransport();
    }

    public HashMap<Integer, TransportS> DisplayFutureTransport(int maxDay) {
        employees.TransportManager();
        return transform.DisplayFutureTransport(maxDay);
    }

    public HashMap<Integer, EmployeeS> displayAvailableDrivers(LocalDateTime date) {
        return employees.displayAvailableDrivers(date);
    }

    //shifts
    public void shiftArrange(LocalDate date, String shiftType, Integer shiftMannager, Map<String, LinkedList<Integer>> inputShift) {
        employees.shiftArrange(date, shiftType, shiftMannager, inputShift);
    }

    public void addShift(LocalDate date, String shiftType, Map<String, Integer> shiftStructure) {
        employees.addShift(date, shiftType, shiftStructure);
    }

    public void addShift(LocalDate date, LocalDate date1, String shiftType1) {
        employees.addShift(date, date1, shiftType1);
    }

    public List<ShiftS> displayShift(LocalDate start, LocalDate end) {
        return employees.displayShift(start, end);
    }

    public void deleteArrange(LocalDate date, String shiftType) {
        employees.deleteArrange(date, shiftType);
    }

    public void deleteEmployee(int id, LocalDate date1, String shiftTime) {
        employees.deleteEmployee(id, date1, shiftTime);
    }

    public void addEmployeeToShift(int id, LocalDate date1, String shiftTime) {
        employees.addEmployeeToShift(id, date1, shiftTime);
    }

    public void switchEmployee(int id1, int id2, LocalDate date1, String shiftTime) {
        employees.switchEmployee(id1, id2, date1, shiftTime);
    }

    public void switchManager(int id1, int id2, LocalDate date1, String shiftTime) {
        employees.switchManager(id1, id2, date1, shiftTime);
    }

    public void updateshift(LocalDate date1, String shiftTime, Map<String, Integer> structure) {
        employees.updateshift(date1, shiftTime, structure);
    }

    public void updateJobInShift(LocalDate date, String shiftTime, String job, int num) {
        employees.updateJobInShift(date, shiftTime, job, num);
    }

    public List<String> getJobType(String shiftTime) {
        return employees.getJobType(shiftTime);
    }

    //employee
    public void login(int id) {
        employees.login(id);
    }

    public List<MessageS> getUnreadMessages(String jobType) {
        return employees.getUnreadMessages(jobType);
    }

    //TODO new fun
    public void login(int id, String jobType) {
        employees.login(id, jobType);
    }


    public void logout() {
        employees.logout();
    }

    public void addConstraint(LocalDate date, String shift) {
        employees.addConstraint(date, shift);
    }

    public void deleteConstraint(LocalDate date, String shift) {
        employees.deleteConstraint(date, shift);
    }

    public Map<LocalDate, List<ShiftType>> displayConstraint() {
        return employees.displayConstraint();
    }

    public String myshift() {
        return employees.myshift();
    }

    //manager actions
    public void addEmployee(int id, String name, int bankAccount, int salary, String hiringCondition, String jobType, String licence) {
        employees.addEmployee(id, name, bankAccount, salary, hiringCondition, jobType, licence);
    }

    public List<EmployeeS> displayEmployees() {
        return employees.displayEmployees();
    }

    public String getDetails(int id) {
        return employees.getDetails(id);
    }

    public void finish(int id) {
        employees.finish(id);
    }

    public void updatebank(int id, int bank) {
        employees.updatebank(id, bank);
    }

    public void updateSalary(int id, int salary) {
        employees.updateSalary(id, salary);
    }

    public void updateJob(int id, String jobType) {
        employees.updateJob(id, jobType);
    }

    public void updateHiring(int id, String hiring) {
        employees.updateHiring(id, hiring);
    }

    public void addTrain(int id, String training) {
        employees.addTrain(id, training);
    }

    public String getJobTypeByID(int ID) {
        return employees.getJobTypeByID(ID);
    }

    public Map<String, List<EmployeeS>> getAvailableEmployees(LocalDate date, String shiftType) {
        return employees.displayAvailableEmployees(date, shiftType);
    }

    public boolean isDriver(String jobType) {
        return employees.isDriver(jobType);
    }

    //data operations
//    public void loadData() throws ParseException {
//        dataService.LoadData(employees, transform);
//    }

    public void DelData() throws ParseException {
        dataService.DelData(employees, transform);
    }

    public void startingProgram() throws ParseException {
        //DelData();
        // loadData();
       // dataService.startingProgram(employees, transform);
    }


}
