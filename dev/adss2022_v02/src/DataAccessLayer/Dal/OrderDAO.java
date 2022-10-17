package DataAccessLayer.Dal;

import BussinessLayer.Objects.Order;
import javafx.util.Pair;

import java.sql.*;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderDAO extends DAO {
    private static final String ORDER_ID = "ORDER_ID";
    private static final String ORDER_DATE = "ORDER_DATE";
    private static final String PERIODIC_ORDER = "PERIODIC_ORDER";
    private static final String IS_PLACED = "IS_PLACED";
    private static final String WEEK_DAYS = "WEEK_DAYS";
    private static final String TOTAL_PRICE_BEFORE_DISCOUNT = "TOTAL_PRICE_BEFORE_DISCOUNT";
    private static final String TOTAL_PRICE_AFTER_DISCOUNT = "TOTAL_PRICE_AFTER_DISCOUNT";
//    private static final String TransportDateColumnName = "TRANSPORT_DATE";
    private HashMap<Integer, Order> ordersHashMap;
    private static OrderDAO instance = null;

    private OrderDAO() {
        super("ORDERS");
        ordersHashMap = new HashMap<>();
    }

    public static OrderDAO getInstance() {
        if (instance == null)
            instance = new OrderDAO();
        return instance;
    }

    public void resetCache(){
        ordersHashMap.clear();
    }

    @Override
    public boolean Insert(Object orderObj) {
        Order order = (Order) orderObj;
        List<DayOfWeek> daysList = order.getDaysOfPeriodic();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        List<String> daysStringList = daysList.stream().map(dayOfWeek -> dayOfWeek.name()).collect(Collectors.toList());
        String daysString = daysStringList.toString();
        boolean res = true;
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3},{4},{5},{6},{7}) VALUES(?, ?, ?, ?,?,?,?)"
                , _tableName, ORDER_ID, ORDER_DATE, PERIODIC_ORDER, WEEK_DAYS, IS_PLACED, TOTAL_PRICE_BEFORE_DISCOUNT, TOTAL_PRICE_AFTER_DISCOUNT
        );
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            //Connection connection=open
            //pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, order.getOrderId());
            pstmt.setString(2, df.format(order.getOrderDate()));
            pstmt.setBoolean(3, order.getPeriodicOrder());
            pstmt.setString(4, daysString);
            pstmt.setBoolean(5, order.isPlaced());
            pstmt.setDouble(6, order.getTotalPriceBeforeDiscount());
            pstmt.setDouble(7, order.getTotalPriceAfterDiscount());
            pstmt.executeUpdate();

            Order oldOrder = ordersHashMap.get(order.getOrderId());
            if (oldOrder == null)
                ordersHashMap.put(order.getOrderId(), order);
            else
                ordersHashMap.replace(order.getOrderId(), oldOrder, order);


        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

//    public boolean PlaceOrder(Object orderObj) {
//        Order order = (Order) orderObj;
//        boolean res = true;
//        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? WHERE {2} = ? "
//                , _tableName, IS_PLACED, ORDER_ID
//        );
//        try (
//                PreparedStatement pstmt = connection.prepareStatement(sql)) {
//            //Connection connection=open
//            //pstmt = connection.prepareStatement(sql);
//            pstmt.setBoolean(1, order.isPlaced());
//            pstmt.setInt(2, order.getOrderId());
//            pstmt.executeUpdate();
//
//        } catch (SQLException e) {
//            System.out.println("Got Exception:");
//            System.out.println(e.getMessage());
//            System.out.println(sql);
//            res = false;
//        }
//        return res;
//    }


    @Override
    public boolean Delete(Object orderObj) {
        Order order = (Order) orderObj;
        boolean res = true;
        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = ?"
                , _tableName, ORDER_ID);

        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
//            Connection connection=open
//            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, order.getOrderId());
            pstmt.executeUpdate();
            if (ordersHashMap.containsKey(order.getOrderId())) {
                ordersHashMap.remove(order.getOrderId());
            }
            ItemOrderDAO.getInstance().DeleteByOrder(order.getOrderId());

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    @Override
    public Order convertReaderToObject(ResultSet rs) throws SQLException {
        Map<Pair<Integer, Integer>, Integer> itemOrderMap = ItemOrderDAO.getInstance().getItemsByOrderID(rs.getInt(ORDER_ID));

        String deliveryDays = rs.getString(WEEK_DAYS);
        boolean isPeriodic = rs.getBoolean(PERIODIC_ORDER);
        List<DayOfWeek> daysOfWeekList = new ArrayList<>();
        if (isPeriodic && (!deliveryDays.equals("[]"))) {
            String deliveryDays2 = deliveryDays.substring(1, deliveryDays.length() - 1);
            String[] daysArray = deliveryDays2.split(",");
            for (String dayS : daysArray) {
                String day = dayS.trim();
                daysOfWeekList.add(DayOfWeek.valueOf(day));
            }
        }
        Date exp;
        exp = Date.valueOf(rs.getString(ORDER_DATE));

        Order order = new Order(rs.getInt(ORDER_ID), exp, itemOrderMap, rs.getDouble(TOTAL_PRICE_BEFORE_DISCOUNT), rs.getDouble(TOTAL_PRICE_AFTER_DISCOUNT), isPeriodic, rs.getBoolean(IS_PLACED), daysOfWeekList);
        return order;
    }

    public int updateAndGetOrderTotalPriceById(int orderId) {
        Order order = getOrderById(orderId);
        int totalA = ItemOrderDAO.getInstance().getOrderTotalPriceById(orderId);
        int totalB = 0;
        Map<Pair<Integer, Integer>, Integer> map = ItemOrderDAO.getInstance().getItemsByOrderID(orderId);
        for (Map.Entry<Pair<Integer, Integer>, Integer> entry : map.entrySet()) {
            double priceBeforeDiscount = SupplierProductDAO.getInstance().getSupplierProductById(entry.getKey().getKey(), entry.getKey().getValue()).getPrice();
            totalB += priceBeforeDiscount * entry.getValue();
        }
        order.setItemCount(map);
        boolean res = true;
        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? , {2} = ? WHERE {3} = ? "
                , _tableName, TOTAL_PRICE_AFTER_DISCOUNT, TOTAL_PRICE_BEFORE_DISCOUNT, ORDER_ID
        );
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            //Connection connection=open
            //pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, totalA);
            pstmt.setInt(2, totalB);
            pstmt.setInt(3, order.getOrderId());
            pstmt.executeUpdate();
            if (!ordersHashMap.containsKey(order.getOrderId()))
                ordersHashMap.put(order.getOrderId(), order);

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return ItemOrderDAO.getInstance().getOrderTotalPriceById(orderId);
    }

    public Order getOrderById(int orderId) {
        if (ordersHashMap.containsKey(orderId))
            return ordersHashMap.get(orderId);
        Order order = null;
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + _tableName + " WHERE " + ORDER_ID + " = " + orderId + ";");

            if (rs.next()) {
                order = convertReaderToObject(rs);
                ordersHashMap.put(orderId, order);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return order;
    }

    public List<Order> getAllOrders() {
        List<Order> orderList = new ArrayList<>();
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBHandler.ORDERS + ";");
            while (rs.next()) {
                Order order = convertReaderToObject(rs);
                orderList.add(order);
                if (!ordersHashMap.containsKey(order.getOrderId()))
                    ordersHashMap.put(order.getOrderId(), order);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderList;
    }

    public int getMaxID() {
        int MaxId = 0;
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBHandler.ORDERS + ";");
            while (rs.next()) {
                int cId = rs.getInt(ORDER_ID);
                if (cId > MaxId) {
                    MaxId = cId;
                }
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MaxId;
    }

    public void updateHashMap(int orderID) {
        Order oldOrder = ordersHashMap.get(orderID);
        if (oldOrder != null)
            ordersHashMap.replace(orderID, oldOrder, getOrderById(orderID));
    }

}
