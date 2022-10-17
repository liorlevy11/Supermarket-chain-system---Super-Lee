package DataAccessLayer.Dal;


import BussinessLayer.Objects.Product;
import BussinessLayer.Objects.*;

import java.sql.*;
import java.text.MessageFormat;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class SupplierDAO extends DAO {

    public static final String IDColumnName = "ID";
    public static final String NameColumnName = "NAME";
    public static final String AddressColumnName = "ADDRESS";
    public static final String PaymentMethodColumnName = "PAYMENT_METHOD";
    public static final String BankAccountColumnName = "BANK_ACCOUNT";
    public static final String SupplyMethodColumnName = "SUPPLY_METHOD";
    public static final String DeliveryDaysColumnName = "DELIVERY_DAYS";
    private HashMap<Integer, Supplier> supplierHashMap;
    private static SupplierDAO instance = null;

    public static SupplierDAO getInstance() {
        if (instance == null)
            instance = new SupplierDAO();
        return instance;
    }

    private static String TableName = "Suppliers";

    private SupplierDAO() {
        super(TableName);
        supplierHashMap = new HashMap<>();
    }

    public void resetCache(){
        supplierHashMap.clear();
    }

    @Override
    public boolean Insert(Object supplierObj) {
        Supplier supplier = (Supplier) supplierObj;
        SupplierCard supplierCard = supplier.getSupplierCard();
        Agreement agreement = supplier.getAgreement();
        boolean res = true;
        List<DayOfWeek> daysList = agreement.getDeliveryDays();
        List<String> daysStringList = daysList.stream().map(dayOfWeek -> dayOfWeek.name()).collect(Collectors.toList());
        String daysString = daysStringList.toString();
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4}, {5},{6},{7}) VALUES(?, ?, ?, ?, ?, ?,?) "
                , _tableName, IDColumnName, NameColumnName, AddressColumnName, PaymentMethodColumnName, BankAccountColumnName, SupplyMethodColumnName, DeliveryDaysColumnName
        );

        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            //Connection connection=open
            //pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, supplierCard.getId());
            pstmt.setString(2, supplierCard.getName());
            pstmt.setString(3, supplierCard.getAddress());
            pstmt.setString(4, supplierCard.getPaymentMethod().toString());
            pstmt.setString(5, supplierCard.getBankAccount());
            pstmt.setString(6, agreement.getSupplyMethod().toString());
            pstmt.setString(7, (daysString));
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
    public boolean Delete(Object supplierObj) {
        Supplier supplier = (Supplier) supplierObj;
        boolean res = true;
        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = ? "
                , _tableName, IDColumnName);

        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, supplier.getSupplierCard().getId());
            pstmt.executeUpdate();
            if (supplierHashMap.containsKey(supplier.getSupplierCard().getId()))
                supplierHashMap.remove(supplier.getSupplierCard().getId());

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }


    public List<Supplier> SelectAllSuppliers() {
        List<Supplier> suppliers = new LinkedList<>();
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBHandler.SUPPLIERS + ";");
            while (rs.next()) {
                Supplier supplier = convertReaderToObject(rs);
                suppliers.add(supplier);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return suppliers;
    }

    @Override
    public Supplier convertReaderToObject(ResultSet rs) throws SQLException, ParseException {
        Supplier supplier = null;
        int supplierID = rs.getInt(IDColumnName);
        String name = rs.getString(NameColumnName);
        String bankAccount = rs.getString(BankAccountColumnName);
        PaymentMethod paymentMethod = PaymentMethod.valueOf(rs.getString(PaymentMethodColumnName));
        String address = rs.getString(AddressColumnName);
        SupplyMethod supplyMethod = SupplyMethod.valueOf(rs.getString(SupplyMethodColumnName));

        String deliveryDays = rs.getString(DeliveryDaysColumnName);
        List<DayOfWeek> daysOfWeekList = new ArrayList<>();
        if (supplyMethod.equals(SupplyMethod.byDays) && (!deliveryDays.equals("[]"))) {
            String deliveryDays2 = deliveryDays.substring(1, deliveryDays.length() - 1);
            String[] daysArray = deliveryDays2.split(",");
            for (String dayS : daysArray) {
                String day = dayS.trim();
                daysOfWeekList.add(DayOfWeek.valueOf(day));
            }
        }

        List<SupplierProduct> supplierProducts = SupplierProductDAO.getInstance().getProductsBySupplier(supplierID);
        List<String> supplierProductsListIDs = supplierProducts.stream().map(supplierProduct -> supplierProduct.getCatalogNumber()).collect(Collectors.toList());

        SupplierCard supplierCard = new SupplierCard(supplierID, name, address, bankAccount, paymentMethod);
        Agreement agreement = new Agreement(name, daysOfWeekList, supplyMethod, supplierProductsListIDs);

        supplier = new Supplier(supplierCard, agreement);

        return supplier;
    }

    public List<String> getSuppliersManufactures(int supplierID, String bankAccount) {
        List<SupplierProduct> supplierProducts = SupplierProductDAO.getInstance().getProductsBySupplier(supplierID);
        List<String> manufactures = new ArrayList<>();
        for (SupplierProduct supplierProduct : supplierProducts) {
            Product product = ProductDAO.getInstance().getProductById(supplierProduct.getProductID());
            manufactures.add(product.getManufacturer());
        }
        manufactures = manufactures.stream().distinct().collect(Collectors.toList());
        return manufactures;
    }

    public List<String> getSuppliersCategories(int supplierID, String bankAccount) {
        List<SupplierProduct> supplierProducts = SupplierProductDAO.getInstance().getProductsBySupplier(supplierID);
        List<String> categories = new ArrayList<>();
        for (SupplierProduct supplierProduct : supplierProducts) {
            Product product = ProductDAO.getInstance().getProductById(supplierProduct.getProductID());
            categories.addAll(product.getCategory().keySet());
        }
        categories = categories.stream().distinct().collect(Collectors.toList());
        return categories;
    }

    public boolean UpdateBankAccount(int supplierID, String bankAccount) {
        boolean res = true;

        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? WHERE {2} = ? "
                , _tableName, BankAccountColumnName, IDColumnName);
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, bankAccount);
            pstmt.setInt(2, supplierID);
            pstmt.executeUpdate();
            Supplier oldSupplier = supplierHashMap.get(supplierID);
            if (oldSupplier != null)
                supplierHashMap.replace(supplierID, oldSupplier, getSupplier(supplierID));

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public boolean UpdateSupplyMethod(int supplierID, String supplyMethod, String days) {
        boolean res = true;

        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? , {2} = ? WHERE {3} = ? "
                , _tableName, SupplyMethodColumnName, DeliveryDaysColumnName, IDColumnName);
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, supplyMethod);
            pstmt.setString(2, days);
            pstmt.setInt(3, supplierID);
            pstmt.executeUpdate();
            Supplier oldSupplier = supplierHashMap.get(supplierID);
            if (oldSupplier != null)
                supplierHashMap.replace(supplierID, oldSupplier, getSupplier(supplierID));

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public boolean UpdatePaymentMethod(int supplierID, String paymentMethod) {
        boolean res = true;
        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? WHERE {2} = ? "
                , _tableName, PaymentMethodColumnName, IDColumnName);
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, paymentMethod);
            pstmt.setInt(2, supplierID);
            pstmt.executeUpdate();
            Supplier oldSupplier = supplierHashMap.get(supplierID);
            if (oldSupplier != null)
                supplierHashMap.replace(supplierID, oldSupplier, getSupplier(supplierID));
        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public boolean UpdateAddress(int supplierID, String address) {
        boolean res = true;
        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? WHERE {2} = ? "
                , _tableName, AddressColumnName, IDColumnName);
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, address);
            pstmt.setInt(2, supplierID);
            pstmt.executeUpdate();
            Supplier oldSupplier = supplierHashMap.get(supplierID);
            if (oldSupplier != null)
                supplierHashMap.replace(supplierID, oldSupplier, getSupplier(supplierID));
        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public Supplier getSupplier(int supplierID) {
        if (supplierHashMap.containsKey(supplierID))
            return supplierHashMap.get(supplierID);
        Supplier supplier = null;
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBHandler.SUPPLIERS + " WHERE " + IDColumnName + " = " + supplierID + ";");
            if (rs.next()) {
                supplier = convertReaderToObject(rs);
                supplierHashMap.put(supplierID, supplier);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return supplier;
    }

    public int getMaxID() {
        int MaxId = 0;
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + _tableName + ";");
            while (rs.next()) {
                int cId = rs.getInt(IDColumnName);
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
}
