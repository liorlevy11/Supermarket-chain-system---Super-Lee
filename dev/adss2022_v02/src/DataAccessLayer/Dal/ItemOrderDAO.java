package DataAccessLayer.Dal;


import BussinessLayer.Objects.SupplierProduct;
import BussinessLayer.Objects.Order;
import javafx.util.Pair;

import java.sql.*;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;

public class ItemOrderDAO extends DAO {
    private static ItemOrderDAO instance = null;
    private static final String PRODUCT_ID = "PRODUCT_ID";
    private static final String SUPPLIER_ID = "SUPPLIER_ID";
    private static final String ORDER_ID = "ORDER_ID";
    private static final String ORDER_DATE = "ORDER_DATE";
    private static final String BUY_PRICE = "BUY_PRICE";
    private static final String AMOUNT = "AMOUNT";

    private ItemOrderDAO() {
        super("ITEM_ORDERS");
    }

    public static ItemOrderDAO getInstance() {
        if (instance == null) {
            instance = new ItemOrderDAO();
        }
        return instance;
    }

    public void resetCache() {
        return;
    }

    public boolean Insert(Map.Entry<Pair<Integer, Integer>, Integer> entry, Order orderObj) {
        Order order = (Order) orderObj;
        boolean res = true;
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4}, {5} ,{6}) VALUES(?, ?, ?, ?, ?,?) "
                , _tableName, PRODUCT_ID, SUPPLIER_ID, ORDER_ID, ORDER_DATE, BUY_PRICE, AMOUNT
        );
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            //Connection connection=open
            //pstmt = connection.prepareStatement(sql);
            int productId = entry.getKey().getKey();
            SupplierProduct sProduct = SupplierProductDAO.getInstance().getSupplierProductById(productId, entry.getKey().getValue());
            NavigableMap<Integer, Double> map = sProduct.getDiscountByAmount();
            double price;
            if (map.floorEntry(entry.getValue()) == null) {
                price = sProduct.getPrice();
            } else {
                price = sProduct.getPrice() * (1 - (map.floorEntry(entry.getValue()).getValue() / 100));
            }
            pstmt.setInt(1, productId);
            pstmt.setInt(2, entry.getKey().getValue());
            pstmt.setInt(3, order.getOrderId());
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            pstmt.setString(4, df.format(order.getOrderDate()));
            pstmt.setDouble(5, price);
            pstmt.setInt(6, entry.getValue());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    @Override
    public boolean Insert(Object orderObj) {
        Order order = (Order) orderObj;
        boolean res = true;
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4}, {5} ,{6}) VALUES(?, ?, ?, ?, ?,?) "
                , _tableName, PRODUCT_ID, SUPPLIER_ID, ORDER_ID, ORDER_DATE, BUY_PRICE, AMOUNT
        );
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            //Connection connection=open
            //pstmt = connection.prepareStatement(sql);
            Map<Pair<Integer, Integer>, Integer> map = order.getItemCount();
            for (Map.Entry<Pair<Integer, Integer>, Integer> entry : map.entrySet()) {
                int productId = entry.getKey().getKey();
                SupplierProduct sProduct = SupplierProductDAO.getInstance().getSupplierProductById(productId, entry.getKey().getValue());
                double price = sProduct.getPrice() * sProduct.getDiscountByAmount().get(productId);
                pstmt.setInt(1, productId);
                pstmt.setInt(2, entry.getKey().getValue());
                pstmt.setInt(3, order.getOrderId());
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                pstmt.setString(4, df.format(order.getOrderDate()));
                pstmt.setDouble(5, price);
                pstmt.setInt(6, entry.getValue());
                pstmt.executeUpdate();
                OrderDAO.getInstance().updateHashMap(order.getOrderId());
            }
        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    @Override
    public boolean Delete(Object pairsOfIDs) {//list of pairs, each pari is <productID,orderID>
        Pair<Integer, Integer> pair = (Pair<Integer, Integer>) pairsOfIDs;
        boolean res = true;
        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = ? AND {2} = ?"
                , _tableName, PRODUCT_ID, ORDER_ID);

        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
//            Connection connection=open
//            pstmt = connection.prepareStatement(sql);

            pstmt.setInt(1, pair.getKey());
            pstmt.setInt(2, pair.getValue());
            pstmt.executeUpdate();
            OrderDAO.getInstance().updateHashMap(pair.getValue());
        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public boolean DeleteByOrder(int orderID) {
        boolean res = true;
        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1}=?"
                , _tableName, ORDER_ID);

        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.executeUpdate();

            pstmt.setInt(1, orderID);

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    @Override
    public Order convertReaderToObject(ResultSet res) throws SQLException, ParseException {
        return null;
    }

    public Map<Pair<Integer, Integer>, Integer> getItemsByOrderID(int orderID) {
        Map<Pair<Integer, Integer>, Integer> map = new HashMap();
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBHandler.ITEM_ORDERS + " WHERE " + ORDER_ID + " = " + orderID + ";");

            while (rs.next()) {
                map.put(new Pair<Integer, Integer>(rs.getInt(PRODUCT_ID), rs.getInt(SUPPLIER_ID)), rs.getInt(AMOUNT));
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public int getItemBuyPrice(int orderID, int productID) {
        int price = -1;
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBHandler.ITEM_ORDERS + " WHERE " + ORDER_ID + " = " + orderID + " AND " + PRODUCT_ID + " = " + productID + ";");
            if (rs.next()) {
                price = rs.getInt(BUY_PRICE);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return price;
    }


    public boolean UpdateAmount(Map.Entry<Pair<Integer, Integer>, Integer> entry) {
        boolean res = true;
        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? WHERE {2} = ? AND {3} = ? "
                , _tableName, AMOUNT, PRODUCT_ID, ORDER_ID
        );
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            //Connection connection=open
            //pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, entry.getValue());
            pstmt.setInt(2, entry.getKey().getKey());
            pstmt.setInt(3, entry.getKey().getValue());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }


    public int getOrderTotalPriceById(int orderID) {
        int price = -1;
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBHandler.ITEM_ORDERS + " WHERE " + ORDER_ID + " = " + orderID + ";");
            price = 0;
            while (rs.next()) {
                price += (rs.getInt(BUY_PRICE) * rs.getInt(AMOUNT));
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return price;
    }
}
