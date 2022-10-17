
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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ReportTests {
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

    @AfterClass
    public static void remove() {
        try {
            DBHandler.getInstance().deleteRecordsFromTables();
            service.DelData();
        } catch (Exception e) {

        }
    }

    @BeforeClass
    public static void setup() {
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
        SupplierManager supplierManager = new SupplierManager();
        OrderManager ordermanager = new OrderManager();


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
        Supplier sup = supplierManager.addSupplier(sCard1, agr1);
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
        EmployeesManager.getInstance().addEmployee(Eid, "david", 998989, 10000, "good", "Driver", "B");
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
        Order o1 = null;
        try {
            o1 = ordermanager.createOrder(map1);
            id1 = o1.getOrderId();
            ordermanager.placeAnOrder(o1);

        } catch (Exception e) {
            for (Order o : OrderDAO.getInstance().getAllOrders()) {
                o1 = o;
                id1 = o1.getOrderId();
            }
        }


        ProductManager.getInstance().addItems(3, proID1, id1, "a12", 6, dt, "Shelf", false);
        ProductManager.getInstance().addItems(4, proID1, id1, "a12", 6, dt, "Storage", false);
        ProductManager.getInstance().addItems(5, proID2, id1, "a13", 6, dt, "Shelf", false);
        ProductManager.getInstance().addItems(4, proID2, id1, "a13", 6, dt, "Storage", false);
        ProductManager.getInstance().addItems(8, proID3, id1, "a14", 18, dt, "Shelf", false);
    }
    @Test
    public void addInventoryReport() {
        InventoryReport inventory1 = ReportsManager.getInstance().addInventoryReport(String.valueOf(cat1.getID())+","+String.valueOf(cat2.getID()));
        boolean Check1 = inventory1.ContainProduct(proID1);
        boolean Check2 = inventory1.ContainProduct(proID2);
        boolean Check3 = inventory1.ContainProduct(proID3);
        boolean Check4 = inventory1.getQuantityOfProduct(proID1)==7;
        boolean Check5 = inventory1.getQuantityOfProduct(proID2)==9;
        boolean Check6 = inventory1.getQuantityOfProduct(proID3)==8;
        InventoryReport inventory2 = ReportsManager.getInstance().addInventoryReport(String.valueOf(cat1.getID()));
        boolean Check7 = inventory2.ContainProduct(proID1);
        boolean Check8 = inventory2.ContainProduct(proID2);
        boolean Check9 = !inventory2.ContainProduct(proID3);
        InventoryReport inventory3 = ReportsManager.getInstance().addInventoryReport("");
        boolean Check10 = !inventory3.ContainProduct(proID1);
        boolean Check11 = !inventory3.ContainProduct(proID2);
        boolean Check12 = !inventory3.ContainProduct(proID3);
        assert (Check1&Check2&Check3&Check4&Check5&Check6&Check7&Check8&Check9&Check10&Check11&Check12);
    }
    @Test
    public void addDefectiveReport() {
        java.sql.Date today = new java.sql.Date( new java.util.Date().getTime() );
        java.sql.Date tommorow= new java.sql.Date( today.getTime() + 24*60*60*1000);
        java.sql.Date dt1= new java.sql.Date( today.getTime() - 24*60*60*1000);
        ProductManager.getInstance().addItems(2,proID3,2,"a15",18,dt1,"Shelf", false);
        ProductManager.getInstance().addItems(1,proID3,2,"a15",18,tommorow,"Shelf", true);
        InventoryReport inventory1 = ReportsManager.getInstance().addInventoryReport(String.valueOf(cat2.getID()));
        boolean Check1 = inventory1.getQuantityOfProduct(proID3)==11;
        DefectiveReport defects = ReportsManager.getInstance().addDefectiveReport();
        boolean Check2 = !defects.ContainItem(proID1);
        boolean Check3 = !defects.ContainItem(proID2);
        boolean Check4 = defects.ContainItem(proID3);
        boolean Check5 = defects.getItemQuantity(proID3)==3;
        InventoryReport inventory2 = ReportsManager.getInstance().addInventoryReport(String.valueOf(cat1.getID())+","+String.valueOf(cat2.getID()));
        boolean Check6 = inventory2.getQuantityOfProduct(proID1)==7;
        boolean Check7 = inventory2.getQuantityOfProduct(proID2)==9;
        boolean Check8 = inventory2.getQuantityOfProduct(proID3)==8;
        assert (Check1&Check2&Check3&Check4&Check5&Check6&Check7&Check8);
    }

    @Test
    public void addItemToOrderReport() {
        java.sql.Date today = new java.sql.Date( new java.util.Date().getTime() );
        java.sql.Date tommorow= new java.sql.Date( today.getTime() + 24*60*60*1000);
        ItemsToOrderReport orderReport1 =  ReportsManager.getInstance().addItemToOrderReport(String.valueOf(proID1)+" 1,"+String.valueOf(proID2)+" 2");
        boolean Check1 = orderReport1.ContainProduct(proID1);
        boolean Check2 = orderReport1.ProductQuantity(proID1)==1;
        boolean Check3 = orderReport1.ContainProduct(proID2);
        boolean Check4 = orderReport1.ProductQuantity(proID2)==2;
        boolean Check5 = !orderReport1.ContainProduct(proID3);
        Product p4 = ProductManager.getInstance().createProduct("forth", "NotMnu", 11,60, cat2.getID(), subcat3.getID(), subsubcat3.getID(),2);
        ProductManager.getInstance().addItems(8,p4.getProductId(),2,"a14",18,tommorow,"Shelf", false);
        ItemsToOrderReport orderReport2 =  ReportsManager.getInstance().addItemToOrderReport(String.valueOf(proID2)+" 2");
        boolean Check6 = !orderReport2.ContainProduct(proID1);
        boolean Check7 = orderReport2.ContainProduct(proID2);
        boolean Check8 = orderReport2.ContainProduct(p4.getProductId());
        boolean Check9 = orderReport2.ProductQuantity(proID2)==2;
        boolean Check10 = orderReport2.ProductQuantity(p4.getProductId())==3;
        ItemsToOrderReport orderReport3 =  ReportsManager.getInstance().addItemToOrderReport("");
        boolean Check11 = !orderReport3.ContainProduct(proID1);
        boolean Check12 = !orderReport3.ContainProduct(proID2);
        assert(Check1&Check2&Check3&Check4&Check5&Check6&Check7&Check8&Check9&Check10&Check11&Check12);
    }


}