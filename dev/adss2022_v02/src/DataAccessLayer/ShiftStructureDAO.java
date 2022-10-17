package DataAccessLayer;
import BussinessLayer.Objects.ShiftType;

import java.sql.*;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.*;


public class ShiftStructureDAO extends  DAO {
    private static final String TableName = "ShiftStructure";
    public static final String ShiftTypeColumnName = "ShiftType";
    public static final String ShiftDateColumnName = "ShiftDate";
    public static final String JobTypeColumnName = "JobType";
    public static final String NumOfEmployeeColumnName = "NumOfEmployee";
    private HashMap<List<String>, HashMap<String, Integer>> shiftStucturesCache;

    public ShiftStructureDAO() {
        super(TableName);
        shiftStucturesCache = new HashMap<>();
    }


    public boolean Insert(String shiftType, String date, String jobType, int numOfEmployee) {
        boolean res = true;
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4}) VALUES(?, ?, ?, ?) "
                , _tableName, ShiftTypeColumnName, ShiftDateColumnName, JobTypeColumnName, NumOfEmployeeColumnName
        );

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            //Connection connection=open
            //pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, shiftType);
            pstmt.setString(2, date);
            pstmt.setString(3, jobType);
            pstmt.setInt(4, numOfEmployee);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public boolean Delete(String shiftType, String date, String jobType) {
        boolean res = true;
        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = ? AND {2} = ? AND {3} = ? "
                , _tableName, ShiftTypeColumnName, ShiftDateColumnName, JobTypeColumnName);


        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
//            Connection connection=open
//            pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, shiftType);
            pstmt.setString(2, date);
            pstmt.setString(3, jobType);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }


    public boolean Delete(String shiftType, String date) {
        boolean res = true;
        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = ? AND {2} = ?"
                , _tableName, ShiftTypeColumnName, ShiftDateColumnName, JobTypeColumnName);


        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, shiftType);
            pstmt.setString(2, date);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }


    public Map<String, Integer> getShiftStructure(LocalDate date, ShiftType shiftType) {
        Map<String, Integer> map = new HashMap<>(); //ALL CONSTRAINT OF EMPLOYEE (ID)
        /// keys is for tables that have more that one key
        String sql = MessageFormat.format("SELECT {0},{1} From {2} WHERE {3} = ? AND {4} = ?",
                JobTypeColumnName, NumOfEmployeeColumnName, _tableName, ShiftTypeColumnName, ShiftDateColumnName);
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(2, date.format(formatters));
            pstmt.setString(1, shiftType.toString());
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                // Fetch each row from the result set
                map.put(resultSet.getString(1), resultSet.getInt(2));
            }
        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
        }
        return map;
    }

    public void setNumOfEmployeeColumnName(String shiftType, String date, String jobType, int numOfEmployee) {
        Update(NumOfEmployeeColumnName, numOfEmployee, makeList(ShiftTypeColumnName, ShiftDateColumnName, JobTypeColumnName), makeList(shiftType, date, jobType));
    }

    @Override
    public boolean Insert(Object obj) {
        return false;
    }

    @Override
    public boolean Delete(Object obj) {
        return false;
    }

    @Override
    public HashMap<String, Integer> convertReaderToObject(ResultSet rs) throws SQLException {
        HashMap<String, Integer> shiftStructure = new HashMap<>();
        shiftStructure.put(rs.getString(3), rs.getInt(4));
        return shiftStructure;
    }

    public void resetCache() {
        shiftStucturesCache.clear();
    }
}