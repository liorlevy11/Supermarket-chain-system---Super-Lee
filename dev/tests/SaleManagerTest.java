import BusinessLayer.InventoryBusinessLayer.Category;
import BusinessLayer.InventoryBusinessLayer.Product;
import BusinessLayer.InventoryBusinessLayer.Sale;
import BusinessLayer.Managers.*;
import BusinessLayer.SuppliersBusiness.*;
import Dal.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class SaleManagerTest {
    private static int sproID1 = 0;
    private static int sproID2 = 0;
    private static int sproID3 = 0;
    private static Category scat1= null;
    private static Category scat2= null;
    private static Category ssubcat1= null;
    private static Category ssubcat2= null;
    private static Category ssubcat3= null;
    private static Category ssubsubcat1= null;
    private static Category ssubsubcat2= null;
    private static Category ssubsubcat3= null;



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
        scat1 = CategoryManager.getInstance().addCategory("cat1","main");
        scat2 = CategoryManager.getInstance().addCategory("cat2","main");
        ssubcat1 = CategoryManager.getInstance().addCategory("subcat1","sub");
        ssubcat2 = CategoryManager.getInstance().addCategory("subcat2","sub");
        ssubcat3 = CategoryManager.getInstance().addCategory("subcat3","sub");
        ssubsubcat1 = CategoryManager.getInstance().addCategory("subSubcat1","subSub");
        ssubsubcat2 = CategoryManager.getInstance().addCategory("subSubcat2","subSub");
        ssubsubcat3 = CategoryManager.getInstance().addCategory("subSubcat3","subSub");
        Product p1 = ProductManager.getInstance().createProduct("first", "mnu", 2,10, scat1.getID(), ssubcat1.getID(), ssubsubcat1.getID());
        sproID1 = p1.getProductId();
        Product p2 = ProductManager.getInstance().createProduct("Second", "mnu", 4,20, scat1.getID(), ssubcat2.getID(), ssubsubcat2.getID());
        sproID2 = p2.getProductId();
        Product p3 = ProductManager.getInstance().createProduct("third", "NotMnu", 6,60, scat2.getID(), ssubcat3.getID(), ssubsubcat3.getID());
        sproID3 = p3.getProductId();
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
        supplierManager.addProductToAgreement(sup.getSupplierCard().getId(),sproID1,"1-100",15.0,discountByAmount1);
        supplierManager.addProductToAgreement(sup.getSupplierCard().getId(),sproID2,"1-101",1500.0,discountByAmount2);
        Map<Integer, Integer> map1 = new HashMap<>();
        map1.put(1, 60);
        map1.put(2, 6);
        Order o1 = ordermanager.createOrder(map1);
        int id1 = o1.getOrderId();
        ordermanager.placeAnOrder(o1);


        ProductManager.getInstance().addItems(3,sproID1,id1,"a12",6,dt,"Shelf", false);
        ProductManager.getInstance().addItems(4,sproID1,id1,"a12",6,dt,"Storage", false);
        ProductManager.getInstance().addItems(5,sproID2,id1,"a13",6,dt,"Shelf", false);
        ProductManager.getInstance().addItems(4,sproID2,id1,"a13",6,dt,"Storage", false);
        ProductManager.getInstance().addItems(8,sproID3,id1,"a14",18,dt,"Shelf", false);
    }

    @org.junit.jupiter.api.Test
    void AddSale() {
        SaleManager.getInstance().AddSale("2022-01-20", "2022-12-20", String.valueOf(scat1.getID()),  String.valueOf(sproID3), 10.1);
        boolean Check1 = (SaleManager.getInstance().SaleOnCategoryAtDate(scat1.getID(), "2022-04-21")!=-1);
        boolean Check2 = (SaleManager.getInstance().SaleOnProductAtDate(sproID1, "2022-04-21")!=-1);
        boolean Check3 = (SaleManager.getInstance().SaleOnProductAtDate(sproID3, "2022-04-21")!=-1);
        SaleManager.getInstance().RemoveSale(SaleManager.getInstance().SaleOnCategoryAtDate(scat1.getID(), "2022-04-21"));
        SaleManager.getInstance().AddSale("2022-01-20", "2022-12-20", String.valueOf(scat1.getID()), "", 10.1);
        boolean Check4 = (SaleManager.getInstance().SaleOnCategoryAtDate(scat1.getID(), "2022-04-21")!=-1);
        boolean Check5 = (SaleManager.getInstance().SaleOnProductAtDate(sproID1, "2022-04-21")!=-1);
        SaleManager.getInstance().RemoveSale(SaleManager.getInstance().SaleOnCategoryAtDate(scat1.getID(), "2022-04-21"));
        SaleManager.getInstance().AddSale("2022-01-20", "2022-12-20", "", String.valueOf(sproID3)+","+String.valueOf(sproID2), 10.1);
        boolean Check6 = (SaleManager.getInstance().SaleOnProductAtDate(sproID2, "2022-04-21")!=-1);
        boolean Check7 = (SaleManager.getInstance().SaleOnProductAtDate(sproID3, "2022-04-21")!=-1);
        SaleManager.getInstance().RemoveSale(SaleManager.getInstance().SaleOnProductAtDate(sproID3, "2022-04-21"));
        assert(Check1&Check2&Check3&Check4&Check5&Check6&Check7);
    }

    @org.junit.jupiter.api.Test
    void getSale() {
        SaleManager.getInstance().AddSale("2022-01-20", "2022-12-20", String.valueOf(scat1.getID()), String.valueOf(sproID3), 10.1);
        boolean Check1 = (SaleManager.getInstance().SaleOnCategoryAtDate(scat1.getID(), "2022-04-21")!=-1);
        boolean Check2 = (SaleManager.getInstance().SaleOnProductAtDate(sproID1, "2022-04-21")!=-1);
        boolean Check3 = (SaleManager.getInstance().SaleOnProductAtDate(sproID3, "2022-04-21")!=-1);
        boolean Check9 = ((SaleManager.getInstance().SaleOnCategoryAtDate(scat1.getID(), "2022-04-21")==SaleManager.getInstance().SaleOnProductAtDate(sproID1, "2022-04-21"))&&
                (SaleManager.getInstance().SaleOnCategoryAtDate(scat1.getID(), "2022-04-21")==SaleManager.getInstance().SaleOnProductAtDate(sproID3, "2022-04-21")));
        SaleManager.getInstance().RemoveSale(SaleManager.getInstance().SaleOnCategoryAtDate(scat1.getID(), "2022-04-21"));
        SaleManager.getInstance().AddSale("2022-01-20", "2022-12-20", String.valueOf(scat1.getID()), "", 10.1);
        boolean Check4 = (SaleManager.getInstance().SaleOnCategoryAtDate(scat1.getID(), "2022-04-21")!=-1);
        boolean Check5 = (SaleManager.getInstance().SaleOnProductAtDate(sproID1, "2022-04-21")!=-1);
        boolean Check10 = (SaleManager.getInstance().SaleOnCategoryAtDate(scat1.getID(), "2022-04-21")==(SaleManager.getInstance().SaleOnProductAtDate(sproID1, "2022-04-21")));
        SaleManager.getInstance().RemoveSale(SaleManager.getInstance().SaleOnCategoryAtDate(scat1.getID(), "2022-04-21"));
        SaleManager.getInstance().AddSale("2022-01-20", "2022-12-20", "", String.valueOf(sproID3)+","+String.valueOf(sproID2), 10.1);
        boolean Check6 = (SaleManager.getInstance().SaleOnProductAtDate(sproID2, "2022-04-21")!=-1);
        boolean Check7 = (SaleManager.getInstance().SaleOnProductAtDate(sproID3, "2022-04-21")!=-1);
        boolean Check8 =(SaleManager.getInstance().SaleOnProductAtDate(sproID2, "2022-04-21")==SaleManager.getInstance().SaleOnProductAtDate(sproID3, "2022-04-21"));
        SaleManager.getInstance().RemoveSale(SaleManager.getInstance().SaleOnProductAtDate(sproID3, "2022-04-21"));
        assert(Check1&Check2&Check3&Check4&Check5&Check6&Check7&Check8&Check9&Check10);
    }

    @org.junit.jupiter.api.Test
    void SaleOnProductAtDate() {
        SaleManager.getInstance().AddSale("2022-01-20", "2022-12-20", String.valueOf(scat1.getID()), String.valueOf(sproID3), 10.1);
        boolean Check1 = (SaleManager.getInstance().SaleOnProductAtDate(sproID1, "2022-04-21")!=-1);
        boolean Check2 = (SaleManager.getInstance().SaleOnProductAtDate(sproID3, "2022-04-21")!=-1);
        SaleManager.getInstance().RemoveSale(SaleManager.getInstance().SaleOnCategoryAtDate(scat1.getID(), "2022-04-21"));
        SaleManager.getInstance().AddSale("2022-05-20", "2022-12-20", String.valueOf(scat1.getID()), String.valueOf(sproID3), 10.1);
        boolean Check3 = (SaleManager.getInstance().SaleOnProductAtDate(sproID1, "2022-04-21")==-1);
        boolean Check4 = (SaleManager.getInstance().SaleOnProductAtDate(sproID3, "2022-04-21")==-1);
        SaleManager.getInstance().RemoveSale(SaleManager.getInstance().SaleOnCategoryAtDate(scat1.getID(), "2022-05-21"));
        SaleManager.getInstance().AddSale("2022-01-20", "2022-3-20", String.valueOf(scat1.getID()), String.valueOf(sproID3), 10.1);
        boolean Check5 = (SaleManager.getInstance().SaleOnProductAtDate(sproID1, "2022-04-21")==-1);
        boolean Check6 = (SaleManager.getInstance().SaleOnProductAtDate(sproID3, "2022-04-21")==-1);
        SaleManager.getInstance().RemoveSale(SaleManager.getInstance().SaleOnCategoryAtDate(scat1.getID(), "2022-02-21"));
        assert(Check1&Check2&Check3&Check4&Check5&Check6);
    }

    @org.junit.jupiter.api.Test
    void SaleOnCategoryAtDate() {
        SaleManager.getInstance().AddSale("2022-01-20", "2022-12-20", String.valueOf(scat1.getID()), String.valueOf(sproID3), 10.1);
        boolean Check1 = (SaleManager.getInstance().SaleOnCategoryAtDate(scat1.getID(), "2022-04-21")!=-1);
        boolean Check2 = (SaleManager.getInstance().SaleOnCategoryAtDate(scat2.getID(), "2022-04-21")==-1);
        SaleManager.getInstance().RemoveSale(SaleManager.getInstance().SaleOnCategoryAtDate(scat1.getID(), "2022-04-21"));
        SaleManager.getInstance().AddSale("2022-05-20", "2022-12-20", String.valueOf(scat1.getID()), String.valueOf(sproID3), 10.1);
        boolean Check3 = (SaleManager.getInstance().SaleOnCategoryAtDate(scat1.getID(), "2022-04-21")==-1);
        boolean Check4 = (SaleManager.getInstance().SaleOnCategoryAtDate(scat2.getID(), "2022-05-21")==-1);
        SaleManager.getInstance().RemoveSale(SaleManager.getInstance().SaleOnCategoryAtDate(scat1.getID(), "2022-05-21"));
        SaleManager.getInstance().AddSale("2022-01-20", "2022-3-20", String.valueOf(scat1.getID()), String.valueOf(sproID3), 10.1);
        SaleManager.getInstance().AddSale("2022-01-20", "2022-3-20", String.valueOf(scat1.getID()), String.valueOf(sproID3), 10.1);
        boolean Check5 = (SaleManager.getInstance().SaleOnCategoryAtDate(scat1.getID(), "2022-04-21")==-1);
        boolean Check6 = (SaleManager.getInstance().SaleOnCategoryAtDate(scat2.getID(), "2022-04-21")==-1);
        SaleManager.getInstance().RemoveSale(SaleManager.getInstance().SaleOnCategoryAtDate(scat1.getID(), "2022-02-21"));
        assert(Check1&Check2&Check3&Check4&Check5&Check6);
    }

    @org.junit.jupiter.api.Test
    void RemoveSale() {
        SaleManager.getInstance().AddSale("2022-01-20", "2022-12-20", String.valueOf(scat1.getID()), String.valueOf(sproID3), 10.1);
        int SaleID1 = SaleManager.getInstance().SaleOnCategoryAtDate(scat1.getID(), "2022-04-21");
        boolean Check1 = SaleID1!=-1;
        SaleManager.getInstance().RemoveSale(SaleID1);
        boolean Check2 = SaleManager.getInstance().SaleOnCategoryAtDate(scat1.getID(), "2022-04-21")==-1;
        assert (Check1&Check2);
    }
}
