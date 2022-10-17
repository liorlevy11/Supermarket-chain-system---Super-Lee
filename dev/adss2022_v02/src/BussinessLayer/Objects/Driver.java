package BussinessLayer.Objects;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

public class Driver extends Employee {

    private double Distance;
    private final List<License> licenses;

    public List<LocalDateTime> transports;

    private double avgDes;

    public Driver(int id, String name, int bankAccount, int salary, String hiringCondition, String jobType, String licence) {
        super(id, name, bankAccount, salary, hiringCondition, jobType);
        this.transports = new LinkedList<>();
        this.licenses = new LinkedList<>();
        convertStringToList(licence);
        Distance = 0;
        avgDes = 0;
    }

    //for db
    public Driver(int id, String name, int bankAccount, int salary, String hiringCondition, LocalDate startOfEmployment, Boolean finishWorking, String jobType) {
        super(id, name, bankAccount, salary, hiringCondition, startOfEmployment, finishWorking, jobType);
        Distance = 0;
        this.transports = new LinkedList<>();
        this.licenses = new LinkedList<>();
        avgDes = 0;
    }


    public void addTransform(LocalDateTime date) {
        ///if (checkIfTheDriverIsAvailable(date))
        transports.add(date);
    }

    public void removeTransport(Transport transport){
        if(transports.contains(transport.getDate()))
            transports.remove(transport.getDate());
        this.Distance=this.Distance-(transport.getDestination().size()*50);
    }


    public void deleteTransform(LocalDateTime date) {
        if (transports.contains(date)) {
            transports.remove(date);
        } else
            throw new IllegalArgumentException("The driver has no transport on this date: " + date);
    }


    public boolean checkIfTheDriverIsAvailable(LocalDateTime transportDateTime) {
       if (inShift(transportDateTime.toLocalDate(), shiftType(transportDateTime))) {//if driver Placed for shift at transportTime
            for (LocalDateTime s : transports) {
                if (isSameDay(transportDateTime,s) && sameShift(transportDateTime, s))
                    return false; //if dates are equal - check if driver has transport
            }
            return true;
        }
        return false;
    }

    public static boolean isSameDay(LocalDateTime timestamp,
                                    LocalDateTime timestampToCompare) {
        return timestamp.truncatedTo(DAYS)
                .isEqual(timestampToCompare.truncatedTo(DAYS));
    }

    public boolean sameShift(LocalDateTime myDate, LocalDateTime newDate) {
        if ((myDate.getHour() < 14 && newDate.getHour() < 14) || (myDate.getHour() >= 14 && newDate.getHour() >= 14))
            return true; //if both at the same shift limits
        return false;
    }

    public ShiftType shiftType(LocalDateTime date) {
        if (date.getHour() < 14)
            return ShiftType.Morning;
        return ShiftType.Evening;
    }

    public void convertStringToList(String licensesString) {
        String[] split = licensesString.split(" ");
        for (String str : split)
            licenses.add(licence(str));
    }

    public void setLicenses(List<String> l) {
        for (String s : l) {
            licenses.add(licenceFromDB(s));
        }
    }

    public void setTransports(List<LocalDateTime> transports) {
        this.transports = transports;
    }

    public double getAvgDistance() {
        setAvgDistance();
        return avgDes;
    }

    public List<String> getStringlicenses() {
        List<String> l = new ArrayList<>();
        for (License license : licenses) {
            l.add(license.toString());
        }
        return l;
    }


    // we assumed that we get the licenses with spaces;
    public boolean driverCanDrive(License license) {
        return licenses.contains(license);
    }

    public License licence(String license) {
        switch (license) {
            case "B":
                return License.B;
            case "C":
                return License.C;
            case "C1":
                return License.C1;
            case "C+E":
                return License.CE;
            default:
                throw new IllegalArgumentException("license should be B|C|C1|C+E");
        }
    }

    public License licenceFromDB(String license) {
        switch (license) {
            case "B":
                return License.B;
            case "C":
                return License.C;
            case "C1":
                return License.C1;
            case "CE":
                return License.CE;
            default:
                throw new IllegalArgumentException("license should be B|C|C1|C+E");
        }
    }

    public String getShift() {
        StringBuilder s = new StringBuilder(super.getShift());
        s.append("MY TRANSPORT: \n");
        for (LocalDateTime d : transports) {
            s.append(d).append("\n");
        }
        return s.toString();
    }

    public String getDetails() {
        StringBuilder s = new StringBuilder(super.getDetails());
        s.append(" | LICENSES: ");
        for (License l : licenses) {
            s.append(l).append(" ");
        }
        return s.toString();
    }

    public void setAvgDistance() {
        if(transports.size()==0)
            this.avgDes=0;
        else
            this.avgDes = Distance / transports.size();
    }

    public void addDistance(double Distance) {
        this.Distance += Distance;
        setAvgDistance();
    }

    public double getDistance(){return this.Distance;}

    public void setDistance(double distance) {
        Distance = distance;
        setAvgDistance();
    }

    @Override
    public String toString() {
        String s="ID: "+id+" | NAME: "+name+" | INFO: ";
        if(finishWorking)
            s=s+" | FINISH TO WORK";
        else {
            s = s + " | JOB TYPE: " + jobType;
            if (training.contains("CancellationCard")&&training.contains("TeamManagement"))
                s = s + " | CAN BE SHIFT MANAGER";
            s= s+"| LICENSES ";
            for(License l:licenses){
                s=s+ l.toString()+" ";
            }

            s= s+"| SHIFTS ";
           for(LocalDate l:shifts.keySet()){
               s=s+" "+l+" "+shifts.get(l);
           }

            s= s+"| TRANSPORTS ";
            for(LocalDateTime l:transports){
                s=s+ l.toString()+" ";
            }
        }
        return s;
    }
}

