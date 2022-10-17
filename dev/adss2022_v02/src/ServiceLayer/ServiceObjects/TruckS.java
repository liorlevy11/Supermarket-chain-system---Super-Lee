package ServiceLayer.ServiceObjects;
import BussinessLayer.Objects.Truck;

public class TruckS {

    private final int idTrack;
    private final String licenseNumber;
    private final String model;
    private final double truckWeight;
    private final double maxWeight;
    // change to id
    private final String typeOfLicence;

    public TruckS(int idTrack,String licenseNumber, String model, double truckWeight, double maxWeight, String license) {
        this.idTrack = idTrack;
        this.licenseNumber = licenseNumber;
        this.model = model;
        this.truckWeight = truckWeight;
        this.maxWeight = maxWeight;
        this.typeOfLicence = license;
    }

    public TruckS(Truck truck){
        this.idTrack = truck.getIdTrack();
        this.licenseNumber = truck.getLicenseNumber();
        this.model = truck.getModel();
        this.truckWeight = truck.getTruckWeight();
        this.maxWeight = truck.getMaxWeight();
        this.typeOfLicence = truck.getStringTypeOfLicence();
    }

    @Override
    public String toString() {
        return  idTrack+") LICENCE NUMBER: " + licenseNumber  +
                " | MODEL: " + model  +
                " | TRUCK WEIGHT: " + truckWeight +
                " | MAX WEIGHT: " + maxWeight +
                " | TYPE OF LICENCE: " + typeOfLicence;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }
}
