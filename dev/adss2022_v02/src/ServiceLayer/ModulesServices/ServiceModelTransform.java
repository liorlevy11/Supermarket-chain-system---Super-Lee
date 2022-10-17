package ServiceLayer.ModulesServices;

import BussinessLayer.Objects.Driver;
import ServiceLayer.ServiceObjects.*;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

public class ServiceModelTransform {
    private final SiteService siteService;
    private final TransportService transportService;
    private final TruckService truckService;

    public ServiceModelTransform() {
        siteService = new SiteService();
        transportService = new TransportService();
        truckService = new TruckService();
    }

    public void AddSite(int idSite , String contactPerson, String phoneNumber, String address, String shippingArea) {
        siteService.AddSite(idSite,contactPerson, phoneNumber, address, shippingArea);
    }

    public HashMap<Integer, SiteS> DisplaySites() {
        return siteService.DisplaySites();
    }

    public SiteS getSite(int id) {
        return siteService.getSite(id);
    }

    public void UpdateSiteContactPerson(int idSite, String contactPerson) {
        siteService.UpdateSiteContactPerson(idSite, contactPerson);
    }

    public void UpdateSitePhoneNumber(int idSite, String phoneNumber) {
        siteService.UpdateSitePhoneNumber(idSite, phoneNumber);
    }

    public void UpdateSiteAddress(int idSite, String address) {
        siteService.UpdateSiteAddress(idSite, address);
    }

    public void UpdateSiteShippingArea(int idSite, String shippingArea) {
        siteService.UpdateSiteShippingArea(idSite, shippingArea);
    }

    //truckOperations
    //add Truck
    //truckManager
    public void AddTruck(String licenseNumber, String model, double truckWeight, double maxWeight, String typeOfLicense) {
        truckService.AddTruck(licenseNumber, model, truckWeight, maxWeight, typeOfLicense);
    }

    public HashMap<Integer, TruckS> DisplayTrucks() {
        return truckService.DisplayTrucks();
    }

    public TruckS getTruck(int id) {
        return truckService.getTruck(id);
    }

    public void UpdateTruckLicenseNumber(int idTruck, String licenseNumber) {
        truckService.UpdateTruckLicenseNumber(idTruck, licenseNumber);
    }

    public void UpdateTruckModel(int idTruck, String model) {
        truckService.UpdateTruckModel(idTruck, model);
    }

    public void UpdateTruckWeight(int idTruck, double weight) {
        truckService.UpdateTruckWeight(idTruck, weight);
    }

    public void UpdateTruckMaxWeight(int idTruck, double maxWeight) {
        truckService.UpdateTruckMaxWeight(idTruck, maxWeight);
    }

    public void UpdateTruckLicenseType(int idTruck, String typeOfLicense)  {
        truckService.UpdateTruckLicenseType(idTruck, typeOfLicense);
    }

    public HashMap<Integer, TruckS> displayAvailableTrucks(LocalDateTime date) {
        return truckService.displayAvailableTrucks(date);
    }

    //transportOperations
    //TransportManager
    //addTransport
    public void AddTransport(LocalDateTime date, int driverDocNum, int idsource, int idTruck, double totalWeight, List<Driver>  potentialDrivers)  {
        transportService.AddTransport(date, driverDocNum, idsource, idTruck, totalWeight, potentialDrivers);
    }

    public TransportS getTransport(int id) {
        return transportService.getTransport(id);
    }

    public HashMap<Integer, TransportS> DisplayTransports() {
        return transportService.DisplayTransports();
    }

    public void deleteTransport(int idTransport) {
        transportService.deleteTransport(idTransport);
    }


    public void UpdateTransportDate(int idTransport, LocalDateTime date) {
        transportService.UpdateTransportDate(idTransport, date);
    }

    public void UpdateTransportDriverDocNum(int idTransport, int DriverDocNum) {
        transportService.UpdateTransportDriverDocNum(idTransport, DriverDocNum);
    }

    public void UpdateTransportTotalWeight(int idTransport, double totalWeight) {
        transportService.UpdateTransportTotalWeight(idTransport, totalWeight);
    }

    public void UpdateTransportSource(int idTransport, int idSite) {
        transportService.UpdateTransportSource(idTransport, idSite);
    }

    public void RemoveTransportDestination(int idTransport, int idDestination) {
        transportService.RemoveTransportDestination(idTransport, idDestination);
    }

    public void addTransportDestination(int idTransport, int idDestination)  {
        transportService.addTransportDestination(idTransport, idDestination);
    }
    public void UpdateTransportDriver(int idTransport, Driver driver) {
        transportService.UpdateTransportDriver(idTransport ,driver);
    }

    public void UpdateTransportTruck(int idTransport, int idTruck) {
        transportService.UpdateTransportTruck(idTransport ,idTruck);
    }

    public HashMap<Integer, TransportS> DisplayOldTransport() {
        return transportService.DisplayOldTransport();
    }

    public HashMap<Integer, TransportS> DisplayFutureTransport(int maxDay) {
        return transportService.DisplayFutureTransport(maxDay);
    }

    public void LoadData() throws ParseException {
        truckService.LoadData();
        siteService.LoadData();
        //transportService.LoadData();

    }

    public void DelData() throws ParseException {
        truckService.DelData();
        siteService.DelData();
        transportService.DelData();
    }

    public void startingProgram() {
        truckService.startingProgram();
        siteService.startingProgram();
        transportService.startingProgram();
    }

    public void deleteTransportByOrderID(int orderID) {
        this.transportService.deleteTransportByOrderID(orderID);
    }
}
