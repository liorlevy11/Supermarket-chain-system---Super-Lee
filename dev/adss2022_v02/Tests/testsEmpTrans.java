import BussinessLayer.Managers.*;
import BussinessLayer.Objects.*;
import DataAccessLayer.Dal.*;
import ServiceLayer.ModulesServices.IntegratedService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.Assert.*;

public class testsEmpTrans {
    public Facade facade;
    public TruckManager truckManager;
    public TransportManager transportManager;
    public SiteManager siteManager;
    public SupplierManager supplierManager;

    DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm");
    DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDateTime today;
    LocalDate today1;

    LocalDateTime tomorrow;
    LocalDate tomorrow1;

    LocalDateTime date3;
    LocalDate date31;

    LocalDateTime date4;
    LocalDate date41;
    LocalDateTime date5;
    LocalDateTime date6;


    public testsEmpTrans() throws ParseException {
//        IntegratedService.getInstance().loadData();
        facade = Facade.getInstance();
        supplierManager = new SupplierManager();
        truckManager = TruckManager.getInstance();
        siteManager = SiteManager.getInstance();
        transportManager = TransportManager.getInstance();
        today= LocalDateTime.now();
        today1= LocalDate.now();

        tomorrow = today.plusDays(1);
        tomorrow1= LocalDate.now();
        date3 = today.plusDays(10).withHour(15);
        date31 = today1.plusDays(10);
        date4 =today.plusDays(9);
        date41 = today1.plusDays(9);

        date5 = today.plusDays(6);


        facade.DelData();
        truckManager.DelData();
        siteManager.DelData();
//        supplierManager.DelData();
        transportManager.DelData();
        truckManager.startingProgram();
        siteManager.startingProgram();
        transportManager.startingProgram();
    }

    @Before
    public void setUp() throws ParseException {
        truckManager.LoadData();
//        supplierManager.loadData();
        siteManager.LoadData();
        facade.loadData();

        //transportManager.LoadData();
        facade.Login(207687849);
    }

    @After
    public void tearDown() {
//        supplierManager.DelData();
        facade.DelData();
        truckManager.DelData();
        siteManager.DelData();
        transportManager.DelData();
        truckManager.startingProgram();
        siteManager.startingProgram();
        transportManager.startingProgram();
    }

    @Test
    public void availableDriverSuccess(){
        List<Driver>drivers=facade.getAvailableDriver(date3);
        assertNotEquals(drivers.size(),0);
    }

    @Test
    public void storeKeeperLessThenZero(){
        try {
            facade.deleteEmployee(209876544,date41,"Morning");
            fail("cant be 0");
        }
        catch (Exception  e){
        }
    }

    //try to create transport in day with out shift
    @Test
    public void createTransportFail(){
        try {
            List<Driver>drivers=facade.getAvailableDriver(date3);
            transportManager.CreateTransport(date3,143,1,1,1.0,drivers);
            fail("cant create transport in day with out available drivers");
        }
        catch (Exception  e){
        }
    }
    // creating site
    @Test
    public void createSiteSucc(){
        List<Driver>drivers1=facade.getAvailableDriver(date3);
        siteManager.CreateSite(1,"shay","0508380280","kgc","Center");
        assertEquals(siteManager.getSites().size() > 0,true);
    }

    // creating truck
    @Test
    public void createTruckSucc() {
        truckManager.CreateTruck("1212","Reno",5.5,15.3,"B");
        assertEquals(truckManager.getTrucks().size() > 0,true);
    }

    // updating the license number of the truck
    @Test
    public void test2() {
        truckManager.CreateTruck("1212","Reno",5.5,15.3,"B");
        truckManager.UpdateTruckLicenseNumber(1,"1515");
        assertNotEquals(truckManager.getTruck(1).getLicenseNumber(),"1212");

    }
    //put 2 truck in the same shift type
    @Test
    public void truckInTransportFail(){
        try {
            List<Driver>drivers1=facade.getAvailableDriver(date3);
            List<Driver>drivers2=facade.getAvailableDriver(date3);
            transportManager.CreateTransport(date3,143,1,1,1.0,drivers1);
            transportManager.CreateTransport(date3,143,1,1,1.0,drivers2);
            fail("cant create transport in day with out available drivers");
        }
        catch (Exception  e){
        }
    }


    //check switch manager
    @Test
    public void SwitchManagerSucceed() {
        facade.switchManager(208687319,202876546,date31,"Evening");
        List<Shift> l=facade.displayShift(date31.minusDays(1),date31.plusDays(1));
        Shift shift=l.get(0);
        if(shift!=null)
            assertEquals(shift.getManager(),202876546);
        else
            fail();
    }

    @Test
    public void SwitchManagerFail(){
        try {
            facade.switchManager(208687319, 207687849, date41, "Evening");
            List<Shift> l = facade.displayShift(date31, date41);
            Shift shift = null;
            for (Shift s : l) {
                if (s.getDate().isEqual(date41) && s.getShiftType().equals(ShiftType.Evening))
                    shift = s;
            }
            fail("Noga cant be shift manager");
        }
        catch(Exception e){}
    }

    //fail because the new driver not available
    @Test
    public void UpdateDriverFail(){
        try {
            transportManager.UpdateTransportDriver(1, facade.getDriver(209120278));
            fail("Mor hasnt have shift yet");
        }
        catch(Exception e){}
    }



}