package ServiceLayer.ServiceObjects;

import BussinessLayer.Managers.OrderManager;
import BussinessLayer.Objects.Order;
import DataAccessLayer.TransportDAO;
import ServiceLayer.Response;
import ServiceLayer.ResponseT;

import java.sql.Date;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

public class ServiceOrderManager {
    private OrderManager orderMan;
    private static ServiceOrderManager instance;

    private ServiceOrderManager() {
        this.orderMan = new OrderManager();
    }

    public static ServiceOrderManager getInstance() {
        if (instance == null) {
            instance = new ServiceOrderManager();
        }
        return instance;
    }

    public Response loadData() {
        try {
            orderMan.loadData();
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public void deleteOrder(int orderID) {
        orderMan.deleteOrder(orderID);
    }

    public ResponseT<ServiceOrder> createPeriodicOrder(Map<Integer, Integer> productIdAmountMap, List<DayOfWeek> days) {
        try {
            Order order = orderMan.createPeriodicOrder(productIdAmountMap, days);
            ServiceOrder orderS = new ServiceOrder(order.getOrderId(), order.getOrderDate(), order.getItemCount(), order.getTotalPriceBeforeDiscount(), order.getTotalPriceAfterDiscount());
            return ResponseT.fromValue(orderS);
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public Response receiveAllOrders(java.sql.Date ThisDate) {
        try {
            orderMan.receiveAllOrders(ThisDate);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public ResponseT<String> PrintAllReceivedOrders(java.sql.Date ThisDate) {
        try {
            return ResponseT.fromValue(orderMan.PrintAllReceivedOrders(ThisDate));
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public Response receive_Defective_Orders(java.util.Date ThisDate, Map<Integer, Map<Integer, Date>> ExpMap, Map<Integer, Map<Integer, Integer>> DefMap) {
        try {
            orderMan.receive_Defective_Orders(ThisDate, ExpMap, DefMap);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public ResponseT<ServiceOrder> createOrder(Map<Integer, Integer> productIdAmountMap) {
        try {
            Order order = orderMan.createOrder(productIdAmountMap);
            ServiceOrder orderS = new ServiceOrder(order.getOrderId(), order.getOrderDate(), order.getItemCount(), order.getTotalPriceBeforeDiscount(), order.getTotalPriceAfterDiscount());
            return ResponseT.fromValue(orderS);
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<Boolean> isLegalOrderID(int orderID) {
        try {
            boolean ans = orderMan.isLegalOrderID(orderID);
            return ResponseT.fromValue(ans);
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }


    public Response placeAnOrder(int orderId) {
        return null;
    }


    public ResponseT addItemsToOrder(int orderId, Map<Integer, Integer> productIdAmountMap) {
        try {
            Order orderB = orderMan.addItemsToOrder(orderId, productIdAmountMap);
            ServiceOrder order = new ServiceOrder(orderB.getOrderId(), orderB.getOrderDate(), orderB.getItemCount(), orderB.getTotalPriceBeforeDiscount(), orderB.getTotalPriceAfterDiscount());
            return ResponseT.fromValue(order);
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<ServiceOrder> getOrderByOrderId(int orderId) {
        try {
            Order orderB = orderMan.getOrderByOrderId(orderId);
            ServiceOrder order = new ServiceOrder(orderB.getOrderId(), orderB.getOrderDate(), orderB.getItemCount(), orderB.getTotalPriceBeforeDiscount(), orderB.getTotalPriceAfterDiscount());
            return ResponseT.fromValue(order);
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }


    public Response reorder(int supplierId) {
        try {
            orderMan.reorder(supplierId);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public ResponseT<ServiceOrder> removeItemsFromOrder(int orderId, List<Integer> productsIdsToRemove) {
        try {
            Order orderB = orderMan.removeItemsFromOrder(orderId, productsIdsToRemove);
            ServiceOrder order = new ServiceOrder(orderB.getOrderId(), orderB.getOrderDate(), orderB.getItemCount(), orderB.getTotalPriceBeforeDiscount(), orderB.getTotalPriceAfterDiscount());
            return ResponseT.fromValue(order);
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<StringBuilder> createOrderReport() {
        try {
            StringBuilder orderReport = orderMan.createOrderReport();
            return ResponseT.fromValue(orderReport);
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }

    }


//
//    private List<String> convertDiscountsMapToList(NavigableMap<Integer, Double> discounts) {
//        List<String> list = new ArrayList<String>();
//        discounts.entrySet().forEach((integerDoubleEntry) -> mapToStringList(integerDoubleEntry, list));
//        return list;
//    }
//
//
//    private void itemsSToBList(Map<SupplierProduct, Integer> itemsAmount, ServiceProductS sProduct, int amount) {
//        itemsAmount.put(new SupplierProduct(sProduct.getProductID(), sProduct.getSupplierID(), sProduct.getName(), Double.parseDouble(sProduct.getPrice()), sProduct.getCatalogNumber(), convertDiscountsListToMap(sProduct.getDiscountByAmount())), amount);
//    }

//
//    private Map<SupplierProduct, Integer> toBusinessItemsAmountMap(Map<Integer, Integer> itemIdAmountMap, List<ServiceProductS> itemList, Map<ServiceProductS, Integer> itemAmountMap) {
//
//        for (ServiceProductS sProduct : itemList) {
//            itemAmountMap.put(sProduct, itemIdAmountMap.get(sProduct.getProductID()));
//        }
//        Map<SupplierProduct, Integer> itemsBAmountMap = new HashMap<>();
//        itemAmountMap.forEach((item, n) -> itemsSToBList(itemsBAmountMap, item, n));
//        return itemsBAmountMap;
//    }
//
//private NavigableMap<Integer, Double> convertDiscountsListToMap(List<String> discountsString) {
//    NavigableMap<Integer, Double> discountsMap = new TreeMap<>();
//    for (String pair : discountsString) {
//        String[] stringPair = pair.split("=");
//        Integer amount = Integer.parseInt(stringPair[0]);
//        Double percentages = Double.parseDouble(stringPair[1]);
//        discountsMap.put(amount, percentages);
//    }
//    return discountsMap;
//}
//
//    private void mapToStringList(Map.Entry entry, List<String> list) {
//        list.add(entry.toString());
//    }
//
//    private void itemsBToSList(Map<ServiceProductS, Integer> sItemsAmount, SupplierProduct supplierProduct, int amount) {
//        List<String> list = new ArrayList<String>();
//        supplierProduct.getDiscountByAmount().entrySet().forEach((integerDoubleEntry) -> mapToStringList(integerDoubleEntry, list));
//        sItemsAmount.put(new ServiceProductS(supplierProduct.getProductID(),supplierProduct.getPrice().toString(), supplierProduct.getCatalogNumber(), list), amount);
//    }

}