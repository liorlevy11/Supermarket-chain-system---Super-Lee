package ServiceLayer.ServiceObjects;

import BussinessLayer.Objects.Site;
import BussinessLayer.Objects.Transport;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class TransportS {

    private final int idTransport;
    private final LocalDateTime date;
    private final int driverDocNum;
    private final SiteS source;
    private TruckS truckS;
    private EmployeeS driverS;
    private final List<SiteS> destinations ;
    private final String area;
    private final double maxWeight;
    private int orderID;

    public TransportS(int idTransport, LocalDateTime date, int driverDocNum, Site source, String area, double maxWeight) {
        this.idTransport = idTransport;
        this.date = date;
        this.driverDocNum = driverDocNum;
        this.area=area;
        this.source = new SiteS(source);
        this.destinations = new LinkedList<>();
        this.maxWeight = maxWeight;
        
    }
    public TransportS(int idTransport, LocalDateTime date, int driverDocNum, Site source, String area, double maxWeight,int orderID) {
        this.idTransport = idTransport;
        this.date = date;
        this.driverDocNum = driverDocNum;
        this.area=area;
        this.source = new SiteS(source);
        this.destinations = new LinkedList<>();
        this.maxWeight = maxWeight;
        this.orderID = orderID;
    }
    public TransportS(int idTransport,LocalDateTime date , int driverDocNum, Site source,String area,LinkedList<Site> destinations,double maxWeight) {
        this.idTransport = idTransport;
        this.date = date;
        this.driverDocNum = driverDocNum;
        this.area=area;
        this.source = new SiteS(source);
        this.destinations = copyDest(destinations);
        this.maxWeight = maxWeight;
    }

    public TransportS(Transport transport) {
        this.idTransport =transport.getIdTransport();
        this.date = transport.getDate();
        this.driverDocNum = transport.getDriverDocNum();
        this.area=transport.getStringArea();
        this.source = new SiteS(transport.getSource());
        //System.out.println("The id of the source " + transport.getSource().getIdSite());
        this.destinations = copyDest(transport.getDestinations());
        this.driverS=new EmployeeS(transport.getDriver());
        this.truckS=new TruckS(transport.getTruck());
       // System.out.println("The id of the truck " + transport.getTruck().getIdTrack());
        this.maxWeight = transport.getTotalWeight();
    }

    public LinkedList<SiteS> copyDest(List<Site> destinations){
        LinkedList<SiteS> dest=new LinkedList<>();
        for(Site s:destinations){
            dest.add(new SiteS(s));
        }
        return dest;
    }

    public String displayDes(){
        StringBuilder des= new StringBuilder();

        for(SiteS s:destinations){
            des.append(s.getAddress()).append(", ");
        }
        if (des.length() > 2) {
            des = new StringBuilder(des.substring(0, des.length() - 2));
            des.append(".");
        }

        return des.toString();
    }


    @Override
    public String toString() {
            return idTransport+ ") TIME: " + date +
                " | DRIVER DOCUMENT NUMBER: " + driverDocNum +
                " | SOURCE: " + source.getAddress() +
                " | TRUCK: " + truckS.getLicenseNumber() +
                " | DRIVER: " + driverS.getName() +" ID: "+driverS.getId() +
                " | DESTINATIONS: <" + desanations(destinations) + ">"+
                " | AREA: " + area + '|' +
                " | MAX WEIGHT: " + maxWeight;
    }

    public String desanations(List<SiteS> dest){
        String s="";
        for(SiteS siteS:destinations){
            s+=siteS.getAddress()+" ,";
        }
        if(destinations.size()!=0)
            s=s.substring(0,s.length()-2);
        return s;
    }


    public List<SiteS> getDestinations() {
        return destinations;
    }

    public SiteS getSource() {
        return source;
    }

    public LocalDateTime getDate() {
        return date;
    }
    public double getMaxWeight() {
        return maxWeight;
    }
}
