package BussinessLayer.Managers;

import BussinessLayer.Objects.Truck;
import DataAccessLayer.TruckDAO;

import java.time.LocalDateTime;
import java.util.*;

public class TruckManager {

    private static TruckManager truckManager = null;

    private TruckDAO truckDAO;

    // Constructor
    // Here we will be creating private constructor
    // restricted to this class itself
    private TruckManager() {
//        trucks=new HashMap<>();
        truckDAO = new TruckDAO();
    }

    // Static method
    // Static method to create instance of Singleton class
    public static TruckManager getInstance() {
        if (truckManager == null)
            truckManager = new TruckManager();

        return truckManager;
    }

    public void CreateTruck(String licenseNumber, String model, double truckWeight, double maxWeight, String typeOfLicence) {
        checkIfTheLicenseNumberAlreadyExist(licenseNumber);
        Truck truck = new Truck(licenseNumber, model, truckWeight, maxWeight, typeOfLicence);
        truckDAO.Insert(truck);
    }

    public Truck getTruck(int idTruck) {
        boolean fromdb=true;
        if(truckDAO.inCache(idTruck))
            fromdb=false;
        Truck truck= truckDAO.getTruck(idTruck);
        if(fromdb)
            loadTruck(truck);
        return truck;
    }

    public void loadTruck(Truck truck){
        truck.setWorkingDays(TransportManager.getInstance().getTruckTransports(truck.getIdTrack()));
    }

    public HashMap<Integer, Truck> getTrucks() {
        List<Truck> truckList = truckDAO.SelectAllTrucks();
        HashMap<Integer, Truck> trucks = new HashMap<>();
        for (Truck truck : truckList) {
            loadTruck(truck);
            trucks.put(truck.getIdTrack(), truck);
        }
        return trucks;
    }

    public void checkIfTheLicenseNumberAlreadyExist(String licenseNumber) {
        if (truckDAO.ExistLicenseNumber(licenseNumber))
            throw new IllegalArgumentException("truck license number: " + licenseNumber + " already exist in our system");
    }

    public void UpdateTruckLicenseNumber(int idTruck, String licenseNumber) {
        checkIfTheLicenseNumberAlreadyExist(licenseNumber);
        getTruck(idTruck).setLicenseNumber(licenseNumber);
        truckDAO.setLicenseNumber(licenseNumber, idTruck);
    }

    public void UpdateTruckModel(int idTruck, String model) {
        getTruck(idTruck).setModel(model);
        truckDAO.setModel(model, idTruck);
    }

    public void UpdateTruckWeight(int idTruck, double weight) {
        getTruck(idTruck).setTruckWeight(weight);
        truckDAO.setTruckWeight(weight, idTruck);

    }

    public void UpdateTruckMaxWeight(int idTruck, double maxWeight) {
        getTruck(idTruck).setMaxWeight(maxWeight);
        truckDAO.setMaxWeight(maxWeight, idTruck);

    }

    public void UpdateTruckLicenseType(int idTruck, String typeOfLicense) {
        Truck truck = truckDAO.getTruck(idTruck);
        truck.setTypeOfLicence(truck.convertStringToEnum(typeOfLicense));

        //special case for type of license
        if (typeOfLicense.equals("C+E"))
            truckDAO.setTypeOfLicence("CE", idTruck);
        else
            truckDAO.setTypeOfLicence(typeOfLicense, idTruck);

    }

    public HashMap<Integer, Truck> displayAvailableTrucks(LocalDateTime date) {
        HashMap<Integer, Truck> availableTrucks = new HashMap<>();
        List<Truck> trucks = truckDAO.SelectAllTrucks();
        for (Truck truck : trucks) {
            if (truck.checkIfTheTruckIsAvailable(date))
                availableTrucks.put(truck.getIdTrack(), truck);
        }
        return availableTrucks;
    }
    public HashMap<Integer, Truck> displayAvailableTrucksW(LocalDateTime date,double weight) {
        HashMap<Integer, Truck> availableTrucks = new HashMap<>();
        List<Truck> trucks = truckDAO.SelectAllTrucks();
        for (Truck truck : trucks) {
            if (truck.checkIfTheTruckIsAvailable(date,weight))
                availableTrucks.put(truck.getIdTrack(), truck);
        }
        return availableTrucks;
    }
    public void LoadData() {
        CreateTruck("998773224", "Toyota", 2, 10000, "B");
        CreateTruck("44772218", "Hyundai", 4, 10000, "C1");
        CreateTruck("82821113", "Mercedes-benz", 2.5, 10000, "C");
        CreateTruck("45457284", "BMW", 1.3, 10000, "C1");
        CreateTruck("123456789", "Jeep", 5, 10000, "B");
        CreateTruck("99876321", "Lexus", 8.9, 10000, "B");
        CreateTruck("557723145", "Tesla", 6, 10000, "C+E");
    }

    public void DelData() {
        truckDAO.resetCache();
        truckDAO.Delete();
    }

    public void resetCounter() {
        Truck.setCounter(truckDAO.getMaxid() + 1);
    }

    public void startingProgram() {
        resetCounter();
    }
}
