import DataAccessLayer.Dal.DBHandler;
import ServiceLayer.ModulesServices.IntegratedService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class integrationTests {
    IntegratedService service = IntegratedService.getInstance();
//    public Facade facade;
//    public TruckManager truckManager;
//    public TransportManager transportManager;
//    public SiteManager siteManager;
//    public OrderManager orderManager;
//    public SupplierManager supplierManager;
//    OrderManager ordermanager = new OrderManager();


//    DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm");
//    DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//    LocalDateTime date1;
//    LocalDateTime date2;
//    LocalDateTime date3;
//    LocalDate date4;
//    LocalDate date5;
//    LocalDate date6;


    public integrationTests() throws ParseException {
//        facade = Facade.getInstance();
////        truckManager = TruckManager.getInstance();
////        siteManager = SiteManager.getInstance();
////        transportManager = TransportManager.getInstance();
////        orderManager = new OrderManager();
////        supplierManager = new SupplierManager();
////        date1 = LocalDateTime.parse("25/06/2022-16:00", formatters);
////        date2 = LocalDateTime.parse("01/06/2022-10:00", formatters);
////        date3 = LocalDateTime.parse("25/06/2022-15:00", formatters);
////        date4 = LocalDate.parse("25/06/2022", formatter2);
////        date5 = LocalDate.parse("01/01/2022", formatter2);
////        date6 = LocalDate.parse("01/01/2023", formatter2);
////        facade.DelData();
////        truckManager.DelData();
////        siteManager.DelData();
////        transportManager.DelData();
////        truckManager.startingProgram();
////        siteManager.startingProgram();
////        transportManager.startingProgram();
        DBHandler.getInstance().deleteRecordsFromTables();
        IntegratedService.getInstance().DelData();
    }

    @Before
    public void setUp() throws ParseException {
//        truckManager.LoadData();
//        siteManager.LoadData();
//        facade.loadData();
        //transportManager.LoadData();
        service.loadData();
    }

    @After
    public void tearDown() throws ParseException {
//        facade.DelData();
//        truckManager.DelData();
//        siteManager.DelData();
//        transportManager.DelData();
//        truckManager.startingProgram();
//        siteManager.startingProgram();
//        transportManager.startingProgram();
        DBHandler.getInstance().deleteRecordsFromTables();
        service.DelData();
    }

//    @Test
//    // trying to create order - fail
//    public void createOrderFail() {
//        service.login(207687849);
//        Map<Integer, Integer> map = new HashMap<>();
//        map.put(3, 6);
//        map.put(1, 2);
//        List<DayOfWeek> list = new LinkedList<>();
//        list.add(DayOfWeek.MONDAY);
//        list.add(DayOfWeek.SATURDAY);
//
//        assertEquals(true, service.orderItems("1 1", 0, list).isErrorOccurred());
//    }

    @Test
    // trying to create order - success
    public void createOrderSucc(){
        service.login(207687849);
        List<DayOfWeek> list = new LinkedList<>();
        list.add(DayOfWeek.MONDAY);
        list.add(DayOfWeek.SATURDAY);
        list.add(DayOfWeek.SUNDAY);

        assertEquals(service.orderItems("1 3", 0, list).isErrorOccurred(), false);
    }

    @Test
    // trying to add Items to Order - success
    public void addItemsToOrderSucc(){
        service.login(207687849);
        assertEquals(service.addItemsToOrder(1, "1 3").isErrorOccurred(), false);

    }

    /// trying to add items to order but the truck can not add the products because there is weight exceed
    @Test
    public void addItemsToOrderFail(){
        service.login(207687849);
        int orderID = 3;
        int count = service.getOrderByOrderID(3).getValue().getItemCount().size();
        service.addProduct("drink", "shlomit",3,15.0,"1","5","8",1000000);
        service.addItemsToOrder(orderID,"5,3");
        assertEquals(count,service.getOrderByOrderID(3).getValue().getItemCount().size());
    }

    @Test
    // checking if only the transport manager can create order
    public void onlyTheTransportManagerCanCreateTransportFail(){
        service.login(207687849);
        LocalDateTime date = LocalDateTime.now();
        try {
            service.AddTransport(date, 555, 1, 3, 5);
            fail("only the transport Manager can add transport");
        }
        catch(Exception e){}

        }

    @Test
    // Checking if the Personal Manager receives message if we can not create order
    public void messageIsNotReadByPM(){
        service.getUnreadMessages("PersonalManager");
        service.login(209876545);
        List<DayOfWeek> list = new LinkedList<>();
        list.add(DayOfWeek.MONDAY);
        list.add(DayOfWeek.SATURDAY);
        service.addProduct("drink", "shlomit",3,15.0,"1","5","8",1000000);
        service.orderItems("5 3", 0, list);
        service.logout();
        assertEquals(1 ,service.getUnreadMessages("PersonalManager").size());

    }
    @Test
    // Checking if the Transport Manager receives message if we can not create order
    public void messageIsNotReadByTM(){
        service.getUnreadMessages("TransportManager");
        service.login(209876545);
        List<DayOfWeek> list = new LinkedList<>();
        list.add(DayOfWeek.MONDAY);
        list.add(DayOfWeek.SATURDAY);
        service.addProduct("drink", "shlomit",3,15.0,"1","5","8",1000000);
        service.orderItems("5 3", 0, list);
        service.logout();
        assertEquals(1 ,service.getUnreadMessages("TransportManager").size());
    }
    @Test
    // Checking if the Transport Manager receives message-> the message is marked as read
    public void messageIsReadByTM(){
        service.login(209876545);
        List<DayOfWeek> list = new LinkedList<>();
        list.add(DayOfWeek.MONDAY);
        list.add(DayOfWeek.SATURDAY);
        service.addProduct("drink", "shlomit",3,15.0,"1","5","8",1000000);
        service.orderItems("5 3", 0, list);
        service.getUnreadMessages("TransportManager");
        assertEquals(0 ,service.getUnreadMessages("TransportManager").size());
    }
    @Test
    // Checking if the Personal Manager receives message -> the message is marked as read
    public void messageIsReadByPM(){
        service.login(209876545);
        List<DayOfWeek> list = new LinkedList<>();
        list.add(DayOfWeek.MONDAY);
        list.add(DayOfWeek.SATURDAY);
        service.addProduct("drink", "shlomit",3,15.0,"1","5","8",1000000);
        service.orderItems("5 3", 0, list);
        service.logout();
        service.login(207687849);
        service.getUnreadMessages("PersonalManager");
        assertEquals(0 ,service.getUnreadMessages("PersonalManager").size());

    }



}
