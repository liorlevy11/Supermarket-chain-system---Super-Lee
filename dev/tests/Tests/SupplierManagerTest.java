//package Tests;
//
//import BusinessLayer.Managers.SupplierManager;
//import BusinessLayer.SuppliersBusiness.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.DayOfWeek;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class SupplierManagerTest {
//
//    private SupplierManager sManager;
//    private Supplier supp1;
//    private Supplier supp2;
//    private Contact c1;
//    private Contact c2;
//
//    @BeforeEach
//    void setUp() {
//        sManager = new SupplierManager();
//
//        //supplier 1
//        c1 = new Contact("tamuz", "muzmuz@nana10.co.il", "0501234567");
//        Map<String, Contact> contactMap1 = new HashMap<>();
//        contactMap1.put("tamuz", c1);
//        List<String> brandsSupplied1 = new ArrayList<>();
//        brandsSupplied1.add("NogaCats");
//        brandsSupplied1.add("LunaDogs");
//        List<String> itemCategories1 = new ArrayList<>();
//        itemCategories1.add("Cats");
//        itemCategories1.add("Dogs");
//        SupplierCard sCard1 = new SupplierCard(1, "Gal&Sons", "Rager 11", "g123", PaymentMethod.TransitToAccount, contactMap1, brandsSupplied1, itemCategories1);
//        List<DayOfWeek> deliveryDays1 = new ArrayList<>();
//        Map<String, ProductS> items1 = new HashMap<>();
//        Map<Integer, Double> overallDiscount1 = new HashMap<>();
//        Agreement agr1 = new Agreement("Gal&Sons", deliveryDays1, SupplyMethod.byDays, items1, overallDiscount1);
//        supp1 = new Supplier(sCard1, agr1);
//
//        //supplier 2
//        c2 = new Contact("noga", "n0gaTh3Qu33n@walla.co.il", "0507654321");
//        Map<String, Contact> contactMap2 = new HashMap<>();
//        contactMap2.put("noga", c2);
//        List<String> brandsSupplied2 = new ArrayList<>();
//        brandsSupplied2.add("Oranjuice");
//        brandsSupplied2.add("Tamara");
//        List<String> itemCategories2 = new ArrayList<>();
//        itemCategories2.add("Juice");
//        itemCategories2.add("Fruit");
//        SupplierCard sCard2 = new SupplierCard(2, "MitzTamuzim", "Rager 12", "t123", PaymentMethod.Cash, contactMap2, brandsSupplied2, itemCategories2);
//        List<DayOfWeek> deliveryDays2 = new ArrayList<>();
//        Map<String, ProductS> items2 = new HashMap<>();
//        Map<Integer, Double> overallDiscount2 = new HashMap<>();
//        Agreement agr2 = new Agreement("Gal&Sons", deliveryDays2, SupplyMethod.byDays, items2, overallDiscount2);
//        supp2 = new Supplier(sCard2, agr2);
//    }
//
//    @Test
//    void addSupplier() {
//        assertEquals(0, sManager.getSuppliers().size());
//        sManager.addSupplier(supp1.getSupplierCard(), supp1.getAgreement());
//        assertEquals(1, sManager.getSuppliers().size());
//        assertEquals(1, sManager.getSuppliers().get(1).getSupplierCard().getId());
//    }
//
//    @Test
//    void removeSupplier() {
//        sManager.addSupplier(supp1.getSupplierCard(), supp1.getAgreement());
//        sManager.addSupplier(supp2.getSupplierCard(), supp2.getAgreement());
//        assertEquals(2, sManager.getSuppliers().size());
//        sManager.removeSupplier(1);
//        assertEquals(1, sManager.getSuppliers().size());
//        assertFalse(sManager.getSuppliers().containsKey(1));
//        assertTrue(sManager.getSuppliers().containsKey(2));
//
//        int failsupplierID = 55;
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            sManager.removeSupplier(failsupplierID);
//        });
//        String expectedMessage = "no supplier with id: " + failsupplierID;
//        String actualMessage = exception.getMessage();
//        assertTrue(actualMessage.equals(expectedMessage));
//    }
//
//    @Test
//    void addContactsToSupplier() {
//        Contact c3 = new Contact("noy", "STOM@gmail.com", "0541233211");
//        sManager.addSupplier(supp1.getSupplierCard(), supp1.getAgreement());
//        sManager.addContactsToSupplier(supp1.getSupplierCard().getId(), c3.getName(), c3.getEmail(), c3.getPhoneNumber());
//        assertEquals(2, sManager.getSupplier(supp1.getSupplierCard().getId()).getSupplierCard().getContactMaps().size());
//        assertTrue(sManager.getSupplier(supp1.getSupplierCard().getId()).getSupplierCard().getContactMaps().containsKey(c3.getName()));
//    }
//
//    @Test
//    void addItemToAgreement() {
//        ProductS productS = new ProductS("Bamba Nugat", 10.0, "1-12", null);
//        sManager.addSupplier(supp1.getSupplierCard(), supp1.getAgreement());
//        sManager.addProductToAgreement(supp1.getSupplierCard().getId(), productS.getName(), productS.getPrice(), productS.getCatalogNumber(), productS.getDiscountByAmount());
//        assertEquals(1, sManager.getSupplier(supp1.getSupplierCard().getId()).getAgreement().getProducts().size());
//        assertTrue(sManager.getSupplier(supp1.getSupplierCard().getId()).getAgreement().getProducts().containsKey(productS.getCatalogNumber()));
//    }
//}