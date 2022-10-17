package Tests;

//import BusinessLayer.SuppliersBusiness.ProductS;
import BusinessLayer.Managers.OrderManager;
import BusinessLayer.SuppliersBusiness.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class OrderManagerTest {

    /*private OrderManager oManager;
    private Map<ProductS, Integer> itemsAmount1;
    private Map<ProductS, Integer> itemsAmount2;
    private Map<ProductS, Integer> itemsAmount3;
    private int supplier1 = 1;
    private int supplier2 = 2;
    private Order o1;
    private Order o2;
    private Order o3;

    @BeforeEach
    void setUp() {
        oManager = new OrderManager();

        //init orders
        itemsAmount1 = new HashMap<>();
        itemsAmount2 = new HashMap<>();
        itemsAmount3 = new HashMap<>();

        NavigableMap<Integer, Double> discount1 = new TreeMap<>();
        NavigableMap<Integer, Double> discount2 = new TreeMap<>();
        NavigableMap<Integer, Double> discount3 = new TreeMap<>();
        NavigableMap<Integer, Double> discount4 = new TreeMap<>();


        discount1.put(10, 10.0);
        discount2.put(20, 15.0);
        discount3.put(30, 20.0);
        discount4.put(50, 25.0);

        ProductS it1 = new ProductS("Chair", 10.0, "1111", discount1);
        ProductS it2 = new ProductS("Table", 20.0, "1112", discount2);
        ProductS it3 = new ProductS("Bottle", 5.0, "1113", discount3);
        ProductS it4 = new ProductS("Computer", 70.0, "1114", discount4);
        itemsAmount1.put(it1, 2);
        itemsAmount1.put(it2, 1);
        itemsAmount2.put(it3, 3);
        itemsAmount2.put(it4, 3);
        itemsAmount3.put(it4, 2);
    }



    @Test
    void createOrder() {
        assertEquals(0, oManager.getUnPlacedOrdersIDMap().size());
        assertNull(o1);
        assertNull(o2);
        o1 = oManager.createOrder(1, itemsAmount1);
        o2 = oManager.createOrder(2, itemsAmount2);
        assertEquals(2, oManager.getUnPlacedOrdersIDMap().size());
        assertEquals(1, o1.getOrderId());
        assertEquals(2, o2.getOrderId());
        assertNotNull(o1);
        assertNotNull(o2);
    }

    @Test
    void placeAnOrder() {
        o1 = oManager.createOrder(1, itemsAmount1);
        o2 = oManager.createOrder(2, itemsAmount2);
        assertEquals(2, oManager.getUnPlacedOrdersIDMap().size());
        oManager.placeAnOrder(o1.getOrderId());
        assertEquals(1, oManager.getUnPlacedOrdersIDMap().size());
        oManager.placeAnOrder(o2.getOrderId());
        assertEquals(0, oManager.getUnPlacedOrdersIDMap().size());
        assertEquals(2, oManager.getPlacedOrdersIDMap().size());

        int failOrderID = 55;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            oManager.placeAnOrder(failOrderID);
        });
        String expectedMessage = "There is no order with the id: " + failOrderID;
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    void reorder() {
        o1 = oManager.createOrder(1, itemsAmount1);
        oManager.placeAnOrder(o1.getOrderId());
        o3 = oManager.createOrder(2, itemsAmount3);
        oManager.placeAnOrder(o3.getOrderId());
        o2 = oManager.createOrder(2, itemsAmount2);
        oManager.placeAnOrder(o2.getOrderId());
        assertEquals(3, oManager.getPlacedOrdersIDMap().size());
        assertEquals(2, oManager.getOrdersBySupplier(2).size());
        oManager.reorder(2);
        assertEquals(3, oManager.getOrdersBySupplier(2).size());
        List<Order> supplier2orders = oManager.getSupplierOrdersMap().get(2);
        assertEquals(4, supplier2orders.get(supplier2orders.size() - 1).getOrderId());
    }*/
}