package Tests;

//import BusinessLayer.SuppliersBusiness.ProductS;
import BusinessLayer.SuppliersBusiness.Order;

import java.util.*;

//import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    /*Order order;
    ProductS it1;
    ProductS it2;
    NavigableMap<Integer, Double> discount1;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        Map<ProductS, Integer> itemsAmount = new HashMap<>();
        discount1 = new TreeMap<>();
        NavigableMap<Integer, Double> discount2 = new TreeMap<>();
        discount1.put(10, 10.0);
        discount2.put(20, 15.0);
        it1 = new ProductS("Chair", 10.0, "1111", discount1);
        it2 = new ProductS("Table", 20.0, "1112", discount2);
        itemsAmount.put(it1, 2);
        itemsAmount.put(it2, 1);
        order = new Order(1, 333, new Date(2022, 10, 12), itemsAmount);

    }

    @org.junit.jupiter.api.Test
    void addItemsToOrder() {
        assertEquals(2, order.getItemCount().entrySet().size());
        Map<ProductS, Integer> itemsAmount = new HashMap<>();
        NavigableMap<Integer, Double> discount3 = new TreeMap<>();
        NavigableMap<Integer, Double> discount4 = new TreeMap<>();
        ProductS it3 = new ProductS("Bottle", 5.0, "1113", discount3);
        ProductS it4 = new ProductS("Computer", 70.0, "1114", discount4);
        itemsAmount.put(it3, 2);
        itemsAmount.put(it4, 1);
        order.addItemsToOrder(itemsAmount);
        assertEquals(4, order.getItemCount().entrySet().size());
        assertTrue(order.getItemCount().containsKey(it3));
    }

    @org.junit.jupiter.api.Test
    void removeItemsFromOrder() {
        assertEquals(2, order.getItemCount().entrySet().size());
        String[] itemsToRemove = {"1111", "1112"};
        order.removeItemsFromOrder(itemsToRemove);
        assertEquals(0, order.getItemCount().entrySet().size());
        assertFalse(order.getItemCount().containsKey(it1));
    }

    @org.junit.jupiter.api.Test
    void checkTotalPrice() {
        Map<ProductS, Integer> itemsAmount = new HashMap<>();
        itemsAmount.put(it1, 11);
        Order o = new Order(2, 100, new Date(2022, 4, 23), itemsAmount);
        o.checkTotalPrice();
        assertEquals(110.0 ,o.getTotalPriceBeforeDiscount());
        assertEquals(99.0 ,o.getTotalPriceAfterDiscount());
    }*/

}