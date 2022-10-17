package BussinessLayer.Objects;
import DataAccessLayer.TransportDAO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Transport {

    private static int counter = 1;
    private int idTransport;
    private LocalDateTime date;
    private int driverDocNum;
    private Site source;
    private Truck truck;
    private Driver driver;
    private List<Site> destinations ;
    private Site.ShippingArea area;
    private static int maxDateDiff = 60;
    private static int minDateDiff = 1;
    private static int milesForDes = 50;
    private double totalWeight;
    private int orderID;

    private boolean isReceived;
    //private double distance;

    public Transport(LocalDateTime date, int driverDocNum, Site source , Truck truck , List<Driver> potentialDrivers , double totalWeight ,int idOrder ) {
        this.idTransport = counter;
        counter++;
        validDateBefore(date,minDateDiff);
        validDateAfter(date,maxDateDiff);
        this.date = date;
        this.driverDocNum = driverDocNum;
        this.area= Site.ShippingArea.Center;
        this.source = source;
        this.destinations = new LinkedList<>();
        this.totalWeight = totalWeight;
        //this.distance=0;
        setTruck(truck);
        setDriver(potentialDrivers);
        this.orderID = idOrder;
        this.isReceived = false;
    }

    //load from db
    public Transport(int idTransport, LocalDateTime date, int driverDocNum, double totalWeight ,int idOrder ,boolean isReceived) {
        this.idTransport = idTransport;
        this.date = date; //No input test required
        this.driverDocNum = driverDocNum;
        this.area= Site.ShippingArea.Center;
        this.destinations = new LinkedList<>();
        this.totalWeight = totalWeight;
        this.orderID = idOrder;
        //source, site, truck , driver - loaded at transport manager
        this.isReceived = isReceived;
    }


    public static void setCounter(int counter) {
        Transport.counter = counter;
    }

    /// RETURNS ONLY THE TIME FROM THE DATE
    public LocalTime getTime(){
        LocalTime localTime = LocalTime.of(date.getHour(),date.getMinute());
        return localTime;
    }
    /// RETURNS ONLY THE DATE WITHOUT TIME FROM THE DATE
    public LocalDate getTheDate(){
        LocalDate localDate =  date.toLocalDate();
        return localDate;
    }

    public boolean isReceived() {
        return this.isReceived;
    }
    public void setIsReceived(boolean isReceived) {
        this.isReceived = isReceived;
    }

    public void RemoveTransportDestination(Site destination) {
        if(destinations.contains(destination)){
            destinations.remove(destination);
        }
        else
            throw new IllegalArgumentException("destination "+destination.getAddress()+" does not part of transport destinations");
        calculateMiles(-milesForDes);
    }

    public void addDestination(Site destination){
        if(destinations.contains(destination)) {
            //System.out.println("ADD TRANSPORT");
            throw new IllegalArgumentException("destination already exists to this transport");
        }
        if(destination.equals(source))
            throw new IllegalArgumentException("transport destination can not be the transport source");
        if(destinations.isEmpty()) {
            destinations.add(destination);
            area=destination.getShippingArea();
        }
//        else if(destination.SameShippingArea(area))
            destinations.add(destination);
//        else
//            throw new IllegalArgumentException("this shipping area destination does not suitable to this transport");
        calculateMiles(milesForDes);
    }

    public void validDateBefore(LocalDateTime date , int num){
        LocalDateTime today=LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        if(date.isBefore(today))
            throw new IllegalArgumentException("the date has passed");
        if(date.equals(today))
            throw new IllegalArgumentException("the date must be " + num + "day from today");
    }

    public void validDateAfter(LocalDateTime date , int num){
        LocalDateTime today=LocalDateTime.now();
        LocalDateTime nextMonth=today.plusDays(num);
        if(date.isAfter(nextMonth))
            throw new IllegalArgumentException("You can schedule transport up to " + num + " days from today");
    }

    /// update the date of transport only if :
    // 1) the date is between the min and the max diff
    // 2) the driver and the truck are available in this day
    public void UpdateTransportDate(LocalDateTime dateCurrent) {
        validDateAfter(dateCurrent ,maxDateDiff);
        validDateBefore(dateCurrent ,minDateDiff);
        if(!driver.checkIfTheDriverIsAvailable(dateCurrent))
            throw new IllegalArgumentException("Driver :"+driver.getName()+" that assign to this transport does not available in "+dateCurrent);
        driver.addTransform(dateCurrent);
        driver.deleteTransform(date);
        truck.addWorkday(dateCurrent);
        truck.RemWorkday(date);
        setDate(dateCurrent);
    }

    public void UpdateTransportDriverDocNum(int driverDocNum) {
        setDriverDocNum(driverDocNum);
    }

    public void UpdateTransportSource(Site source) {
        setSource(source);
    }

    public boolean happened(){
        LocalDateTime today = LocalDateTime.now();
        System.out.println("now" + LocalDateTime.now());
        LocalDateTime dateConverted = date;
        System.out.println("transport " + dateConverted);
        return dateConverted.isBefore(today);

    }

    public boolean happenedInTime(int time,LocalDateTime dateToCheck){
        LocalDate today = LocalDate.now();
        long days = ChronoUnit.DAYS.between(today, dateToCheck);
        return days < time;
    }

    public String displayDes(){
        StringBuilder des= new StringBuilder();
        for(Site s:destinations){
            des.append(s.getAddress()).append(", ");
        }
        des = new StringBuilder(des.substring(0, des.length() - 2));
        des.append(".");
        return des.toString();
    }


    /////////////// getters and setters

    public int getIdTransport() {
        return idTransport;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getDriverDocNum() {
        return driverDocNum;
    }

    public void setDriverDocNum(int driverDocNum) {
        this.driverDocNum = driverDocNum;
    }

    public Site getSource() {
        return source;
    }

    public void setSource(Site source) {
        this.source = source;
    }

    public List<Site> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<Site> destinations) {
        this.destinations = destinations;
    }

    public Site.ShippingArea getArea() {
        return area;
    }

    public String getStringArea() {
        return area.toString();
    }

    public void setArea(Site.ShippingArea area) {
        this.area = area;
    }

    public static int getMaxDateDiff() {
        return maxDateDiff;
    }

    public List<Site> getDestination(){
        return destinations;
    }

    public static void setMaxDateDiff(int maxDateDiff) {
        Transport.maxDateDiff = maxDateDiff;
    }

    public static int getMinDateDiff() {
        return minDateDiff;
    }

    public static void setMinDateDiff(int minDateDiff) {
        Transport.minDateDiff = minDateDiff;
    }

    public Integer getTruckID() {
        return truck.getIdTrack();
    }
    public Integer getSiteID() {
        return source.getIdSite();
    }
    public Truck getTruck() {
        return truck;
    }

    public Driver getDriver() {
        return driver;
    }
    public void setValidTruck(Truck truck){
        this.truck=truck;
    }

    public void setValidDriver(Driver driver){
        this.driver=driver;
    }
    public void setTruck(Truck truck) {
        truck.checkIfTheWeightIsLegal(totalWeight);
        if(driver!=null) {
            if (!driver.driverCanDrive(truck.getTypeOfLicence())) ;
            throw new IllegalArgumentException("Driver :" + driver.getName() + " that assign to this transport has no sutiable license to truck " + truck.getLicenseNumber());
        }
        this.truck = truck;
        truck.addWorkday(date);
    }

    public void setDriver(List<Driver> potentialDrivers) {
        List<Driver> potential=findSuitableDrivers(potentialDrivers);
        if(potential.size()==0)
            throw new IllegalArgumentException("Cannot place Driver to this transport, Please make sure that there are available drivers who are assigned to the shift "+date+" who hold the license "+truck.getTypeOfLicence() );
        this.driver=chooseDriver(potential);
        driver.addTransform(date);
    }

    //used when driver updated
    public void setDriver(Driver driver) {
        if(!driver.driverCanDrive(truck.getTypeOfLicence()))
            throw new IllegalArgumentException("Driver has no sutiable license to truck, required license: "+truck.getTypeOfLicence() );
       if(!driver.checkIfTheDriverIsAvailable(date))
           throw new IllegalArgumentException("Driver "+driver.getName()+" is not available in : "+date );
       this.driver.deleteTransform(this.date);
       this.driver.setDistance(this.driver.getDistance()-(50*destinations.size()));
       this.driver=driver;
       driver.addTransform(date);
       this.driver.setDistance(driver.getDistance()+(50*destinations.size()));
    }

    public void calculateMiles(double des){
        driver.addDistance(des);
    }

    public Driver chooseDriver(List<Driver> potential){//returns the driver that drove the least
        double minDis=potential.get(0).getAvgDistance();
        Driver driver=potential.get(0);
        for(Driver d:potential){
            if(d.getAvgDistance()<minDis){
                minDis=d.getAvgDistance();
                driver=d;
            }
        }
        return driver;
    }

    public List<Driver> findSuitableDrivers(List<Driver> potentialDrivers){
        List<Driver> canDrive=new ArrayList<>();
        for(Driver d:potentialDrivers){
            if(d.driverCanDrive(truck.getTypeOfLicence()))
                canDrive.add(d);
        }
        return canDrive;
    }

    public void setTotalWeight(double totalWeight) {
        if(totalWeight>truck.getMaxWeight())
            throw new IllegalArgumentException("total weight is not legal for truck id: "+truck.getIdTrack());
        this.totalWeight = totalWeight;
    }

    public double getTotalWeight() {
        return totalWeight;
    }
    @Override
    public String toString() {
        return "Transport{" +
                "idTransport=" + idTransport +
                ", date=" + date +
                ", driverDocNum=" + driverDocNum +
                ", source=" + source +
                ", truck=" + truck +
                ", driver=" + driver +
                ", destinations=" + destinations +
                ", area=" + area +
                '}';
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }
}
