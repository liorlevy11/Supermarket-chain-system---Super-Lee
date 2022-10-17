package DataAccessLayer.Dal;

import BussinessLayer.Objects.Item;

import java.sql.*;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class SuperItemDAO extends DAO {

    private static SuperItemDAO instance = null;
    private static final String ID = "ID";
    private static final String PRODUCT_ID = "PRODUCT_ID";
    private static final String ORDER_ID = "ORDER_ID";
    private static final String LOCATION = "LOCATION";
    private static final String STORAGE_SHELF = "STORAGE_OR_SHELF";
    private static final String EX_DATE = "EXP_DATE";
    private static final String BUY_PRICE = "BUY_PRICE";
    private static final String DEFECTIVE = "DEFECTIVE";
    private HashMap<Integer, Item> superItemsHashMap;

    private SuperItemDAO() {
        super("SUPERLEE_ITEMS");
        superItemsHashMap = new HashMap<>();
    }

    public static SuperItemDAO getInstance() {
        if (instance == null) {
            instance = new SuperItemDAO();
        }
        return instance;
    }

    public void resetCache(){
        superItemsHashMap.clear();
    }

    @Override
    public boolean Insert(Object ItemObj) {
        Item Itm = (Item) ItemObj;
        boolean res = true;
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4}, {5}, {6} ,{7},{8}) VALUES(?, ?, ?, ?, ?, ?, ?,?) "
                , _tableName, ID, PRODUCT_ID, ORDER_ID, LOCATION, BUY_PRICE,STORAGE_SHELF, EX_DATE, DEFECTIVE
        );
        try (
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            //Connection connection=open
            //pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, Itm.getId());
            pstmt.setInt(2, Itm.getProductid());
            pstmt.setInt(3, Itm.getOrderid());
            pstmt.setString(4, Itm.getLocation());
            pstmt.setDouble(5,Itm.getBuyPrice());
            pstmt.setString(6, Itm.getStorageShelf());
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            pstmt.setString(7, df.format(Itm.getExpressionDate()));
            pstmt.setBoolean(8, Itm.getDefective());
            pstmt.executeUpdate();
            if (!superItemsHashMap.containsKey(Itm.getId())) {
                superItemsHashMap.putIfAbsent(Itm.getId(), Itm);
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
    public boolean Delete(Object ItemObj) {
        Item Itm = (Item) ItemObj;
        boolean res = true;
        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = ?"
                , _tableName, ID);

        try (
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
//            Connection connection=open
//            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, Itm.getId());
            pstmt.executeUpdate();
            if (superItemsHashMap.containsKey(Itm.getId())) {
                superItemsHashMap.remove(Itm.getId());
            }
        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }
    public boolean deleteByProduct(Object productID) {
        Integer Itm = (Integer) productID;
        boolean res = true;
        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = ?"
                , _tableName, PRODUCT_ID);

        try (
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
//            Connection connection=open
//            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, Itm);
            pstmt.executeUpdate();
            List<Item> items = getAllSuperItemsOfProduct(Itm);
            for (Item item:items) {
                if (superItemsHashMap.containsKey(item.getId())) {
                    superItemsHashMap.remove(item.getId());
                    return true;
                }
            }

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public List<Item> selectAllSuperItems() {
        List<Item> list = (List<Item>) (List<?>) Select();
        return list;
    }

    public Item convertReaderToObject(ResultSet rs) throws SQLException {
        Item Itm = null;
        Date exp;
        exp = Date.valueOf(rs.getString(EX_DATE));
        Itm = new Item(rs.getInt(ID), rs.getInt(PRODUCT_ID), rs.getInt(ORDER_ID), rs.getString(LOCATION), rs.getDouble(BUY_PRICE),rs.getString(STORAGE_SHELF), exp,
                rs.getBoolean(DEFECTIVE));

        return Itm;
    }


    public Item getSuperItemByIdAndProductID(int id,int productID) {
        for(Integer key : this.superItemsHashMap.keySet()){
            if(key == id){
                Item item = this.superItemsHashMap.get(id);
                if(item.getProductid() == productID){
                    return item;
                }
            }
        }
        Item Itm = null;
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + _tableName + " WHERE " + ID + " = " + id + "AND" + PRODUCT_ID + "=" + productID + ";");

            if (rs.next()) {
                Itm = convertReaderToObject(rs);
                if (!superItemsHashMap.containsKey(Itm.getId())) {
                    superItemsHashMap.putIfAbsent(Itm.getId(), Itm);
                }
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Itm;
    }

    public boolean UpdateOrderID(int itemID,int newOrderID){
        boolean res = true;
        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? WHERE {2} = ? "
                , _tableName,ORDER_ID,ID);
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, newOrderID);
            pstmt.setInt(2, itemID);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public Item getSuperItemById(int id) {
        if (superItemsHashMap.containsKey(id))
            return superItemsHashMap.get(id);
        Item Itm = null;
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + _tableName + " WHERE " + ID + " = " + id + ";");

            if (rs.next()) {
                Itm = convertReaderToObject(rs);
                if (!superItemsHashMap.containsKey(Itm.getId())) {
                    superItemsHashMap.putIfAbsent(Itm.getId(), Itm);
                }
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Itm;
    }

    public List<Item> getAllSuperItems() {
        List<Item> ItmList = new LinkedList<>();
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + _tableName + ";");
            while (rs.next()) {
                Item Itm = convertReaderToObject(rs);
                ItmList.add(Itm);
                if (!superItemsHashMap.containsKey(Itm.getId())) {
                    superItemsHashMap.putIfAbsent(Itm.getId(), Itm);
                }
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ItmList;
    }

    public int getMaxID() {
        int MaxId = 0;
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + _tableName + ";");
            while (rs.next()) {
                int cId = rs.getInt(ID);
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

    public int CountSuperItems() {
        int counter = 0;
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + _tableName + ";");
            while (rs.next()) {
                counter++;
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return counter;
    }

    public boolean UpdateItemLocation(int id,int productID, String StOrSh, String Loc) {
        boolean res = true;
        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? , {2} = ? WHERE {3} = ? AND {4} =? "
                , _tableName, LOCATION, STORAGE_SHELF, ID,PRODUCT_ID);
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, Loc);
            pstmt.setString(2, StOrSh);
            pstmt.setInt(3, id);
            pstmt.setInt(4, productID);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }


    public List<Item> getAllSuperItemsOfProduct(int proID) {
        List<Item> ItmList = new LinkedList<>();
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + _tableName + ";");
            while (rs.next()) {
                Item Itm = convertReaderToObject(rs);
                if (Itm.getProductid() == proID) {
                    ItmList.add(Itm);
                }
                if (!superItemsHashMap.containsKey(Itm.getId())) {
                    superItemsHashMap.putIfAbsent(Itm.getId(), Itm);
                }
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ItmList;
    }

    public List<Item> getSuperItemByProIDAndOrdID(int ProductID, int orderID) {
        List<Item> ItmLst = new LinkedList<>();
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + _tableName + " WHERE " + PRODUCT_ID + " = " + ProductID + " AND " + ORDER_ID + " = " + orderID + ";");

            if (rs.next()) {
                Item Itm = convertReaderToObject(rs);
                ItmLst.add(Itm);
                if (!superItemsHashMap.containsKey(Itm.getId())) {
                    superItemsHashMap.putIfAbsent(Itm.getId(), Itm);
                }
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ItmLst;
    }

    public List<Item> getSuperItemsByOrderID(int orderID) {
        List<Item> ItmLst = new LinkedList<>();
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + _tableName + " WHERE "  + ORDER_ID + " = " + orderID + ";");

            while (rs.next()) {
                Item Itm = convertReaderToObject(rs);
                ItmLst.add(Itm);
                if (!superItemsHashMap.containsKey(Itm.getId())) {
                    superItemsHashMap.putIfAbsent(Itm.getId(), Itm);
                }
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ItmLst;
    }
}
