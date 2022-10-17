package Tests;

import BusinessLayer.InventoryBusinessLayer.Category;
import BusinessLayer.InventoryBusinessLayer.Product;
import BusinessLayer.Managers.CategoryManager;
import BusinessLayer.Managers.OrderManager;
import BusinessLayer.Managers.ProductManager;
import BusinessLayer.Managers.SupplierManager;
import BusinessLayer.SuppliersBusiness.*;
import Dal.*;
import javafx.util.Pair;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class SupplierTests {
    private static int proID1 = 0;
    private static int proID2 = 0;
    private static int proID3 = 0;
    private static Category cat1= null;
    private static Category cat2= null;
    private static Category subcat1= null;
    private static Category subcat2= null;
    private static Category subcat3= null;
    private static Category subsubcat1= null;
    private static Category subsubcat2= null;
    private static Category subsubcat3= null;
    private static Order or = null;
    private static Agreement ag = null;
    private static Supplier sup = null;
    private static SupplierCard SC = null;
    private static SupplierManager supplierManager = null;
    private static OrderManager ordermanager = null;

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
        supplierManager = new SupplierManager();
        ordermanager = new OrderManager();
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
        sup = supplierManager.addSupplier(sCard1, agr1);
        supplierManager.addContactsToSupplier(sup.getSupplierCard().getId(),"Harry", "poterH@gmail.com", "0501234567");
        supplierManager.addProductToAgreement(sup.getSupplierCard().getId(),proID1,"1-100",15.0,discountByAmount1);
        supplierManager.addProductToAgreement(sup.getSupplierCard().getId(),proID2,"1-101",1500.0,discountByAmount2);
        Map<Integer, Integer> map1 = new HashMap<>();
        map1.put(1, 60);
        map1.put(2, 6);
        or = ordermanager.createOrder(map1);
        int id1 = or.getOrderId();
        ordermanager.placeAnOrder(or);


        ProductManager.getInstance().addItems(3,proID1,id1,"a12",6,dt,"Shelf", false);
        ProductManager.getInstance().addItems(4,proID1,id1,"a12",6,dt,"Storage", false);
        ProductManager.getInstance().addItems(5,proID2,id1,"a13",6,dt,"Shelf", false);
        ProductManager.getInstance().addItems(4,proID2,id1,"a13",6,dt,"Storage", false);
        ProductManager.getInstance().addItems(8,proID3,id1,"a14",18,dt,"Shelf", false);
    }
    @org.junit.jupiter.api.Test
    void checkOrder() {
        boolean check1 = ordermanager.getOrderByOrderId(or.getOrderId()).equals(or);
        boolean check2 = ordermanager.getOrderByOrderId(or.getOrderId()).equals(or);
        ordermanager.placeAnOrder(or);
        boolean check3 = false;
        try{
            ordermanager.getUnplacedOrderByOrderId(or.getOrderId());
        }catch(Exception e){
            check3 = true;
        }
        ordermanager.reorder(or.getOrderId());
        assert(check1&check2&check3);
    }

    /*
    @org.junit.jupiter.api.Test
    void checkSupplier() {
        supplierManager.addContactsToSupplier(sup.getSupplierCard().getId(),sup.getSupplierCard().getName(),"SomeMail.@post.bgu.ac.il","0555555555");
        Contact con1 = new Contact(sup.getSupplierCard().getId(),sup.getSupplierCard().getName(),"SomeMail.@post.bgu.ac.il","0555555555");
        Contact con2 = supplierManager.editSupplierContacts(sup.getSupplierCard().getId(),sup.getSupplierCard().getName(),"SomeMail.@gmail.com","0555555555",true);
        boolean check1 = false;
        boolean check2 = false;
        boolean check3 = true;
        for(Contact co:ContactDAO.getInstance().getContactsBySupplierID(sup.getSupplierCard().getId())){
            if(co.getName().equals(con1.getName())&co.getEmail().equals(con1.getEmail())&co.getSupplierId()==(con1.getSupplierId())){
                check1=true;
            }else if(co.getName().equals(con2.getName())&co.getEmail().equals(con2.getEmail())&co.getSupplierId()==(con2.getSupplierId())){
                check2=true;
            }else if(co.getName().equals(con1.getName())&co.getEmail().equals(con1.getEmail())&co.getSupplierId()==(con1.getSupplierId())){
                check3=false;
            }
        }
        assert (check1&check2&check3);
    }*/

    @org.junit.jupiter.api.Test
    void ValidOrder(){
        //order 1
        Map<Integer, Integer> map1 = new HashMap<>();
        map1.put(1, 60);
        map1.put(1, 6);
        Order o1 = ordermanager.createOrder(map1);
        boolean check1 = (ordermanager.getOrderByOrderId(o1.getOrderId()).equals(o1));
        int id1 = o1.getOrderId();
        ordermanager.placeAnOrder(o1);

        //order 2
        Map<Integer, Integer> map2 = new HashMap<>();
        map1.put(1, 10);
        map1.put(1, 50);
        Order o2 = ordermanager.createOrder(map2);
        boolean check2 = (ordermanager.getOrderByOrderId(o2.getOrderId()).equals(o2));
        int id2 = o2.getOrderId();
        ordermanager.placeAnOrder(o2);

        //order 3
        Map<Integer, Integer> map3 = new HashMap<>();
        map3.put(1, 10);
        map3.put(1, 50);
        List<DayOfWeek> days = new LinkedList<>();
        days.add(DayOfWeek.SUNDAY);
        days.add(DayOfWeek.MONDAY);
        days.add(DayOfWeek.TUESDAY);
        days.add(DayOfWeek.WEDNESDAY);
        days.add(DayOfWeek.THURSDAY);
        days.add(DayOfWeek.FRIDAY);
        days.add(DayOfWeek.SATURDAY);
        Order o3 = ordermanager.createPeriodicOrder(map3, days);
        boolean check3 = (ordermanager.getOrderByOrderId(o3.getOrderId()).equals(o3));
        int id3 = o3.getOrderId();
        ordermanager.placeAnOrder(o3);
        assert (check1&check2&check3);
    }

    @org.junit.jupiter.api.Test
    void CheckDayOrder(){
        //order 1
        Map<Integer, Integer> map1 = new HashMap<>();
        map1.put(1, 60);
        map1.put(1, 6);
        Order o1 = ordermanager.createOrder(map1);
        boolean check1 = ordermanager.isValidDayToEdit(o1.getOrderId());
        int id1 = o1.getOrderId();
        ordermanager.placeAnOrder(o1);
        //order 2
        Map<Integer, Integer> map3 = new HashMap<>();
        map3.put(1, 10);
        map3.put(1, 50);
        List<DayOfWeek> days = new LinkedList<>();
        days.add(DayOfWeek.SUNDAY);
        days.add(DayOfWeek.MONDAY);
        days.add(DayOfWeek.TUESDAY);
        days.add(DayOfWeek.WEDNESDAY);
        days.add(DayOfWeek.THURSDAY);
        days.add(DayOfWeek.FRIDAY);
        days.add(DayOfWeek.SATURDAY);
        Order o3 = ordermanager.createPeriodicOrder(map3, days);
        boolean check2 = !ordermanager.isValidDayToEdit(o3.getOrderId());
        int id3 = o3.getOrderId();
        ordermanager.placeAnOrder(o3);
        assert (check1&check2);
    }
    /*@org.junit.jupiter.api.Test
    void placeOrder(){
        //order 1
        Map<Integer, Integer> map1 = new HashMap<>();
        map1.put(1, 60);
        map1.put(1, 6);
        Order o1 = ordermanager.createOrder(map1);
        boolean check1 = false;
        try{
            ordermanager.getUnplacedOrderByOrderId(o1.getOrderId());
        }catch(Exception e){check1 = true;}
        int id1 = o1.getOrderId();
        ordermanager.placeAnOrder(o1);
        //order 2
        Map<Integer, Integer> map3 = new HashMap<>();
        map3.put(1, 10);
        map3.put(1, 50);
        List<DayOfWeek> days = new LinkedList<>();
        days.add(DayOfWeek.SUNDAY);
        days.add(DayOfWeek.MONDAY);
        days.add(DayOfWeek.TUESDAY);
        days.add(DayOfWeek.WEDNESDAY);
        days.add(DayOfWeek.THURSDAY);
        days.add(DayOfWeek.FRIDAY);
        days.add(DayOfWeek.SATURDAY);
        Order o2 = ordermanager.createPeriodicOrder(map3, days);
        boolean check2 = ordermanager.getUnplacedOrderByOrderId(o2.getOrderId()).equals(o2);
        int id2 = o2.getOrderId();
        ordermanager.placeAnOrder(o2);
        try{
            ordermanager.getUnplacedOrderByOrderId(o2.getOrderId());
        }catch(Exception e){check2 = true;}
        assert (check1&check2);
    }

    @org.junit.jupiter.api.Test
    void CheckOrderDisCount(){
        //supplier 1

        SupplierCard sCard1 = new SupplierCard(SupplierDAO.getInstance().getMaxID()+1 , "AnimalKingdom", "Tel Aviv Shaul st 11", "187444", PaymentMethod.Cash);
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
        int supId = SupplierDAO.getInstance().getMaxID()+1;
        SupplierProduct it2 = new SupplierProduct(supId, sCard1.getId(), "Puppies", 1500.0, "1-101", discountByAmount2);
        SupplierProduct it1 = new SupplierProduct(supId+1, sCard1.getId(), "Animal Food", 15.0, "1-100", discountByAmount1);
        Map<String, SupplierProduct> product1 = new HashMap<>();
        product1.put(it1.getCatalogNumber(), it1);
        product1.put(it2.getCatalogNumber(), it2);

        Agreement agr1 = new Agreement("Animal Kingdom", deliveryDays1, SupplyMethod.byDays, product1.keySet().stream().collect(Collectors.toList()));

        supplierManager.addSupplier(sCard1, agr1);
        supplierManager.addContactsToSupplier(sCard1.getId(), "Harry", "poterH@gmail.com", "0501234567");
        supplierManager.addProductToAgreement(sCard1.getId(), supId, "1-100", 15.0, discountByAmount1);
        supplierManager.addProductToAgreement(sCard1.getId(), supId+1, "1-101", 1500.0, discountByAmount2);
        Map<Integer, Integer> map1 = new HashMap<>();
        map1.put(supId, 60);
        Order o1 = ordermanager.createOrder(map1);
        boolean check1 = o1.getTotalPriceBeforeDiscount()>o1.getTotalPriceAfterDiscount();
        Map<Integer, Integer> map2 = new HashMap<>();
        map2.put(supId, 60);
        Order o2 = ordermanager.createOrder(map2);
        boolean check2 = o2.getTotalPriceBeforeDiscount()>o2.getTotalPriceAfterDiscount();
        assert (check1&check2);
    }

    @org.junit.jupiter.api.Test
    void CheckOrderAmountDisCount(){
        //supplier 1

        SupplierCard sCard1 = new SupplierCard(SupplierDAO.getInstance().getMaxID()+1 , "AnimalKingdom", "Tel Aviv Shaul st 11", "187444", PaymentMethod.Cash);
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
        int supId = SupplierDAO.getInstance().getMaxID()+1;
        SupplierProduct it2 = new SupplierProduct(supId, sCard1.getId(), "Puppies", 1500.0, "1-101", discountByAmount2);
        SupplierProduct it1 = new SupplierProduct(supId+1, sCard1.getId(), "Animal Food", 15.0, "1-100", discountByAmount1);
        Map<String, SupplierProduct> product1 = new HashMap<>();
        product1.put(it1.getCatalogNumber(), it1);
        product1.put(it2.getCatalogNumber(), it2);

        Agreement agr1 = new Agreement("Animal Kingdom", deliveryDays1, SupplyMethod.byDays, product1.keySet().stream().collect(Collectors.toList()));

        supplierManager.addSupplier(sCard1, agr1);
        supplierManager.addContactsToSupplier(sCard1.getId(), "Harry", "poterH@gmail.com", "0501234567");
        supplierManager.addProductToAgreement(sCard1.getId(), supId, "1-100", 15.0, discountByAmount1);
        supplierManager.addProductToAgreement(sCard1.getId(), supId+1, "1-101", 1500.0, discountByAmount2);
        Map<Integer, Integer> map1 = new HashMap<>();
        map1.put(supId, 60);
        Order o1 = ordermanager.createOrder(map1);
        Map<Integer, Integer> map2 = new HashMap<>();
        map2.put(supId, 60);
        Order o2 = ordermanager.createOrder(map2);
        boolean check1 = o1.getTotalPriceBeforeDiscount()*(0.9) == o1.getTotalPriceAfterDiscount();
        boolean check2 = o2.getTotalPriceBeforeDiscount()*(0.85) == o2.getTotalPriceAfterDiscount();
        assert (check1&check2);
    }


    @org.junit.jupiter.api.Test
    void z_addItemsToOrder(){
        Map<Integer, Integer> map1 = new HashMap<>();
        map1.put(1, 60);
        map1.put(2, 10);
        map1.put(3, 31);
        Map<Integer,Integer> prices = new HashMap<>();
        ordermanager.addItemsToOrder(or.getOrderId(),map1);
        for (Pair<Integer,Integer> pair:or.getItemCount().keySet()) {
            prices.put(pair.getKey(),ItemOrderDAO.getInstance().getItemBuyPrice(or.getOrderId(),pair.getKey()));
        }
        int price1 = prices.get(1);
        int price2 = prices.get(2);
        int price3 = prices.get(3);
        assert (price1 == 0.25*15 && price2 == 0.4*1500 && price3 == 0.05*7);
    }*/

}
