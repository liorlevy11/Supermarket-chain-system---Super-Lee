package ServiceLayer.ServiceObjects;

import BussinessLayer.Objects.Truck;
import BussinessLayer.Managers.TruckManager;

import java.time.LocalDateTime;
import java.util.HashMap;

public class TruckService {

    private final TruckManager truckManager;

    public TruckService(){
        truckManager=TruckManager.getInstance();
    }

    //truckOperations
    //add Truck
    //truckManager
    public void AddTruck(String licenseNumber, String model, double truckWeight, double maxWeight, String typeOfLicence) {
        truckManager.CreateTruck(licenseNumber, model, truckWeight, maxWeight, typeOfLicence);
    }

    public HashMap<Integer, TruckS> DisplayTrucks() {
        HashMap<Integer, TruckS> truckS=new HashMap<>();
        HashMap<Integer, Truck> trucks=truckManager.getTrucks();
        for(Truck t:trucks.values()){
            truckS.put(t.getIdTrack(),new TruckS(t));
        }
        return truckS;
    }

    public HashMap<Integer, TruckS> displayAvailableTrucks(LocalDateTime date) {
        HashMap<Integer, TruckS> truckS=new HashMap<>();
        HashMap<Integer, Truck> trucks=truckManager.displayAvailableTrucks(date);
        for(Truck t:trucks.values()){
            truckS.put(t.getIdTrack(),new TruckS(t));
        }
        return truckS;
    }

    public TruckS getTruck(int id){
        return new TruckS(truckManager.getTruck(id));
    }

    public void UpdateTruckLicenseNumber(int idTruck, String licenseNumber) {
         truckManager.UpdateTruckLicenseNumber(idTruck,licenseNumber);
    }

    public void UpdateTruckModel(int idTruck , String model) {
         truckManager.UpdateTruckModel(idTruck,model);

    }

    public void UpdateTruckWeight(int idTruck, double weight) {
         truckManager.UpdateTruckWeight(idTruck,weight);

    }

    public void UpdateTruckMaxWeight(int idTruck, double maxWeight) {
         truckManager.UpdateTruckMaxWeight(idTruck,maxWeight);

    }
    public void UpdateTruckLicenseType(int idTruck,String typeOfLicence)  {
         truckManager.UpdateTruckLicenseType(idTruck,typeOfLicence);

    }


    public void LoadData(){
        truckManager.LoadData();
    }

    public void DelData(){
        truckManager.DelData();
    }


    public void startingProgram() {
        truckManager.startingProgram();
    }
}
