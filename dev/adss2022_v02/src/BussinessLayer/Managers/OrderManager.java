package BussinessLayer.Managers;

import BussinessLayer.Objects.Product;
import BussinessLayer.Objects.Order;
import BussinessLayer.Objects.SupplierProduct;
import BussinessLayer.Objects.Transport;
import DataAccessLayer.Dal.*;

import DataAccessLayer.TransportDAO;
import javafx.util.Pair;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

public class OrderManager {
    private TransportManager transportManager;

    public OrderManager() {
        transportManager = TransportManager.getInstance();
    }

    public int getOrderCount() {
        return OrderDAO.getInstance().getMaxID() + 1;
    }

    public boolean receiveOrder(int orderID, Map<Integer, java.sql.Date> ExpDateMap, Map<Integer, Integer> def) {
        try {
            Order ord = OrderDAO.getInstance().getOrderById(orderID);
            Map<Pair<Integer, Integer>, Integer> itemorderL = ItemOrderDAO.getInstance().getItemsByOrderID(orderID);
            //<<product,supplier> ,amount>
            for (Pair<Integer, Integer> pa : itemorderL.keySet()) {
                if (def.get(pa.getKey()) > 0) {
                    ProductManager.getInstance().addItems(itemorderL.get(pa), pa.getKey(), orderID, "Storage Not Set", ItemOrderDAO.getInstance().getItemBuyPrice(orderID, pa.getKey()), ExpDateMap.get(pa.getKey()), "Storage", true);
                    def.replace(pa.getKey(), def.get(pa.getKey()) - 1);
                } else {
                    ProductManager.getInstance().addItems(itemorderL.get(pa), pa.getKey(), orderID, "Storage Not Set", ItemOrderDAO.getInstance().getItemBuyPrice(orderID, pa.getKey()), ExpDateMap.get(pa.getKey()), "Storage", false);
                }

            }
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public boolean receiveOrder(int orderID) {
        try {
            Order ord = OrderDAO.getInstance().getOrderById(orderID);
            Map<Pair<Integer, Integer>, Integer> itemorderL = ItemOrderDAO.getInstance().getItemsByOrderID(orderID);
            //<<product,supplier> ,amount>
            for (Pair<Integer, Integer> pa : itemorderL.keySet()) {
                ProductManager.getInstance().addItems(itemorderL.get(pa), pa.getKey(), orderID, "Storage Not Set", ItemOrderDAO.getInstance().getItemBuyPrice(orderID, pa.getKey()), java.sql.Date.valueOf("2025-01-01"), "Storage", false);
            }
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public String PrintAllReceivedOrders(java.sql.Date ThisDate) {
        String ret = "";
        for (Transport t : TransportManager.getInstance().getTransportByDate(ThisDate)) {
            if (!t.isReceived()) {
                for (Pair<Integer, Integer> Pro_Sup : ItemOrderDAO.getInstance().getItemsByOrderID(t.getOrderID()).keySet()) {
                    ret = ret + "Order ID: " + t.getOrderID() + " | Product ID: " + Pro_Sup.getKey() + " | From Supplier(ID): " + Pro_Sup.getValue() + " | At the Amount of: " +
                            ItemOrderDAO.getInstance().getItemsByOrderID(t.getOrderID()).get(Pro_Sup) + "\n";
                }
            }
        }
        return ret;
    }

    public void receiveAllOrders(java.sql.Date ThisDate) {
        for (Transport t : TransportManager.getInstance().getTransportByDate(ThisDate)) {
            if (!t.isReceived())
                receiveOrder(t.getOrderID());
            TransportManager.getInstance().setIsReceived(t.getOrderID(), true);
        }
    }

    //<OrderID,<<ProductID,Exp_Date><ProductID, Amount_Defective>>>
    public void receive_Defective_Orders(java.util.Date ThisDate, Map<Integer, Map<Integer, java.sql.Date>> ExpMap, Map<Integer, Map<Integer, Integer>> DefMap) {
        for (Transport t : TransportManager.getInstance().getTransportByDate(ThisDate)) {
            if (!t.isReceived())
                receiveOrder(t.getOrderID(), ExpMap.get(t.getOrderID()), DefMap.get(t.getOrderID()));
            TransportManager.getInstance().setIsReceived(t.getOrderID(), true);
        }
    }

    //ORDERS

    public Order createOrder(Map<Integer, Integer> productsCount) {
        Pair<Map<Pair<Integer, Integer>, Integer>, Pair<Double, Double>> orderInfo = makeOrder(productsCount);
        Map<Pair<Integer, Integer>, Integer> orderProducts = orderInfo.getKey();
        Order order = new Order(getOrderCount(), Date.from(Instant.now()), orderProducts, false);
        order.setTotalPriceBeforeDiscount(orderInfo.getValue().getKey());
        order.setTotalPriceAfterDiscount(orderInfo.getValue().getValue());
        //System.out.println(order.getOrderId()+ " inserted");
        OrderDAO.getInstance().Insert(order);
        placeAnOrder(order);
        List<Integer> suppliers = new ArrayList<>(new HashSet<Integer>(getAllSuppliersFromOrder(order.getOrderId())));
        transportManager.createTransportForOrder(order, convertToLocalDate(order.getOrderDate()), productsCount, suppliers);
        return order;
    }


    public LocalDateTime convertToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public List<Integer> getAllSuppliersFromOrder(int orderId) {
        List<Integer> suppliers = new ArrayList<>();
        Map<Pair<Integer, Integer>, Integer> orderItems = ItemOrderDAO.getInstance().getItemsByOrderID(orderId);
        for (Map.Entry<Pair<Integer, Integer>, Integer> pair : orderItems.entrySet()) {
            suppliers.add(pair.getKey().getValue());
        }
        return suppliers;
    }

    public Order createPeriodicOrder(Map<Integer, Integer> productsCount, List<DayOfWeek> days) {
        Pair<Map<Pair<Integer, Integer>, Integer>, Pair<Double, Double>> orderInfo = makeOrder(productsCount);
        Map<Pair<Integer, Integer>, Integer> orderProducts = orderInfo.getKey();
        Order order = new Order(getOrderCount(), Date.from(Instant.now()), orderProducts, true, false, days);
        order.setTotalPriceBeforeDiscount(orderInfo.getValue().getKey());
        order.setTotalPriceAfterDiscount(orderInfo.getValue().getValue());
        OrderDAO.getInstance().Insert(order);
        placeAnOrder(order);
        List<Integer> suppliers = getAllSuppliersFromOrder(order.getOrderId());
        transportManager.createTransportForOrder(order, convertToLocalDate(order.getOrderDate()), productsCount, suppliers);
        return order;
    }

    public Pair<SupplierProduct, Double> cheapestSupplierProduct(Product product, int amount) {
        List<SupplierProduct> supplierProducts = SupplierProductDAO.getInstance().getAllSupplierProductsByProductId(product.getProductId());
        double minPrice = Double.MAX_VALUE;
        double current = 0;
        double discountOnProduct = 0;
        SupplierProduct cheapestProduct = null;
        NavigableMap<Integer, Double> discountMap;
        for (SupplierProduct sProduct : supplierProducts) {
            discountMap = sProduct.getDiscountByAmount();
            double discount = 0;
            try {
                discount = discountMap.floorEntry(amount).getValue();
            } catch (Exception e) {
            }
            discountOnProduct = sProduct.getPrice() * discount; // calculates the discount on a product
            current = (sProduct.getPrice() - discountOnProduct) * amount; // calculates product price after discount
            if (current < minPrice) {
                minPrice = current;
                cheapestProduct = sProduct;
            }
        }
        return new Pair<>(cheapestProduct, current);
    }

    //returns a pair where the key is the supplierProduct and amount, value is a pair of before and after discount prices
    private Pair<Map<Pair<Integer, Integer>, Integer>, Pair<Double, Double>> makeOrder(Map<Integer, Integer> productsCount) {
        List<Product> products = ProductDAO.getInstance().getAllProducts();
        double beforeDiscount = 0;
        double afterDiscount = 0;
        Map<Pair<Integer, Integer>, Integer> orderProducts = new HashMap<>();
        SupplierProduct cheapestProduct = null;
        Double totalPricePerProduct = 0.0;
        for (Product product : products) {
            if (productsCount.containsKey(product.getProductId())) {
                int amountOfProduct = productsCount.get(product.getProductId());
                Pair<SupplierProduct, Double> pair = cheapestSupplierProduct(product, amountOfProduct);
                if (pair != null && pair.getKey() != null) {
                    cheapestProduct = pair.getKey();
                    totalPricePerProduct = pair.getValue();
                    orderProducts.put(new Pair(cheapestProduct.getProductID(), cheapestProduct.getSupplierID()), amountOfProduct);
                    beforeDiscount += cheapestProduct.getPrice() * amountOfProduct; // accumulate total price without discount
                    afterDiscount += totalPricePerProduct; // accumulate total price with discount
                }
            }
        }
        return new Pair(orderProducts, new Pair<>(beforeDiscount, afterDiscount));
    }

    public void placeAnOrder(Order o) {
        //add to placed orders map and remove from unplaced
        if (o == null)
            throw new IllegalArgumentException("There is no order with the id.");
        o.setPlacedOrder(true);
        Map<Pair<Integer, Integer>, Integer> orderProducts = o.getItemCount();
        for (Map.Entry<Pair<Integer, Integer>, Integer> pair : orderProducts.entrySet()) {
            ItemOrderDAO.getInstance().Insert(pair, o);
        }
        OrderDAO.getInstance().updateAndGetOrderTotalPriceById(o.getOrderId());
    }

    public Order getUnplacedOrderByOrderId(int orderId) {
        Order o = OrderDAO.getInstance().getOrderById(orderId);
        if (o == null)
            throw new IllegalArgumentException("There is no order with the id: " + orderId);
        if (o.isPlaced())
            throw new IllegalArgumentException("The order already been placed");
        return o;
    }

    public Order getOrderByOrderId(int orderId) {
        Order o = OrderDAO.getInstance().getOrderById(orderId);
        if (o == null)
            throw new IllegalArgumentException("There is no order with the id: " + orderId);
        return o;
    }

    public void reorder(int orderId) {
        Order o = OrderDAO.getInstance().getOrderById(orderId);
        Map<Integer, Integer> productsAmount = new HashMap<>();
        Map<Pair<Integer, Integer>, Integer> orderProducts = o.getItemCount();
        for (Map.Entry<Pair<Integer, Integer>, Integer> pair : orderProducts.entrySet()) {
            productsAmount.put(pair.getKey().getKey(), pair.getValue());
        }
        Order newOrder = createOrder(productsAmount);
    }

    public boolean isValidDayToEdit(int orderID) {
        Order order = OrderDAO.getInstance().getOrderById(orderID);
        List<DayOfWeek> dayOfTheWeek = order.getDaysOfPeriodic();
        return !dayOfTheWeek.contains(LocalDate.now().getDayOfWeek());
    }


    public Order addItemsToOrder(int orderId, Map<Integer, Integer> productsCount) {
        Order o = OrderDAO.getInstance().getOrderById(orderId);
        if (o == null)
            throw new IllegalArgumentException("There is no order with the id: " + orderId);
        if (!isValidDayToEdit(orderId)) {
            throw new IllegalArgumentException("Can't edit order in the order periodic day.");
        }
        Map<Pair<Integer, Integer>, Integer> orderProducts = o.getItemCount();
        Map<Pair<Integer, Integer>, Integer> prodToUpdate = new HashMap<>();
        if(transportManager.checkIfTransportForOrderExist(o.getOrderId())) {
            Transport transport = transportManager.getTransportByOrderID(o.getOrderId());
            double totalWeight = transport.getTotalWeight();
            if (totalWeight < transport.getTruck().getMaxWeight()) {
                Set<Integer> prodToAdd = new HashSet<>();
                for (Integer id : productsCount.keySet()) {
                    prodToAdd.add(id);
                }
                for (Integer pId : prodToAdd) {
                    for (Map.Entry<Pair<Integer, Integer>, Integer> entry : orderProducts.entrySet()) {
                        if (entry.getKey().getKey() == pId) {
                            prodToUpdate.put(entry.getKey(), entry.getValue() + productsCount.get(entry.getKey().getKey()));
                            productsCount.remove(pId);
                        }
                    }
                }
                Map<Pair<Integer, Integer>, Integer> toAdd = makeOrder(productsCount).getKey();
                for (Map.Entry<Pair<Integer, Integer>, Integer> entry : prodToUpdate.entrySet()) {
                    ItemOrderDAO.getInstance().UpdateAmount(entry);
                }
                for (Map.Entry<Pair<Integer, Integer>, Integer> entry : toAdd.entrySet()) {
                    ItemOrderDAO.getInstance().Insert(entry, o);
                }
                OrderDAO.getInstance().updateAndGetOrderTotalPriceById(orderId);
                transport.setTotalWeight(totalWeight);
                return OrderDAO.getInstance().getOrderById(orderId);

            }
        }
        else{
            Set<Integer> prodToAdd = new HashSet<>();
            for (Integer id : productsCount.keySet()) {
                prodToAdd.add(id);
            }
            for (Integer pId : prodToAdd) {
                for (Map.Entry<Pair<Integer, Integer>, Integer> entry : orderProducts.entrySet()) {
                    if (entry.getKey().getKey() == pId) {
                        prodToUpdate.put(entry.getKey(), entry.getValue() + productsCount.get(entry.getKey().getKey()));
                        productsCount.remove(pId);
                    }
                }
            }
            Map<Pair<Integer, Integer>, Integer> toAdd = makeOrder(productsCount).getKey();
            for (Map.Entry<Pair<Integer, Integer>, Integer> entry : prodToUpdate.entrySet()) {
                ItemOrderDAO.getInstance().UpdateAmount(entry);
            }
            for (Map.Entry<Pair<Integer, Integer>, Integer> entry : toAdd.entrySet()) {
                ItemOrderDAO.getInstance().Insert(entry, o);
            }
            OrderDAO.getInstance().updateAndGetOrderTotalPriceById(orderId);
            return OrderDAO.getInstance().getOrderById(orderId);
        }
        throw new IllegalArgumentException("There is weight exception");
    }

    public double calculateTotalWeight(Map<Integer, Integer> productsCount) {
        double result = 0.0;
        for (Integer idProduct : productsCount.keySet()) {
            Product product = ProductManager.getInstance().getProduct(idProduct);
            result = result + product.getWeight() * productsCount.get(idProduct);
        }
        return result;
    }

    public Order removeItemsFromOrder(int orderId, List<Integer> productIdsToRemove) {
        Order o = OrderDAO.getInstance().getOrderById(orderId);
        if (o == null)
            throw new IllegalArgumentException("There is no order with the id: " + orderId);
        if (!isValidDayToEdit(orderId)) {
            throw new IllegalArgumentException("Can't edit order in the order periodic day.");
        }
        for (Integer pId : productIdsToRemove) {
            Pair p = new Pair(pId, orderId);
            ItemOrderDAO.getInstance().Delete(p);
        }
        OrderDAO.getInstance().updateAndGetOrderTotalPriceById(orderId);
        return OrderDAO.getInstance().getOrderById(orderId);
    }

    public boolean isLegalOrderID(int orderId) {
        Order order = OrderDAO.getInstance().getOrderById(orderId);
        return (order != null);
    }

    public void deleteOrder(int orderID) {
        Order order = OrderDAO.getInstance().getOrderById(orderID);
        if (order == null) {
            throw new IllegalArgumentException("this order is not exist");
        }
        OrderDAO.getInstance().Delete(order);
        Map<Pair<Integer, Integer>, Integer> orders = ItemOrderDAO.getInstance().getItemsByOrderID(orderID);
        for (Pair<Integer, Integer> orderI : orders.keySet()) {
            Pair<Integer, Integer> toDelete = new Pair<>(orderI.getKey(), orderID);
            ItemOrderDAO.getInstance().Delete(toDelete);
        }

    }

    public void loadData() {

        //order 1
        Map<Integer, Integer> map1 = new HashMap<>();
        map1.put(1, 60);
        map1.put(2, 6);
        Order o1 = createOrder(map1);
        int id1 = o1.getOrderId();

        //order 2
        Map<Integer, Integer> map2 = new HashMap<>();
        map2.put(3, 5);
        map2.put(4, 10);
        Order o2 = createOrder(map2);
        int id2 = o2.getOrderId();

        //order 3
        Map<Integer, Integer> map3 = new HashMap<>();
        map3.put(3, 20);
        map3.put(4, 25);
        List<DayOfWeek> days = new LinkedList<>();
        days.add(DayOfWeek.MONDAY);
        Order o3 = createPeriodicOrder(map3, days);
        int id3 = o3.getOrderId();

        //order 4
        Map<Integer, Integer> map4 = new HashMap<>();
        map4.put(3, 60);
        map4.put(4, 60);
        Order o4 = createOrder(map4);
        int id4 = o4.getOrderId();

    }

    public StringBuilder createOrderReport() {
        List<Order> orders = OrderDAO.getInstance().getAllOrders();
        StringBuilder orderReport = new StringBuilder();
        for (Order order : orders) {
            orderReport.append("order ID: ").append(order.getOrderId()).
                    append(" | order's creation date: ").append(order.getOrderDate()).
                    append(" | is periodic order: ").append(order.getPeriodicOrder()).
                    append(" | is placed: ").append(order.isPlaced()).append("\n");
        }
        return orderReport;
    }
}
