import BusinessLayer.InventoryBusinessLayer.Category;
import BusinessLayer.InventoryBusinessLayer.Item;
import BusinessLayer.InventoryBusinessLayer.Product;
import BusinessLayer.Managers.OrderManager;
import BusinessLayer.Managers.ProductManager;
import BusinessLayer.Managers.CategoryManager;
import BusinessLayer.Managers.SupplierManager;
import BusinessLayer.SuppliersBusiness.*;
import Dal.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

import java.sql.Date;
import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@TestMethodOrder(MethodOrderer.MethodName.class)
class ProductManagerTest {
    private static int proID1 = 0;
    private static int proID2 = 0;
    private static int proID3 = 0;
    private static BussinessLayer.Objects.Category cat1= null;
    private static Category cat2= null;
    private static Category subcat1= null;
    private static Category subcat2= null;
    private static Category subcat3= null;
    private static Category subsubcat1= null;
    private static Category subsubcat2= null;
    private static Category subsubcat3= null;


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
        cat1 = CategoryManager.getInstance().addCategory("cat1","main");
        cat2 = CategoryManager.getInstance().addCategory("cat2","main");
        subcat1 = CategoryManager.getInstance().addCategory("subcat1","sub");
        subcat2 = CategoryManager.getInstance().addCategory("subcat2","sub");
        subcat3 = CategoryManager.getInstance().addCategory("subcat3","sub");
        subsubcat1 = CategoryManager.getInstance().addCategory("subSubcat1","subSub");
        subsubcat2 = CategoryManager.getInstance().addCategory("subSubcat2","subSub");
        subsubcat3 = CategoryManager.getInstance().addCategory("subSubcat3","subSub");
        Product p1 = ProductManager.getInstance().createProduct("first", "mnu", 2,10, cat1.getID(), subcat1.getID(), subsubcat1.getID());
        proID1 = p1.getProductId();
        Product p2 = ProductManager.getInstance().createProduct("Second", "mnu", 4,20, cat1.getID(), subcat2.getID(), subsubcat2.getID());
        proID2 = p2.getProductId();
        Product p3 = ProductManager.getInstance().createProduct("third", "NotMnu", 6,60, cat2.getID(), subcat3.getID(), subsubcat3.getID());
        proID3 = p3.getProductId();
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
        supplierManager.addProductToAgreement(sup.getSupplierCard().getId(),proID1,"1-100",15.0,discountByAmount1);
        supplierManager.addProductToAgreement(sup.getSupplierCard().getId(),proID2,"1-101",1500.0,discountByAmount2);
        Map<Integer, Integer> map1 = new HashMap<>();
        map1.put(1, 60);
        map1.put(2, 6);
        Order o1 = ordermanager.createOrder(map1);
        int id1 = o1.getOrderId();
        ordermanager.placeAnOrder(o1);


        ProductManager.getInstance().addItems(3,proID1,id1,"a12",6,dt,"Shelf", false);
        ProductManager.getInstance().addItems(4,proID1,id1,"a12",6,dt,"Storage", false);
        ProductManager.getInstance().addItems(5,proID2,id1,"a13",6,dt,"Shelf", false);
        ProductManager.getInstance().addItems(4,proID2,id1,"a13",6,dt,"Storage", false);
        ProductManager.getInstance().addItems(8,proID3,id1,"a14",18,dt,"Shelf", false);
    }
    @org.junit.jupiter.api.Test
    void A_createProduct() {
        boolean Check1 = false;
        boolean Check2 = false;
        boolean Check3 = false;
        for(Product p: ProductManager.getInstance().getAllProduct()){
            if(p.getProductId()==proID1){
                Check1 = true;
            }else if(p.getProductId()==proID2){
                Check2 = true;
            }else if(p.getProductId()==proID3){
                Check3 = true;
            }
        }
       boolean Check4 = ProductManager.getInstance().getProduct(proID1).getCategory().get("main").equals(cat1.getID());
        boolean Check5 = ProductManager.getInstance().getProduct(proID1).getCategory().get("sub").equals(subcat1.getID());
        boolean Check6 = ProductManager.getInstance().getProduct(proID1).getCategory().get("subSub").equals(subsubcat1.getID());
        boolean Check7 = ProductManager.getInstance().getProduct(proID2).getCategory().get("main").equals(cat1.getID());
        boolean Check8 = ProductManager.getInstance().getProduct(proID2).getCategory().get("sub").equals(subcat2.getID());
        boolean Check9 = ProductManager.getInstance().getProduct(proID2).getCategory().get("subSub").equals(subsubcat2.getID());
        boolean Check10 = ProductManager.getInstance().getProduct(proID3).getCategory().get("main").equals(cat2.getID());
        boolean Check11 = ProductManager.getInstance().getProduct(proID3).getCategory().get("sub").equals(subcat3.getID());
        boolean Check12 = ProductManager.getInstance().getProduct(proID3).getCategory().get("subSub").equals(subsubcat3.getID());
        assert (Check1&Check2&Check3&Check4&Check5&Check6&Check7&Check8&Check9&Check10&Check11&Check12);

    }


    @org.junit.jupiter.api.Test
    void C_moveProductToStorage() {
        boolean Check1 = ProductManager.getInstance().getProduct(proID1).getAmountShelf()==3;
        boolean Check2 = ProductManager.getInstance().getProduct(proID1).getAmountStorage()==4;
        int storage = 0;
        int shelf = 0;
        for(Item itm: SuperItemDAO.getInstance().getAllSuperItemsOfProduct(proID1)){
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
        ProductManager.getInstance().moveProductToStorage(proID1, 2, "b15");
        boolean Check5 = ProductManager.getInstance().getProduct(proID1).getAmountShelf()==1;
        boolean Check6 = ProductManager.getInstance().getProduct(proID1).getAmountStorage()==6;
        int storage1 = 0;
        int shelf1 = 0;
        for(Item itm:SuperItemDAO.getInstance().getAllSuperItemsOfProduct(proID1)){
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
        boolean Check9 = false;
        try{
            ProductManager.getInstance().moveProductToStorage(proID1, 2, "b15");
        }catch(Exception e){
            Check9 = true;
        }

        boolean Check10 = ProductManager.getInstance().getProduct(proID1).getAmountShelf()==1;
        boolean Check11 = ProductManager.getInstance().getProduct(proID1).getAmountStorage()==6;
        int storage2 = 0;
        int shelf2 = 0;
        for(Item itm:SuperItemDAO.getInstance().getAllSuperItemsOfProduct(proID1)){
            if(itm.getStorageShelf().equals("Storage")){
                storage2++;
            }else if(itm.getStorageShelf().equals("Shelf")){
                shelf2++;
            }else{
                assert (false);
            }
        }
        boolean Check12 = shelf2==1;
        boolean Check13 = storage2==6;
        assert (Check1&Check2&Check3&Check4&Check5&Check6&Check7&Check8&
                Check9&Check10&Check11&Check12&Check13);

    }

    @org.junit.jupiter.api.Test
    void D_moveProductToShelf() {
        boolean Check1 = ProductManager.getInstance().getProduct(proID2).getAmountShelf()==5;
        boolean Check2 = ProductManager.getInstance().getProduct(proID2).getAmountStorage()==4;
        int storage = 0;
        int shelf = 0;
        for(Item itm:SuperItemDAO.getInstance().getAllSuperItemsOfProduct(proID2)){
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
        ProductManager.getInstance().moveProductToShelf(proID2, 2, "b15");
        boolean Check5 = ProductManager.getInstance().getProduct(proID2).getAmountShelf()==7;
        boolean Check6 = ProductManager.getInstance().getProduct(proID2).getAmountStorage()==2;
        int storage1 = 0;
        int shelf1 = 0;
        for(Item itm:SuperItemDAO.getInstance().getAllSuperItemsOfProduct(proID2)){
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
            ProductManager.getInstance().moveProductToShelf(proID2, 3, "b15");
        }catch(Exception e){
            Check9 = true;
        }

        boolean Check10 = ProductManager.getInstance().getProduct(proID2).getAmountShelf()==7;
        boolean Check11 = ProductManager.getInstance().getProduct(proID2).getAmountStorage()==2;
        int storage2 = 0;
        int shelf2 = 0;
        for(Item itm:SuperItemDAO.getInstance().getAllSuperItemsOfProduct(proID2)){
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
        ProductManager.getInstance().moveProductToShelf(proID2, 2, "b15");
        assert (Check1&Check2&Check3&Check4&Check5&Check6&Check7&Check8&
                Check9&Check10&Check11&Check12&Check13);
    }
}