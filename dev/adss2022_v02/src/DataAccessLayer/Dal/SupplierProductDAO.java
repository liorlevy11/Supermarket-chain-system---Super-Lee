package DataAccessLayer.Dal;


import BussinessLayer.Objects.SupplierProduct;
import javafx.util.Pair;

import java.sql.*;
import java.text.MessageFormat;
import java.util.*;

public class SupplierProductDAO extends DAO {
    private static final String PRODUCT_ID = "PRODUCT_ID";
    private static final String SUPPLIER_ID = "SUPPLIER_ID";
    private static final String NAME = "NAME";
    private static final String CATALOG_NUMBER = "CATALOG_NUMBER";
    private static final String PRICE = "PRICE";
    private static final String DISCOUNT_BY_AMOUNT = "DISCOUNT_BY_AMOUNT";
    private HashMap<Pair<Integer, Integer>, SupplierProduct> supplierProductHashMap; // prodID,suppID - supplier product
    private static SupplierProductDAO instance = null;

    private SupplierProductDAO() {
        super("SUPPLIER_PRODUCTS");
        supplierProductHashMap = new HashMap<>();
    }

    public static SupplierProductDAO getInstance() {
        if (instance == null)
            instance = new SupplierProductDAO();
        return instance;
    }

    public void resetCache() {
        supplierProductHashMap.clear();
    }
    //table column names

    @Override
    public boolean Insert(Object productObj) {
        SupplierProduct sProduct = (SupplierProduct) productObj;
        boolean res = true;
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4}, {5}, {6}) VALUES(?, ?, ?, ?, ?, ?) "
                , _tableName, PRODUCT_ID, SUPPLIER_ID, NAME, CATALOG_NUMBER, PRICE, DISCOUNT_BY_AMOUNT
        );
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            //Connection connection=open
            //pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, sProduct.getProductID());
            pstmt.setInt(2, sProduct.getSupplierID());
            pstmt.setString(3, sProduct.getName());
            pstmt.setString(4, sProduct.getCatalogNumber());
            pstmt.setDouble(5, sProduct.getPrice());
            pstmt.setString(6, sProduct.discountsToString());
            pstmt.executeUpdate();
            if (!isInProductsMap(sProduct))
                supplierProductHashMap.put(new Pair<>(sProduct.getProductID(), sProduct.getSupplierID()), sProduct);

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    private boolean isInProductsMap(SupplierProduct sProduct) {
        for (Map.Entry<Pair<Integer, Integer>, SupplierProduct> entry : supplierProductHashMap.entrySet()) {
            if (entry.getKey().getKey() == sProduct.getProductID() && entry.getKey().getValue() == sProduct.getSupplierID())
                return true;
        }
        return false;
    }

    @Override
    public boolean Delete(Object productObj) {
        SupplierProduct sProduct = (SupplierProduct) productObj;
        boolean res = true;
        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = ? AND {2} = ?"
                , _tableName, PRODUCT_ID, SUPPLIER_ID);

        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
//            Connection connection=open
//            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, sProduct.getProductID());
            pstmt.setInt(2, sProduct.getSupplierID());
            pstmt.executeUpdate();
            for (Map.Entry<Pair<Integer, Integer>, SupplierProduct> entry : supplierProductHashMap.entrySet()) {
                if (entry.getKey().getKey() == sProduct.getProductID() && entry.getKey().getValue() == sProduct.getSupplierID()) {
                    supplierProductHashMap.remove(entry.getKey());
                    break;
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


    public List<SupplierProduct> selectAllProducts() {
        List<SupplierProduct> list = (List<SupplierProduct>) (List<?>) Select();
        return list;
    }

    @Override
    public SupplierProduct convertReaderToObject(ResultSet rs) throws SQLException {
        int ProID = rs.getInt(PRODUCT_ID);
        int SupplierID = rs.getInt(SUPPLIER_ID);
        String Name = rs.getString(NAME);
        String CatNum = rs.getString(CATALOG_NUMBER);
        double price = rs.getDouble(PRICE);
        String DisByAmount = rs.getString(DISCOUNT_BY_AMOUNT);
        NavigableMap<Integer, Double> discountByAmount = new TreeMap<>();
        String[] pairs = DisByAmount.split(",");
        for (int i = 0; i < pairs.length - 1; i++) {
            String[] pair = pairs[i].split(":");
            discountByAmount.put(Integer.parseInt(pair[0]), Double.parseDouble(pair[1]));
        }
        SupplierProduct supplierProduct = new SupplierProduct(ProID, SupplierID, Name, price, CatNum, discountByAmount);
        return supplierProduct;

    }

    public List<SupplierProduct> getAllSupplierProductsByProductId(int productID) {
        List<SupplierProduct> productList = new LinkedList<>();
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBHandler.SUPPLIER_PRODUCT + " WHERE " + PRODUCT_ID +
                    " = " + productID + ";");
            while (rs.next()) {
                SupplierProduct sProduct = convertReaderToObject(rs);
                productList.add(sProduct);
                if (!isInProductsMap(sProduct))
                    supplierProductHashMap.put(new Pair<>(sProduct.getProductID(), sProduct.getSupplierID()), sProduct);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productList;
    }

    public SupplierProduct getSupplierProductById(int PId, int SuppId) {
        for (Map.Entry<Pair<Integer, Integer>, SupplierProduct> entry : supplierProductHashMap.entrySet()) {
            if (entry.getKey().getKey() == PId && entry.getKey().getValue() == SuppId)
                return supplierProductHashMap.get(entry.getKey());
        }
        SupplierProduct sProduct = null;
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBHandler.SUPPLIER_PRODUCT + " WHERE " + PRODUCT_ID +
                    " = " + PId + " AND " + SUPPLIER_ID + " = " + SuppId + ";");
            if (rs.next()) {
                sProduct = convertReaderToObject(rs);
                if (!isInProductsMap(sProduct))
                    supplierProductHashMap.put(new Pair<>(sProduct.getProductID(), sProduct.getSupplierID()), sProduct);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sProduct;
    }


    public List<SupplierProduct> getAllSupplierProducts() {
        List<SupplierProduct> productList = new LinkedList<>();
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBHandler.SUPPLIER_PRODUCT + ";");
            while (rs.next()) {
                SupplierProduct sProduct = convertReaderToObject(rs);
                productList.add(sProduct);
                if (!isInProductsMap(sProduct))
                    supplierProductHashMap.put(new Pair<>(sProduct.getProductID(), sProduct.getSupplierID()), sProduct);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productList;
    }


    public List<SupplierProduct> getProductsBySupplier(int supplierID) {
        List<SupplierProduct> prod = new LinkedList<>();
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBHandler.SUPPLIER_PRODUCT + " WHERE " + SUPPLIER_ID + " = " + supplierID + ";");
            while (rs.next()) {
                SupplierProduct supplierProduct = convertReaderToObject(rs);
                prod.add(supplierProduct);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prod;
    }

    public Integer getSupplierByProductId(int productID) {
        Integer supplierID = -1;
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBHandler.SUPPLIER_PRODUCT + " WHERE " + PRODUCT_ID + " = " + productID + ";");
            while (rs.next()) {
                // Fetch each row from the result set
                supplierID = rs.getInt(2);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return supplierID;
    }

    public static int getMaxID() {
        int MaxId = 0;
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBHandler.SUPPLIER_PRODUCT + ";");
            while (rs.next()) {
                int cId = rs.getInt(PRODUCT_ID);
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

    public int CountProducts() {
        int counter = 0;
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBHandler.SUPPLIER_PRODUCT + ";");
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


}
