
import BussinessLayer.Objects.Category;
import BussinessLayer.Objects.Item;
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
public class ProductManagerTest {
    private static IntegratedService service = IntegratedService.getInstance();
    private static int rproID1 = 0;
    private static int rproID2 = 0;
    private static int rproID3 = 0;
    private static Category rcat1= null;
    private static Category rcat2= null;
    private static Category rsubcat1= null;
    private static Category rsubcat2= null;
    private static Category rsubcat3= null;
    private static Category rsubsubcat1= null;
    private static Category rsubsubcat2= null;
    private static Category rsubsubcat3= null;

    @AfterClass
    public static void remove(){
        try{
            DBHandler.getInstance().deleteRecordsFromTables();
            service.DelData();
        }catch(Exception e){

        }
    }
    @BeforeClass
    public static void setup(){
        java.sql.Date date = new java.sql.Date(new java.util.Date().getTime());
        java.sql.Date dt= new java.sql.Date( date.getTime() + 24*60*60*1000);
        rcat1 = CategoryManager.getInstance().addCategory("cat1","main");
        rcat2 = CategoryManager.getInstance().addCategory("cat2","main");
        rsubcat1 = CategoryManager.getInstance().addCategory("subcat1","sub");
        rsubcat2 = CategoryManager.getInstance().addCategory("subcat2","sub");
        rsubcat3 = CategoryManager.getInstance().addCategory("subcat3","sub");
        rsubsubcat1 = CategoryManager.getInstance().addCategory("subSubcat1","subSub");
        rsubsubcat2 = CategoryManager.getInstance().addCategory("subSubcat2","subSub");
        rsubsubcat3 = CategoryManager.getInstance().addCategory("subSubcat3","subSub");
        Product p1 = ProductManager.getInstance().createProduct("first", "mnu", 2,10, rcat1.getID(), rsubcat1.getID(), rsubsubcat1.getID(),1);
        rproID1 = p1.getProductId();
        Product p2 = ProductManager.getInstance().createProduct("Second", "mnu", 4,20, rcat1.getID(), rsubcat2.getID(), rsubsubcat2.getID(),1);
        rproID2 = p2.getProductId();
        Product p3 = ProductManager.getInstance().createProduct("third", "NotMnu", 6,60, rcat2.getID(), rsubcat3.getID(), rsubsubcat3.getID(),1);
        rproID3 = p3.getProductId();
        SupplierManager supplierManager = new SupplierManager();
        OrderManager ordermanager = new OrderManager();



        //supplier 1
        SupplierCard sCard1 = new SupplierCard(1, "AnimalKingdom", "Tel Aviv Shaul st 11", "187444", PaymentMethod.Cash);
        List<DayOfWeek> deliveryDays1 = new ArrayList<>();
        deliveryDays1.add(DayOfWeek.of(2));
        //product1
        NavigableMap<Integer,Double> discountByAmount1 = new TreeMap<>();
        discountByAmount1.put(50,10.0);
        discountByAmount1.put(100,25.0);
        //product2
        NavigableMap<Integer,Double> discountByAmount2 = new TreeMap<>();
        discountByAmount2.put(5,15.0);
        discountByAmount2.put(10,40.0);
        Map<String, SupplierProduct> product1 = new HashMap<>();


        Agreement agr1 = new Agreement("Animal Kingdom", deliveryDays1, SupplyMethod.byDays, product1.keySet().stream().collect(Collectors.toList()));
        Supplier sup = supplierManager.addSupplier(sCard1, agr1);
        try{
            supplierManager.addContactsToSupplier(sup.getSupplierCard().getId(), "yossi", "poterH@gmail.com", "0501234567");
            supplierManager.addProductToAgreement(sup.getSupplierCard().getId(), rproID1, "1-100", 15.0, discountByAmount1);
            supplierManager.addProductToAgreement(sup.getSupplierCard().getId(), rproID2, "1-101", 1500.0, discountByAmount2);
        }catch(Exception e){

        }
        Map<Integer, Integer> map1 = new HashMap<>();
        //date
        LocalDate date3 = LocalDate.of(2022, 12, 21);
        LocalDate date4 = LocalDate.of(2022, 12, 20);
        LocalDate date5 = LocalDate.of(2022, 12, 19);
        LocalDate today= LocalDate.now();
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
        ShiftsManager.getInstance().addShift(date4,ShiftType.Evening,shiftStructure1);
        ShiftsManager.getInstance().addShift(date3,date4,ShiftType.Evening);
        ShiftsManager.getInstance().addShift(date5,date4,ShiftType.Evening);
        ShiftsManager.getInstance().addShift(today,date4,ShiftType.Evening);
        ShiftsManager.getInstance().addShift(tomorrow,date4,ShiftType.Evening);
        int Eid = EmployeeDAO.getmaxID()+1;
        EmployeesManager.getInstance().addEmployee(Eid,"david",998989,10000,"good","Driver","B");
        EmployeesManager.getInstance().getEmployee(Eid).addShift(today,ShiftType.Evening);
        EmployeesManager.getInstance().getEmployee(Eid).addShift(today,ShiftType.Morning);
        EmployeesManager.getInstance().addEmployeetoShift(Eid, date3,ShiftType.Evening);
        EmployeesManager.getInstance().addEmployeetoShift(Eid, date4,ShiftType.Evening);
        Eid = EmployeeDAO.getmaxID()+1;
        EmployeesManager.getInstance().addEmployee(Eid,"yossi",998988,10000,"good","Driver","C");
        EmployeesManager.getInstance().getEmployee(Eid).addShift(today,ShiftType.Evening);
        EmployeesManager.getInstance().getEmployee(Eid).addShift(today,ShiftType.Morning);
        EmployeesManager.getInstance().addEmployeetoShift(Eid, date3,ShiftType.Evening);
        EmployeesManager.getInstance().addEmployeetoShift(Eid, date5,ShiftType.Evening);
        Eid = EmployeeDAO.getmaxID()+1;
        EmployeesManager.getInstance().addEmployee(Eid,"yossi",998988,10000,"good","Driver","C1");
        EmployeesManager.getInstance().getEmployee(Eid).addShift(today,ShiftType.Evening);
        EmployeesManager.getInstance().getEmployee(Eid).addShift(today,ShiftType.Morning);
        EmployeesManager.getInstance().addEmployeetoShift(Eid, date3,ShiftType.Evening);
        EmployeesManager.getInstance().addEmployeetoShift(Eid, date3,ShiftType.Evening);
        SiteManager.getInstance().CreateSite(5,"Shay Kresner", "0543012364", "Tel Aviv Rothchild bolv 14", "Center");
        int id1 = 0;
        Order o1 = null;
        try{
             o1 = ordermanager.createOrder(map1);
             id1= o1.getOrderId();
             ordermanager.placeAnOrder(o1);

        }catch(Exception e){
            for(Order o: OrderDAO.getInstance().getAllOrders()) {
                o1 = o;
                id1 = o1.getOrderId();
            }
        }



        ProductManager.getInstance().addItems(3,rproID1,id1,"a12",6,dt,"Shelf", false);
        ProductManager.getInstance().addItems(4,rproID1,id1,"a12",6,dt,"Storage", false);
        ProductManager.getInstance().addItems(5,rproID2,id1,"a13",6,dt,"Shelf", false);
        ProductManager.getInstance().addItems(4,rproID2,id1,"a13",6,dt,"Storage", false);
        ProductManager.getInstance().addItems(8,rproID3,id1,"a14",18,dt,"Shelf", false);
    }
    @Test
    public void A_createProduct() {
        boolean Check1 = false;
        boolean Check2 = false;
        boolean Check3 = false;
        for(Product p: ProductManager.getInstance().getAllProduct()){
            if(p.getProductId()==rproID1){
                Check1 = true;
            }else if(p.getProductId()==rproID2){
                Check2 = true;
            }else if(p.getProductId()==rproID3){
                Check3 = true;
            }
        }
        boolean Check4 = ProductManager.getInstance().getProduct(rproID1).getCategory().get("main").equals(rcat1.getID());
        boolean Check5 = ProductManager.getInstance().getProduct(rproID1).getCategory().get("sub").equals(rsubcat1.getID());
        boolean Check6 = ProductManager.getInstance().getProduct(rproID1).getCategory().get("subSub").equals(rsubsubcat1.getID());
        boolean Check7 = ProductManager.getInstance().getProduct(rproID2).getCategory().get("main").equals(rcat1.getID());
        boolean Check8 = ProductManager.getInstance().getProduct(rproID2).getCategory().get("sub").equals(rsubcat2.getID());
        boolean Check9 = ProductManager.getInstance().getProduct(rproID2).getCategory().get("subSub").equals(rsubsubcat2.getID());
        boolean Check10 = ProductManager.getInstance().getProduct(rproID3).getCategory().get("main").equals(rcat2.getID());
        boolean Check11 = ProductManager.getInstance().getProduct(rproID3).getCategory().get("sub").equals(rsubcat3.getID());
        boolean Check12 = ProductManager.getInstance().getProduct(rproID3).getCategory().get("subSub").equals(rsubsubcat3.getID());
        assert (Check1&Check2&Check3&Check4&Check5&Check6&Check7&Check8&Check9&Check10&Check11&Check12);

    }


    @Test
    public void C_moveProductToStorage() {
        boolean Check1 = ProductManager.getInstance().getProduct(rproID1).getAmountShelf()==3;
        boolean Check2 = ProductManager.getInstance().getProduct(rproID1).getAmountStorage()==4;
        int storage = 0;
        int shelf = 0;
        for(Item itm: SuperItemDAO.getInstance().getAllSuperItemsOfProduct(rproID1)){
            if(itm.getStorageShelf().equals("Storage")){
                storage++;
            }else if(itm.getStorageShelf().equals("Shelf")){
                shelf++;
            }else{
                assert (false);
            }
        }
        boolean Check3 = shelf==3;
        boolean Check4 = storage==4;
        ProductManager.getInstance().moveProductToStorage(rproID1, 2, "b15");
        boolean Check5 = ProductManager.getInstance().getProduct(rproID1).getAmountShelf()==1;
        boolean Check6 = ProductManager.getInstance().getProduct(rproID1).getAmountStorage()==6;
        int storage1 = 0;
        int shelf1 = 0;
        for(Item itm:SuperItemDAO.getInstance().getAllSuperItemsOfProduct(rproID1)){
            if(itm.getStorageShelf().equals("Storage")){
                storage1++;
            }else if(itm.getStorageShelf().equals("Shelf")){
                shelf1++;
            }else{
                assert (false);
            }
        }
        boolean Check7 = shelf1==1;
        boolean Check8 = storage1==6;
        try{
            ProductManager.getInstance().moveProductToStorage(rproID1, 2, "b15");
        }catch(Exception e){
        }

        boolean Check9 = ProductManager.getInstance().getProduct(rproID1).getAmountShelf()==1;
        boolean Check10 = ProductManager.getInstance().getProduct(rproID1).getAmountStorage()==6;
        int storage2 = 0;
        int shelf2 = 0;
        for(Item itm:SuperItemDAO.getInstance().getAllSuperItemsOfProduct(rproID1)){
            if(itm.getStorageShelf().equals("Storage")){
                storage2++;
            }else if(itm.getStorageShelf().equals("Shelf")){
                shelf2++;
            }else{
                assert (false);
            }
        }
        boolean Check11 = shelf2==1;
        boolean Check12 = storage2==6;
        assert (Check1&Check2&Check3&Check4&Check5&Check6&Check7&Check8&
                Check9&Check10&Check11&Check12);

    }

    @Test
    public void D_moveProductToShelf() {
        boolean Check1 = ProductManager.getInstance().getProduct(rproID2).getAmountShelf()==5;
        boolean Check2 = ProductManager.getInstance().getProduct(rproID2).getAmountStorage()==4;
        int storage = 0;
        int shelf = 0;
        for(Item itm:SuperItemDAO.getInstance().getAllSuperItemsOfProduct(rproID2)){
            if(itm.getStorageShelf().equals("Storage")){
                storage++;
            }else if(itm.getStorageShelf().equals("Shelf")){
                shelf++;
            }else{
                assert (false);
            }
        }
        boolean Check3 = shelf==5;
        boolean Check4 = storage==4;
        ProductManager.getInstance().moveProductToShelf(rproID2, 2, "b15");
        boolean Check5 = ProductManager.getInstance().getProduct(rproID2).getAmountShelf()==7;
        boolean Check6 = ProductManager.getInstance().getProduct(rproID2).getAmountStorage()==2;
        int storage1 = 0;
        int shelf1 = 0;
        for(Item itm:SuperItemDAO.getInstance().getAllSuperItemsOfProduct(rproID2)){
            if(itm.getStorageShelf().equals("Storage")){
                storage1++;
            }else if(itm.getStorageShelf().equals("Shelf")){
                shelf1++;
            }else{
                assert (false);
            }
        }
        boolean Check7 = shelf1==7;
        boolean Check8 = storage1==2;
        boolean Check9 =false;
        try{
            ProductManager.getInstance().moveProductToShelf(rproID2, 3, "b15");
        }catch(Exception e){
            Check9 = true;
        }

        boolean Check10 = ProductManager.getInstance().getProduct(rproID2).getAmountShelf()==7;
        boolean Check11 = ProductManager.getInstance().getProduct(rproID2).getAmountStorage()==2;
        int storage2 = 0;
        int shelf2 = 0;
        for(Item itm:SuperItemDAO.getInstance().getAllSuperItemsOfProduct(rproID2)){
            if(itm.getStorageShelf().equals("Storage")){
                storage2++;
            }else if(itm.getStorageShelf().equals("Shelf")){
                shelf2++;
            }else{
                assert (false);
            }
        }
        boolean Check12 = shelf2==7;
        boolean Check13 = storage2==2;
        ProductManager.getInstance().moveProductToShelf(rproID2, 2, "b15");
        assert (Check1&Check2&Check3&Check4&Check5&Check6&Check7&Check8&
                Check9&Check10&Check11&Check12&Check13);
    }
}
