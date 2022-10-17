package DataAccessLayer.Dal;

import BussinessLayer.Objects.Product;

import java.sql.*;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class ProductDAO extends DAO {
    private static final String PRODUCT_ID = "PRODUCT_ID";
    private static final String NAME = "NAME";
    private static final String MANUFACTURER = "MANUFACTURER";
    private static final String SALE_ID = "SALE_ID";
    private static final String CATEGORY_ID = "CATEGORY_ID";
    private static final String SUB_CATEGORY_ID = "SUB_CATEGORY_ID";
    private static final String SUB_SUB_CATEGORY_ID = "SUB_SUB_CATEGORY_ID";
    private static final String SELL_PRICE = "SELL_PRICE";
    private static final String MIN_QUANTITY = "MIN_QUANTITY";
    private static final String WEIGHT = "Weight";
    private HashMap<Integer, Product> productsHashMap;
    private static ProductDAO instance = null;

    public static ProductDAO getInstance(){
        if(instance == null)
            instance = new ProductDAO();
        return instance;
    }

    private ProductDAO() {
        super("PRODUCTS");
        productsHashMap = new HashMap<>();
    }

    public void resetCache(){
        productsHashMap.clear();
    }

    //table column names

    @Override
    public boolean Insert(Object productObj) {
        Product product = (Product) productObj;
        boolean res = true;
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4}, {5}, {6}, {7}, {8}, {9},{10}) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?,?) "
                , _tableName, PRODUCT_ID, NAME, MANUFACTURER, SALE_ID, CATEGORY_ID, SUB_CATEGORY_ID, SUB_SUB_CATEGORY_ID, SELL_PRICE, MIN_QUANTITY,WEIGHT
        );
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            //Connection connection=open
            //pstmt = connection.prepareStatement(sql);

            pstmt.setInt(1, product.getProductId());
            pstmt.setString(2, product.getName());
            pstmt.setString(3, product.getManufacturer());
            pstmt.setInt(4, product.getSaleId());
            pstmt.setInt(5, product.getCategory().get("main"));
            pstmt.setInt(6, product.getCategory().get("sub"));
            pstmt.setInt(7, product.getCategory().get("subSub"));
            pstmt.setDouble(8, product.getSellPrice());
            pstmt.setInt(9, product.getMinQuantity());
            pstmt.setDouble(10,product.getWeight());
            pstmt.executeUpdate();
            //   EmployeeConstraintsDAO.Insert


        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    @Override
    public boolean Delete(Object productObj) {
        Product product = (Product) productObj;
        boolean res = true;
        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = ?"
                , _tableName, PRODUCT_ID);

        try (
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
//            Connection connection=open
//            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, product.getProductId());
            pstmt.executeUpdate();
            if(productsHashMap.containsKey(product.getProductId()))
                productsHashMap.remove(product.getProductId());


        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public boolean DeleteAll() {
        boolean res = true;
        String sql = MessageFormat.format("DELETE FROM {0}"
                , _tableName);

        try (
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
//            Connection connection=open
//            pstmt = connection.prepareStatement(sql);
            pstmt.executeUpdate();


        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }


    public List<Product> selectAllProducts() {
        List<Product> list = (List<Product>) (List<?>) Select();
        return list;
    }

    @Override
    public Product convertReaderToObject(ResultSet rs) throws SQLException {
        int ProID = rs.getInt(PRODUCT_ID);
        String Name = rs.getString(NAME);
        String Mnu = rs.getString(MANUFACTURER);
        int SaleId = rs.getInt(SALE_ID);
        int CatID = rs.getInt(CATEGORY_ID);
        int SubId = rs.getInt(SUB_CATEGORY_ID);
        int SubSub = rs.getInt(SUB_SUB_CATEGORY_ID);
        double price = rs.getDouble(SELL_PRICE);
        int MinQuantity = rs.getInt(MIN_QUANTITY);
        double weight = rs.getInt(WEIGHT);

        Product product = new Product(ProID, Name,Mnu,MinQuantity,price, SaleId
                , CatID, SubId, SubSub,weight);
        return product;

    }
    public Product getProductByName(String productName) {
        Product product = null;
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBHandler.PRODUCTS + " WHERE " + NAME +
                    " = "+ productName +";");
            if (rs.next()) {
                product = convertReaderToObject(rs);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return product;
    }


    public Product getProductById(int PId) {
        Product product = null;
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBHandler.PRODUCTS + " WHERE " + PRODUCT_ID +
                    " = "+ PId +";");
            if (rs.next()) {
                int ProID = rs.getInt(PRODUCT_ID);
                String Name = rs.getString(NAME);
                String Mnu = rs.getString(MANUFACTURER);
                int SaleId = rs.getInt(SALE_ID);
                int CatID = rs.getInt(CATEGORY_ID);
                int SubId = rs.getInt(SUB_CATEGORY_ID);
                int SubSub = rs.getInt(SUB_SUB_CATEGORY_ID);
                double price = rs.getDouble(SELL_PRICE);
                int MinQuantity = rs.getInt(MIN_QUANTITY);
                double weight = rs.getInt(WEIGHT);
                product = new Product(ProID, Name,Mnu,MinQuantity,price, SaleId
                        , CatID, SubId,
                        SubSub,weight);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return product;
    }
    public List<Product> getAllProducts() {
        List<Product> prod = new LinkedList<>();
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBHandler.PRODUCTS + ";");
            while (rs.next()) {
                Product product = convertReaderToObject(rs);
                prod.add(product);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prod;
    }

    public List<Product> getAllProductsOfCategory(int CategoryID) {
        List<Product> prod = new LinkedList<>();
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBHandler.PRODUCTS + " WHERE " + CATEGORY_ID +
                    " = "+ CategoryID +";");
            while (rs.next()) {
                int ProID = rs.getInt(PRODUCT_ID);
                String Name = rs.getString(NAME);
                String Mnu = rs.getString(MANUFACTURER);
                int SaleId = rs.getInt(SALE_ID);
                int CatID = rs.getInt(CATEGORY_ID);
                int SubId = rs.getInt(SUB_CATEGORY_ID);
                int SubSub = rs.getInt(SUB_SUB_CATEGORY_ID);
                double price = rs.getDouble(SELL_PRICE);
                int MinQuantity = rs.getInt(MIN_QUANTITY);
                double weight = rs.getInt(WEIGHT);
                Product product = new Product(ProID, Name,Mnu,MinQuantity,price, SaleId
                        , CatID, SubId, SubSub,weight);
                prod.add(product);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prod;
    }

    public static int getMaxID(){
        int MaxId = 0;
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBHandler.PRODUCTS + ";");
            while (rs.next()) {
                int cId = rs.getInt(PRODUCT_ID);
                if(cId>MaxId){
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

    public static int CountProducts() {
        int counter = 0;
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBHandler.PRODUCTS + ";");
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
    public boolean UpdateProductCategory(int id,Integer catID, String Category_kind){
        boolean res = true;
        String catK = "";
        if(Category_kind.equals("main")){
            catK = CATEGORY_ID;
        }else if(Category_kind.equals("sub")){
            catK = SUB_CATEGORY_ID;
        }else{
            catK = SUB_SUB_CATEGORY_ID;
        }
        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? WHERE {2} = ? "
                , _tableName,catK,PRODUCT_ID);
        try (
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, catID);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }
    public boolean UpdateProductSale(int id,int SaleID){
        boolean res = true;
        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? WHERE {2} = ? "
                , _tableName,SALE_ID,PRODUCT_ID);
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, SaleID);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }
    public boolean UpdateProductWeight(int id,double weight){
        boolean res = true;
        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? WHERE {2} = ? "
                , _tableName,WEIGHT,PRODUCT_ID);
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, weight);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }


}
