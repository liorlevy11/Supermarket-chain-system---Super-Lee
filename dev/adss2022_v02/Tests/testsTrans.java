//import BussinessLayer.*;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.jupiter.api.*;
//import java.text.ParseException;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//public class testsTrans {
//    public TruckManager truckManager;
//    public TransportManager transportManager;
//    public SiteManager siteManager;
//
//    public testsTrans(){
//        truckManager = TruckManager.getInstance();
//        siteManager = SiteManager.getInstance();
//        transportManager = TransportManager.getInstance();
//        truckManager.DelData();
//        siteManager.DelData();
//        transportManager.DelData();
//        truckManager.startingProgram();
//        siteManager.startingProgram();
//        transportManager.startingProgram();
//    }
//
//    @Before
//    public void setUp() throws ParseException {
//        siteManager.LoadData();
//        truckManager.LoadData();
//        transportManager.LoadData();
//    }
//
//
//    @After
//    public void tearDown()  {
//        truckManager.DelData();
//        siteManager.DelData();
//        transportManager.DelData();
//        truckManager.startingProgram();
//        siteManager.startingProgram();
//        transportManager.startingProgram();
//    }
//
//    public LocalDateTime CreateDate(int num){
//        ZoneId defaultZoneId = ZoneId.systemDefault();
//        LocalDate today=LocalDate.now();
//        LocalDate nextMonth=today.plusDays(num);
//        LocalDateTime date = LocalDateTime.from(nextMonth.atStartOfDay(defaultZoneId).toInstant());
//        return date;
//    }
//
//
//    /// the truck has already transport in the same day
//    @Test
//    public void updateTransportWithTuckThatIsBusyFail()  {
//        try {
//            transportManager.UpdateTransportDate(2, CreateDate(18));
//            Assertions.fail();
//        } catch (Exception e) {
//
//        }
//    }
//
//    @Test
//    /// the truck has already transport in the same day
//    public void updateTransportWithDriverThatIsBusyFail() {
//        try {
//            transportManager.UpdateTransportDate(0, CreateDate(11));
//            Assertions.fail();
//        } catch (Exception e) {
//        }
//    }
//
//
//    @Test
//    //// create size successfully
//    public void createSiteSuccess() {
//        int sizeOfSites = siteManager.getSites().size();
//        siteManager.CreateSite("Rotem Sela", "0507978456", "Tel Aviv Izhak Rabin 5", "South");
//        Assertions.assertEquals(sizeOfSites + 1, siteManager.getSites().size());
//    }
//
//
//    @Test
//    //// create size successfully
//    public void createTruckSuccess() {
//        int sizeOfTrucks = truckManager.getTrucks().size();
//        truckManager.CreateTruck("373738757", "Ferrari", 5, 10, "C1");
//        Assertions.assertEquals(sizeOfTrucks + 1, truckManager.getTrucks().size());
//    }
//
//
//    @Test
//    /// add the same destination twice
//    public void addDestinationTwiceFail() {
//        try {
//            transportManager.addDestination(4, 3);
//            transportManager.addDestination(4, 3);
//            Assertions.fail();
//        } catch (Exception e) {
//
//        }
//    }
//
//
//    @Test
//    //// add two destinations with different shipping area
//    public void createTwoDesWithDiffShippingAreaFails() throws ParseException {
//        try {
//            transportManager.addDestination(4, 5);
//            transportManager.addDestination(4, 6);
//            Assertions.fail();
//        } catch (Exception e) {
//        }
//
//    }
//
//
//
//    @Test
//    //// update the transport date to not legal date
//    public void UpdateTransportDateFails() {
//        try {
//            transportManager.UpdateTransportDate(0, CreateDate(65));
//            Assertions.fail();
//        } catch (Exception e) {
//
//        }
//    }
//
//
//    @Test
//    //// update the transport driver with the not appropriate licenses for the truck
//    public void UpdateTransportDriverFails() {
//        try {
//            transportManager.UpdateTransportDriver(0, EmployeesManager.getInstance().getDriver(208649319));
//            Assertions.fail();
//        } catch (Exception e) {
//
//        }
//    }
//
//
//    @Test
//    //// delete transport that not happened yet
//    public void DeleteTransportSuccess() {
//        try {
//            transportManager.deleteTransport(2);
//            transportManager.getTransport(2);
//            Assertions.fail();
//        } catch (Exception e) {
//
//        }
//    }
//
//    @Test
//    //// add Site
//    public void AddSite() {
//        try {
//            siteManager.CreateSite("sgsggs","","amamam","Hello");
//            Assertions.fail();
//        } catch (Exception e) {
//
//        }
//    }
//}
//
//
//
//
//
//
