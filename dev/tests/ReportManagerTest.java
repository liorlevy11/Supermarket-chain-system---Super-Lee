import BusinessLayer.InventoryBusinessLayer.*;
import BusinessLayer.Managers.*;
import BusinessLayer.SuppliersBusiness.*;
import Dal.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ReportManagerTest {
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
//
@AfterAll
public static void remove(){
    try{
        SupplierProductDAO.getInstance().deleteTable();
    }catch(Exception e){

    }
    try{
        OrderDAO.getInstance().deleteTable();
    }catch(Exception e){

    }
    try{
        SuperItemDAO.getInstance().deleteTable();
    }catch(Exception e){

    }
    try{
        ProductDAO.getInstance().deleteTable();
    }catch(Exception e){

    }
    try{
        CategoryDAO.getInstance().deleteTable();
    }catch(Exception e){

    }
    try{
        SaleDAO.getInstance().deleteTable();
    }catch(Exception e){

    }
    try{
        ContactDAO.getInstance().deleteTable();
    }catch(Exception e){

    }
    try{
        ItemOrderDAO.getInstance().deleteTable();
    }catch(Exception e){

    }
    try{
        SupplierDAO.getInstance().deleteTable();
    }catch(Exception e){

    }
}
    @BeforeAll
    public static void setup(){
        try{
            DBHandler.getInstance().init();
        }catch(Exception e){}
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
        Product p1 = ProductManager.getInstance().createProduct("first", "mnu", 2,10, rcat1.getID(), rsubcat1.getID(), rsubsubcat1.getID());
        rproID1 = p1.getProductId();
        Product p2 = ProductManager.getInstance().createProduct("Second", "mnu", 4,20, rcat1.getID(), rsubcat2.getID(), rsubsubcat2.getID());
        rproID2 = p2.getProductId();
        Product p3 = ProductManager.getInstance().createProduct("third", "NotMnu", 6,60, rcat2.getID(), rsubcat3.getID(), rsubsubcat3.getID());
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
        supplierManager.addContactsToSupplier(sup.getSupplierCard().getId(),"Harry", "poterH@gmail.com", "0501234567");
        supplierManager.addProductToAgreement(sup.getSupplierCard().getId(),rproID1,"1-100",15.0,discountByAmount1);
        supplierManager.addProductToAgreement(sup.getSupplierCard().getId(),rproID2,"1-101",1500.0,discountByAmount2);
        Map<Integer, Integer> map1 = new HashMap<>();
        map1.put(1, 60);
        map1.put(2, 6);
        Order o1 = ordermanager.createOrder(map1);
        int id1 = o1.getOrderId();
        ordermanager.placeAnOrder(o1);


        ProductManager.getInstance().addItems(3,rproID1,id1,"a12",6,dt,"Shelf", false);
        ProductManager.getInstance().addItems(4,rproID1,id1,"a12",6,dt,"Storage", false);
        ProductManager.getInstance().addItems(5,rproID2,id1,"a13",6,dt,"Shelf", false);
        ProductManager.getInstance().addItems(4,rproID2,id1,"a13",6,dt,"Storage", false);
        ProductManager.getInstance().addItems(8,rproID3,id1,"a14",18,dt,"Shelf", false);
    }
    @org.junit.jupiter.api.Test
    void addInventoryReport() {
        InventoryReport inventory1 = ReportsManager.getInstance().addInventoryReport(String.valueOf(rcat1.getID())+","+String.valueOf(rcat2.getID()));
        boolean Check1 = inventory1.ContainProduct(rproID1);
        boolean Check2 = inventory1.ContainProduct(rproID2);
        boolean Check3 = inventory1.ContainProduct(rproID3);
        boolean Check4 = inventory1.getQuantityOfProduct(rproID1)==7;
        boolean Check5 = inventory1.getQuantityOfProduct(rproID2)==9;
        boolean Check6 = inventory1.getQuantityOfProduct(rproID3)==8;
        InventoryReport inventory2 = ReportsManager.getInstance().addInventoryReport(String.valueOf(rcat1.getID()));
        boolean Check7 = inventory2.ContainProduct(rproID1);
        boolean Check8 = inventory2.ContainProduct(rproID2);
        boolean Check9 = !inventory2.ContainProduct(rproID3);
        InventoryReport inventory3 = ReportsManager.getInstance().addInventoryReport("");
        boolean Check10 = !inventory3.ContainProduct(rproID1);
        boolean Check11 = !inventory3.ContainProduct(rproID2);
        boolean Check12 = !inventory3.ContainProduct(rproID3);
        assert (Check1&Check2&Check3&Check4&Check5&Check6&Check7&Check8&Check9&Check10&Check11&Check12);
    }
    @org.junit.jupiter.api.Test
    void addDefectiveReport() {
        java.sql.Date today = new java.sql.Date( new java.util.Date().getTime() );
        java.sql.Date tommorow= new java.sql.Date( today.getTime() + 24*60*60*1000);
        java.sql.Date dt1= new java.sql.Date( today.getTime() - 24*60*60*1000);
        ProductManager.getInstance().addItems(2,rproID3,2,"a15",18,dt1,"Shelf", false);
        ProductManager.getInstance().addItems(1,rproID3,2,"a15",18,tommorow,"Shelf", true);
        InventoryReport inventory1 = ReportsManager.getInstance().addInventoryReport(String.valueOf(rcat2.getID()));
        boolean Check1 = inventory1.getQuantityOfProduct(rproID3)==11;
        DefectiveReport defects = ReportsManager.getInstance().addDefectiveReport();
        boolean Check2 = !defects.ContainItem(rproID1);
        boolean Check3 = !defects.ContainItem(rproID2);
        boolean Check4 = defects.ContainItem(rproID3);
        boolean Check5 = defects.getItemQuantity(rproID3)==3;
        InventoryReport inventory2 = ReportsManager.getInstance().addInventoryReport(String.valueOf(rcat1.getID())+","+String.valueOf(rcat2.getID()));
        boolean Check6 = inventory2.getQuantityOfProduct(rproID1)==7;
        boolean Check7 = inventory2.getQuantityOfProduct(rproID2)==9;
        boolean Check8 = inventory2.getQuantityOfProduct(rproID3)==8;
        assert (Check1&Check2&Check3&Check4&Check5&Check6&Check7&Check8);
    }
    @org.junit.jupiter.api.Test
    void addItemToOrderReport() {
        java.sql.Date today = new java.sql.Date( new java.util.Date().getTime() );
        java.sql.Date tommorow= new java.sql.Date( today.getTime() + 24*60*60*1000);
        ItemsToOrderReport orderReport1 =  ReportsManager.getInstance().addItemToOrderReport(String.valueOf(rproID1)+" 1,"+String.valueOf(rproID2)+" 2");
        boolean Check1 = orderReport1.ContainProduct(rproID1);
        boolean Check2 = orderReport1.ProductQuantity(rproID1)==1;
        boolean Check3 = orderReport1.ContainProduct(rproID2);
        boolean Check4 = orderReport1.ProductQuantity(rproID2)==2;
        boolean Check5 = !orderReport1.ContainProduct(rproID3);
        Product p4 = ProductManager.getInstance().createProduct("forth", "NotMnu", 11,60, rcat2.getID(), rsubcat3.getID(), rsubsubcat3.getID());
        ProductManager.getInstance().addItems(8,p4.getProductId(),2,"a14",18,tommorow,"Shelf", false);
        ItemsToOrderReport orderReport2 =  ReportsManager.getInstance().addItemToOrderReport(String.valueOf(rproID2)+" 2");
        boolean Check6 = !orderReport2.ContainProduct(rproID1);
        boolean Check7 = orderReport2.ContainProduct(rproID2);
        boolean Check8 = orderReport2.ContainProduct(p4.getProductId());
        boolean Check9 = orderReport2.ProductQuantity(rproID2)==2;
        boolean Check10 = orderReport2.ProductQuantity(p4.getProductId())==3;
        ItemsToOrderReport orderReport3 =  ReportsManager.getInstance().addItemToOrderReport("");
        boolean Check11 = !orderReport3.ContainProduct(rproID1);
        boolean Check12 = !orderReport3.ContainProduct(rproID2);
        assert(Check1&Check2&Check3&Check4&Check5&Check6&Check7&Check8&Check9&Check10&Check11&Check12);
    }
}
