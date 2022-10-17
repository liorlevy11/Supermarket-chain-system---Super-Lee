package DataAccessLayer.Dal;

import BussinessLayer.Objects.Sale;

import java.sql.*;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class SaleDAO extends DAO {
    private static final String ID = "ID";
    private static final String START_DATE = "START_DATE";
    private static final String END_DATE = "END_DATE";
    private static final String DISCOUNT = "DISCOUNT";
    private HashMap<Integer, Sale> salesHashMap;
    private static SaleDAO instance = null;

    private SaleDAO() {
        super("SALES");
        salesHashMap = new HashMap<>();
    }

    public static SaleDAO getInstance() {
        if (instance == null) {
            instance = new SaleDAO();
        }
        return instance;
    }

    public void resetCache(){
        salesHashMap.clear();
    }

    @Override
    public boolean Insert(Object SaleObj) {
        Sale sale = (Sale) SaleObj;
        boolean res = true;
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4}) VALUES(?, ?, ?, ?) "
                , _tableName, ID, START_DATE, END_DATE, DISCOUNT
        );
        try (
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            //Connection connection=open
            //pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, sale.getSaleID());
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            pstmt.setString(2,df.format(sale.getStartDate()) );
            pstmt.setString(3,df.format(sale.getEndDate()));
            pstmt.setDouble(4, sale.getDiscount());
            pstmt.executeUpdate();
            if (!salesHashMap.containsKey(sale.getSaleID()))
                salesHashMap.put(sale.getSaleID(), sale);


        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }


    public boolean DeleteByID(int categoryid) {
        boolean res = true;
        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = ?"
                , _tableName, ID);

        try (
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, categoryid);
            pstmt.executeUpdate();
            if (salesHashMap.containsKey(categoryid)) {
                salesHashMap.remove(categoryid);
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
    public boolean Delete(Object SaleObj) {
        Sale sale = (Sale) SaleObj;
        boolean res = true;
        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = ? AND {2} = ? AND {3} = ? AND {4} = ?"
                , _tableName, ID, START_DATE, END_DATE, DISCOUNT);

        try (
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
//            Connection connection=open
//            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, sale.getSaleID());
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            pstmt.setString(2,df.format(sale.getStartDate()) );
            pstmt.setString(3,df.format(sale.getEndDate()));
            pstmt.setDouble(4, sale.getDiscount());
            pstmt.executeUpdate();
            if (salesHashMap.containsKey(sale.getSaleID()))
                salesHashMap.remove(sale.getSaleID());


        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public List<Sale> selectAllSales() {
        List<Sale> list = (List<Sale>) (List<?>) Select();
        return list;
    }

    public Sale convertReaderToObject(ResultSet rs) throws SQLException {
        Sale sale = null;
        Date StartDate;
        StartDate = Date.valueOf(rs.getString(2));
        Date EndDate;
        EndDate = Date.valueOf(rs.getString(3));
        sale = new Sale(rs.getInt(1), StartDate, EndDate, rs.getDouble(4));
        if (!salesHashMap.containsKey(sale.getSaleID())) {
            salesHashMap.putIfAbsent(sale.getSaleID(), sale);
        }
        return sale;
    }

    public Sale getSaleById(int id) {
        if (salesHashMap.containsKey(id))
            return salesHashMap.get(id);
        Sale sale = null;
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBHandler.SALES + " WHERE " + ID + " = " + id + ";");

            if (rs.next()) {
                Date StartDate;
                StartDate = Date.valueOf(rs.getString(START_DATE));
                Date EndDate;
                EndDate = Date.valueOf(rs.getString(END_DATE));
                int sId = rs.getInt(ID);
                double sDiscount = rs.getDouble(DISCOUNT);
                sale = new Sale(sId, StartDate, EndDate, sDiscount);
                salesHashMap.put(id, sale);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sale;
    }

    public List<Sale> getAllSales() {
        List<Sale> saleList = new LinkedList<>();
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBHandler.SALES + ";");
            while (rs.next()) {
                Date StartDate;
                StartDate = Date.valueOf(rs.getString(START_DATE));
                Date EndDate;
                EndDate = Date.valueOf(rs.getString(END_DATE));
                int sId = rs.getInt(ID);
                double sDiscount = rs.getDouble(DISCOUNT);


                Sale sale = new Sale(sId, StartDate, EndDate, sDiscount);
                saleList.add(sale);
                if (!salesHashMap.containsKey(sId))
                    salesHashMap.put(sId, sale);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return saleList;
    }

    public static int getMaxID() {
        int MaxId = 0;
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBHandler.SALES + ";");
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

    public static int getNumOfSales() {
        int counter = 0;
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBHandler.SALES + ";");
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
