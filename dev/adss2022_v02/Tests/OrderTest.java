import BussinessLayer.Objects.Category;
import BussinessLayer.Objects.Product;
import BussinessLayer.Managers.*;
import BussinessLayer.Objects.*;
import BussinessLayer.Managers.EmployeesManager;
import BussinessLayer.Managers.SiteManager;
import BussinessLayer.Managers.TruckManager;
import DataAccessLayer.Dal.*;
import DataAccessLayer.EmployeeDAO;
import ServiceLayer.ModulesServices.IntegratedService;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrderTest {
    private static IntegratedService service = IntegratedService.getInstance();
    private static int proID1 = 0;
    private static int proID2 = 0;
    private static int proID3 = 0;
    private static Category cat1 = null;
    private static Category cat2 = null;
    private static Category subcat1 = null;
    private static Category subcat2 = null;
    private static Category subcat3 = null;
    private static Category subsubcat1 = null;
    private static Category subsubcat2 = null;
    private static Category subsubcat3 = null;
    private static Order order ;
    private static OrderManager ordermanager ;
    private static Supplier sup ;
    private static SupplierManager supplierManager;
    public Facade facade;
    public TruckManager truckManager;
    public TransportManager transportManager;
    public SiteManager siteManager;
    DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm");
    DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDateTime date1;
    LocalDateTime date2;
    LocalDateTime date3;
    LocalDate date4;
    LocalDate date5;
    LocalDate date6;

    @After
    public void remove() {
        try {
            DBHandler.getInstance().deleteRecordsFromTables();
            service.DelData();
        } catch (Exception e) {

        }
    }

    @Before
    public void setup() {
        try {
            facade = Facade.getInstance();
            truckManager = TruckManager.getInstance();
            siteManager = SiteManager.getInstance();
            transportManager = TransportManager.getInstance();
            ordermanager = new OrderManager();
            supplierManager = new SupplierManager();
            date1 = LocalDateTime.parse("17/06/2022-16:00", formatters);
            date2 = LocalDateTime.parse("01/06/2022-10:00", formatters);
            date3 = LocalDateTime.parse("25/06/2022-15:00", formatters);
            date4 = LocalDate.parse("17/06/2022", formatter2);
            date5 = LocalDate.parse("01/01/2022", formatter2);
            date6 = LocalDate.parse("01/01/2023", formatter2);
            facade.DelData();
            truckManager.DelData();
            siteManager.DelData();
            transportManager.DelData();
            truckManager.startingProgram();
            siteManager.startingProgram();
            transportManager.startingProgram();
            DBHandler.getInstance().deleteRecordsFromTables();
            TruckManager.getInstance().DelData();
            SiteManager.getInstance().DelData();
            TransportManager.getInstance().DelData();
            ShiftsManager.getInstance().DelData();
            EmployeesManager.getInstance().DelData();
        } catch (Exception e) {

        }
        java.sql.Date date = new java.sql.Date(new java.util.Date().getTime());
        java.sql.Date dt = new java.sql.Date(date.getTime() + 24 * 60 * 60 * 1000);
        cat1 = CategoryManager.getInstance().addCategory("cat1", "main");
        cat2 = CategoryManager.getInstance().addCategory("cat2", "main");
        subcat1 = CategoryManager.getInstance().addCategory("subcat1", "sub");
        subcat2 = CategoryManager.getInstance().addCategory("subcat2", "sub");
        subcat3 = CategoryManager.getInstance().addCategory("subcat3", "sub");
        subsubcat1 = CategoryManager.getInstance().addCategory("subSubcat1", "subSub");
        subsubcat2 = CategoryManager.getInstance().addCategory("subSubcat2", "subSub");
        subsubcat3 = CategoryManager.getInstance().addCategory("subSubcat3", "subSub");
        Product p1 = ProductManager.getInstance().createProduct("first", "mnu", 2, 10, cat1.getID(), subcat1.getID(), subsubcat1.getID(), 1);
        proID1 = p1.getProductId();
        Product p2 = ProductManager.getInstance().createProduct("Second", "mnu", 4, 20, cat1.getID(), subcat2.getID(), subsubcat2.getID(), 1);
        proID2 = p2.getProductId();
        Product p3 = ProductManager.getInstance().createProduct("third", "NotMnu", 6, 60, cat2.getID(), subcat3.getID(), subsubcat3.getID(), 1);
        proID3 = p3.getProductId();
        supplierManager = new SupplierManager();


        //supplier 1
        SupplierCard sCard1 = new SupplierCard(1, "AnimalKingdom", "Tel Aviv Shaul st 11", "187444", PaymentMethod.Cash);
        List<DayOfWeek> deliveryDays1 = new ArrayList<>();
        deliveryDays1.add(DayOfWeek.of(2));
        //product1
        NavigableMap<Integer, Double> discountByAmount1 = new TreeMap<>();
        discountByAmount1.put(50, 10.0);
        discountByAmount1.put(100, 25.0);
        //product2
        NavigableMap<Integer, Double> discountByAmount2 = new TreeMap<>();
        discountByAmount2.put(5, 15.0);
        discountByAmount2.put(10, 40.0);
        Map<String, SupplierProduct> product1 = new HashMap<>();


        Agreement agr1 = new Agreement("Animal Kingdom", deliveryDays1, SupplyMethod.byDays, product1.keySet().stream().collect(Collectors.toList()));
        sup = supplierManager.addSupplier(sCard1, agr1);
        try{
            supplierManager.addContactsToSupplier(sup.getSupplierCard().getId(), "yossi", "poterH@gmail.com", "0501234567");
            supplierManager.addProductToAgreement(sup.getSupplierCard().getId(), proID1, "1-100", 15.0, discountByAmount1);
            supplierManager.addProductToAgreement(sup.getSupplierCard().getId(), proID2, "1-101", 1500.0, discountByAmount2);
        }catch(Exception e){

        }
        Map<Integer, Integer> map1 = new HashMap<>();
        //date
        LocalDate date3 = LocalDate.of(2022, 12, 21);
        LocalDate date4 = LocalDate.of(2022, 12, 20);
        LocalDate date5 = LocalDate.of(2022, 12, 19);
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);


        //put jobType
        Map<JobType, Integer> shiftStructure1 = new HashMap<>();
        shiftStructure1.put(JobType.Usher, 1);
        shiftStructure1.put(JobType.Cashier, 1);
        shiftStructure1.put(JobType.Storekeeper, 1);
        shiftStructure1.put(JobType.Driver, 3);
        map1.put(1, 60);
        map1.put(2, 6);
        TruckManager.getInstance().CreateTruck("998773250", "Toyota", 2, 1, "B");
        TruckManager.getInstance().CreateTruck("4477221851", "Hyundai", 4, 3, "C1");
        TruckManager.getInstance().CreateTruck("8282111351", "Mercedes-benz", 2.5, 1.72, "C");
        TruckManager.getInstance().CreateTruck("55772314551", "Tesla", 6, 3.2, "C+E");
        ShiftsManager.getInstance().addShift(date4, ShiftType.Evening, shiftStructure1);
        ShiftsManager.getInstance().addShift(date3, date4, ShiftType.Evening);
        ShiftsManager.getInstance().addShift(date5, date4, ShiftType.Evening);
        ShiftsManager.getInstance().addShift(today, date4, ShiftType.Evening);
        ShiftsManager.getInstance().addShift(tomorrow, date4, ShiftType.Evening);
        int Eid = EmployeeDAO.getmaxID() + 1;
        EmployeesManager.getInstance().addEmployee(Eid, "david", 998989, 10000, "good", "Driver", "C+E");
        EmployeesManager.getInstance().getEmployee(Eid).addShift(today, ShiftType.Evening);
        EmployeesManager.getInstance().getEmployee(Eid).addShift(today, ShiftType.Morning);
        EmployeesManager.getInstance().addEmployeetoShift(Eid, date3, ShiftType.Evening);
        EmployeesManager.getInstance().addEmployeetoShift(Eid, date4, ShiftType.Evening);
        Eid = EmployeeDAO.getmaxID() + 1;
        EmployeesManager.getInstance().addEmployee(Eid, "yossi", 998988, 10000, "good", "Driver", "C");
        EmployeesManager.getInstance().getEmployee(Eid).addShift(today, ShiftType.Evening);
        EmployeesManager.getInstance().getEmployee(Eid).addShift(today, ShiftType.Morning);
        EmployeesManager.getInstance().addEmployeetoShift(Eid, date3, ShiftType.Evening);
        EmployeesManager.getInstance().addEmployeetoShift(Eid, date5, ShiftType.Evening);
        Eid = EmployeeDAO.getmaxID() + 1;
        EmployeesManager.getInstance().addEmployee(Eid, "yossi", 998988, 10000, "good", "Driver", "C1");
        EmployeesManager.getInstance().getEmployee(Eid).addShift(today, ShiftType.Evening);
        EmployeesManager.getInstance().getEmployee(Eid).addShift(today, ShiftType.Morning);
        EmployeesManager.getInstance().addEmployeetoShift(Eid, date3, ShiftType.Evening);
        EmployeesManager.getInstance().addEmployeetoShift(Eid, date3, ShiftType.Evening);
        SiteManager.getInstance().CreateSite(5, "Shay Kresner", "0543012364", "Tel Aviv Rothchild bolv 14", "Center");
        int id1 = 0;
        try {
            order = ordermanager.createOrder(map1);
            id1 = order.getOrderId();

        } catch (Exception e) {
            for (Order o : OrderDAO.getInstance().getAllOrders()) {
                order = o;
                id1 = order.getOrderId();
            }
        }


        ProductManager.getInstance().addItems(3, proID1, id1, "a12", 6, dt, "Shelf", false);
        ProductManager.getInstance().addItems(4, proID1, id1, "a12", 6, dt, "Storage", false);
        ProductManager.getInstance().addItems(5, proID2, id1, "a13", 6, dt, "Shelf", false);
        ProductManager.getInstance().addItems(4, proID2, id1, "a13", 6, dt, "Storage", false);
        ProductManager.getInstance().addItems(8, proID3, id1, "a14", 18, dt, "Shelf", false);
    }

    @Test
    public void A_CheckOrderTest() {
        boolean test1 = (OrderDAO.getInstance().getAllOrders().size() == 1);
        boolean test2 = (order.getOrderId()==1);
        DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
        Date today = Calendar.getInstance().getTime();
        boolean test3 = (df.format(order.getOrderDate()).equals(df.format(today)));
        System.out.println(df.format(order.getOrderDate()));
        System.out.println((df.format(today)));
        boolean test4 = (order.getItemCount().size()==2);
        boolean test5 = (order.getTotalPriceBeforeDiscount()==9900);
        assert (test1&test2&test3&test4&test5);
    }

    @Test
    public void B_SecondOrderTest() {
        NavigableMap<Integer, Double> discountByAmount2 = new TreeMap<>();
        discountByAmount2.put(1, 1.0);
        discountByAmount2.put(2, 2.0);
        supplierManager.removeProductFromAgreement(sup.getSupplierCard().getId(), proID2);
        supplierManager.addProductToAgreement(sup.getSupplierCard().getId(), proID2, "1-101", 1500.0, discountByAmount2);
        SiteManager.getInstance().CreateSite(1 , "samuell", "055555555", "TA", "Center");
        SiteManager.getInstance().CreateSite(0 , "siri", "055555555", "TA", "Center");
        Map<Integer, Integer> orMap = new HashMap<>();
        orMap.putIfAbsent(proID1,1);
        orMap.putIfAbsent(proID2,1);
        Map<String, Integer> shiftStructureD1 = new HashMap<>();
        shiftStructureD1.put("Driver", 1);
        EmployeesManager.getInstance().addEmployee(207687849, "Noga Schwartz", 123458, 9999, "normal hiring condition", "PersonalManager", "");
        facade.Login(207687849);
        facade.addShift(LocalDate.now(), "Morning", shiftStructureD1);
        Map<String, LinkedList<Integer>> employeesID = new HashMap<>();
        LinkedList<Integer> l1 = new LinkedList<>();
        l1.add(1);
        employeesID.put("Driver", l1);
        facade.addTrain(207687849, "CancellationCard");
        facade.addTrain(207687849, "TeamManagement");
        facade.addEmployee(0,"yoss",1234567,888,"normal hiring condition", "PersonalManager", "");
        facade.switchManager(0,207687849,LocalDate.now(),"Evening");
        facade.addEmployee(1,LocalDate.now(),"Evening");
        //facade.switchManager(0,207687849,LocalDate.now(),"Morning");
        facade.shiftArrange(LocalDate.now(),"Morning",207687849,employeesID);
        order = ordermanager.createOrder(orMap);
        boolean test1 = (OrderDAO.getInstance().getAllOrders().size() == 2);
        boolean test2 = (order.getOrderId()==2);
        DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
        Date today = Calendar.getInstance().getTime();
        boolean test3 = (df.format(order.getOrderDate()).equals(df.format(today)));
        boolean test4 = (order.getItemCount().size()==2);
        boolean test5 = (order.getPeriodicOrder()==false);
        assert (test1&test2&test3&test4&test5);

    }
    /*@Test
    public void C_TransportOrderTest() {
        System.out.println("Before "+order.getTotalPriceBeforeDiscount());
        System.out.println("After "+order.getTotalPriceAfterDiscount());
        boolean check1 = order.getTotalPriceBeforeDiscount()==9900;
    }*/

}