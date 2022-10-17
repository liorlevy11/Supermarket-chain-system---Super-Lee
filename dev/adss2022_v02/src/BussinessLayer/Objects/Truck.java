package BussinessLayer.Objects;
import java.time.LocalDateTime;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

public class Truck {

    private static int counter = 1;
    private int idTrack;
    private String licenseNumber;
    private String model;
    private double truckWeight;
    private double maxWeight;
    private License typeOfLicence;
    private List<LocalDateTime> workingDays;

    //private Map<Date,Transport> transportMap;

    public Truck(String licenseNumber, String model, double truckWeight, double maxWeight, String license) {
        this.idTrack = counter;
        counter++;
        //System.out.println("The truck id :" + idTrack);
        this.licenseNumber = licenseNumber;
        this.model = model;
        this.truckWeight = truckWeight;
        this.maxWeight = maxWeight;
        //System.out.println("BBBBBBBBBBBBBBBB");
        this.typeOfLicence = convertStringToEnum(license);
        workingDays=new LinkedList<>();
    }

    //load from db
    public Truck(int idTrack, String licenseNumber, String model, double truckWeight, double maxWeight, String license) {
        this.idTrack = idTrack;
        //System.out.println("The truck id :" + idTrack);
        this.licenseNumber = licenseNumber;
        this.model = model;
        this.truckWeight = truckWeight;
        this.maxWeight = maxWeight;
        //System.out.println("Iiiiiii");
        this.typeOfLicence = convertStringToEnumFromDB(license);
        workingDays=new LinkedList<>();
    }

    public static int getCounter() {
        return counter;
    }

    public void checkIfTheWeightIsLegal(double totalWeight) {
        if(totalWeight > maxWeight)
            throw new IllegalArgumentException("The max weight of this truck id: "+idTrack+" is : " + maxWeight);
    }

    public boolean checkIfTheTruckIsAvailable(LocalDateTime date) {
        for(LocalDateTime dateBusy : workingDays) {
            if (isSameDay(dateBusy,date)&& sameShift(dateBusy,date) )
                return false; //check if the transports at the same day, and if so check if at the same shift.
        }
        return true;
    }
    public boolean checkIfTheTruckIsAvailable(LocalDateTime date,double weight) {
        for(LocalDateTime dateBusy : workingDays) {
            if (isSameDay(dateBusy,date)&& sameShift(dateBusy,date) )
                return false; //check if the transports at the same day, and if so check if at the same shift.
        }
        if(weight < maxWeight)
            return true;
        return false;
    }

    public static boolean isSameDay(LocalDateTime timestamp,
                                    LocalDateTime timestampToCompare) {
        return timestamp.truncatedTo(DAYS)
                .isEqual(timestampToCompare.truncatedTo(DAYS));
    }

    public boolean sameShift(LocalDateTime myDate,LocalDateTime newDate){
        if((myDate.getHour()<14 && newDate.getHour()<14)||(myDate.getHour()>=14 && newDate.getHour()>=14))
            return true; //if both at the same shift limits
        return false;
    }
    public License convertStringToEnum(String license) {
        switch (license) {
            case "C+E":
                return License.CE;
            case "B":
                return License.B;
            case "C":
                return License.C;
            case "C1":
                return License.C1;
            default: {
                throw new IllegalArgumentException("license should be B|C|C1|C+E");
            }
        }
    }

    public License convertStringToEnumFromDB(String license) {
        switch (license) {
            case "CE":
                return License.CE;
            case "B":
                return License.B;
            case "C":
                return License.C;
            case "C1":
                return License.C1;
            default: {
                throw new IllegalArgumentException("license should be B|C|C1|C+E");
            }
        }
    }

    public void addWorkday(LocalDateTime date){
        if(!checkIfTheTruckIsAvailable(date))
            throw new IllegalArgumentException("truck id: "+idTrack+" is not available at "+date);
        workingDays.add(date);
    }

    public void RemWorkday(LocalDateTime date){
        boolean found=false;
        for(LocalDateTime workdate:workingDays){
            if (date.getDayOfYear() == workdate.getDayOfYear() && date.getMonth().equals(workdate.getMonth()) &&
                    date.getDayOfMonth() == workdate.getDayOfMonth())
                found=true;
        }
        if(found)
            workingDays.remove(date);
        else
            throw new IllegalArgumentException("The truck has no transport on this date: "+date);
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public String getModel() {
        return model;
    }

    public double getTruckWeight() {
        return truckWeight;
    }

    public double getMaxWeight() {
        return maxWeight;
    }

    public License getTypeOfLicence() { return typeOfLicence; }

    public String getStringTypeOfLicence(){
        return typeOfLicence.toString();
    }

    public static void setCounter(int counter) {
        Truck.counter = counter;
        //System.out.println("!!!!!!!!!!!!!!!!!!!"+counter);
    }

    public void setWorkingDays(List<LocalDateTime> workingDays){
        this.workingDays=workingDays;
    }

    public void setIdTrack(int idTrack) {
        this.idTrack = idTrack;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setTruckWeight(double truckWeight) {
        this.truckWeight = truckWeight;
    }

    public void setMaxWeight(double maxWeight) {
        this.maxWeight = maxWeight;
    }

    public void setTypeOfLicence(License typeOfLicence) {
        this.typeOfLicence = typeOfLicence;
    }

    public int getIdTrack() {
        return idTrack;
    }

    @Override
    public String toString() {
        return  idTrack+") LICENCE NUMBER: " + licenseNumber  +
                " | MODEL: " + model  +
                " | TRUCK WEIGHT: " + truckWeight +
                " | MAX WEIGHT: " + maxWeight +
                " | TYPE OF LICENCE: " + typeOfLicence;
    }

}
