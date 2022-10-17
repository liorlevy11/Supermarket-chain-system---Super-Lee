package DataAccessLayer;


import BussinessLayer.Objects.Employee;
import BussinessLayer.Objects.ShiftType;

import java.sql.*;
import java.text.MessageFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;

public class EmployeeConstraintsDAO extends DAO {
    //table column names
    private static String TableName = "EmployeeConstraints";
    public static final String EmployeeIDColumnName = "EmployeeID";
    public static final String ShiftTypeColumnName = "ShiftType";
    public static final String DateColumnName = "Date";
    private HashMap<Integer, HashMap<String, List<String>>> EmployeeConstraintsCache;//<ID,<date,List<shiftType>>

    public EmployeeConstraintsDAO() {
        super(TableName);
        EmployeeConstraintsCache = new HashMap<>();
    }

    @Override
    public boolean Insert(Object employeeObj) {
        Employee employee = (Employee) employeeObj;
        boolean res = true;
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}) VALUES(?, ?, ?) "
                , _tableName, EmployeeIDColumnName, ShiftTypeColumnName, DateColumnName
        );

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (LocalDate l : employee.getConstraint().keySet()) {
                for (ShiftType s : employee.getConstraint().get(l)) {
                    String shiftType = s.toString();
                    String date = l.toString();
                    pstmt.setInt(1, employee.getId());
                    pstmt.setString(2, shiftType);
                    pstmt.setString(3, date);

                    pstmt.executeUpdate();
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


    public boolean InsertEmployeeConstraint(int id, String shiftType, String date) {
        boolean res = true;
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}) VALUES(?, ?, ?) "
                , _tableName, EmployeeIDColumnName, ShiftTypeColumnName, DateColumnName
        );

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.setString(2, shiftType);
            pstmt.setString(3, date);
            addToHashMapFromInsert(id, shiftType, date);

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
    public boolean Delete(Object employeeObj) {
        boolean res = true;
        Employee employee = (Employee) employeeObj;
        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = ? AND {2} = ? AND {3} = ?"
                , _tableName, EmployeeIDColumnName, ShiftTypeColumnName, DateColumnName);
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            for (LocalDate l : employee.getConstraint().keySet()) {
                for (ShiftType s : employee.getConstraint().get(l)) {
                    String shiftType = s.toString();
                    String date = l.toString();
                    pstmt.setInt(1, employee.getId());
                    pstmt.setString(2, shiftType);
                    pstmt.setString(3, date);
                    pstmt.executeUpdate();
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

    public boolean DeleteEmployeeConstraint(int id, String shiftType, String date) {
        boolean res = true;
        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = ? AND {2} = ? AND {3} = ?"
                , _tableName, EmployeeIDColumnName, ShiftTypeColumnName, DateColumnName);
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.setString(2, shiftType);
            pstmt.setString(3, date);
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
    public Map<String, List<String>> convertReaderToObject(ResultSet rs) throws SQLException, ParseException {
        if (!EmployeeConstraintsCache.containsKey(rs.getInt(1)))
            EmployeeConstraintsCache.put(rs.getInt(1), new HashMap<>());
        if (EmployeeConstraintsCache.get(rs.getInt(1)).get(rs.getString(3)) == null) {
            EmployeeConstraintsCache.get(rs.getInt(1)).put(rs.getString(3), new LinkedList<>());
        }
        EmployeeConstraintsCache.get(rs.getInt(1)).get(rs.getString(3)).add(rs.getString(2));
        return EmployeeConstraintsCache.get(rs.getInt(1));//<ID,<date,List<shiftType>>
    }



    protected Map<String,List<String>> SelectEmployeeConstraint(int id) {
        Map<String,List<String>> map = new HashMap<>(); //ALL CONSTRAINT OF EMPLOYEE (ID)
        /// keys is for tables that have more that one key
        String sql = MessageFormat.format("SELECT {0},{1} From {2} WHERE {3} = ?",
                ShiftTypeColumnName, DateColumnName, _tableName, EmployeeIDColumnName);
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                // Fetch each row from the result set
                if(!map.containsKey(resultSet.getString(2))) //INSERT ALL CONSTRAINTS TO MAP
                    map.put(resultSet.getString(2),new ArrayList<>());
                map.get(resultSet.getString(2)).add(resultSet.getString(1));
            }

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
        }
        return map;
    }


    public Map<LocalDate, List<String>> getEmployeeConstraintsById(int id) {
        Map<String, List<String>> employeeConstraintsFromDB=SelectEmployeeConstraint(id);
        Map<LocalDate,List<String>> employeeConstraints=new HashMap<>();
        for(String s: employeeConstraintsFromDB.keySet()){
            employeeConstraints.put(parseLocalDate(s),employeeConstraintsFromDB.get(s));
        }
        return employeeConstraints;
    }

    public void updateHashMapFromGet(Map<String, List<String>> m, int id) {
        if (EmployeeConstraintsCache.get(id) == null) {
            EmployeeConstraintsCache.put(id, new HashMap<>());
        }
        for (String dateS : m.keySet()) {
            if (EmployeeConstraintsCache.get(id).get(dateS) == null) {
                EmployeeConstraintsCache.get(id).put(dateS, new LinkedList<>());
            }
            for (String shiftTypeS : m.get(dateS)) {
                if (!EmployeeConstraintsCache.get(id).get(dateS).contains(shiftTypeS))
                    EmployeeConstraintsCache.get(id).get(dateS).add(shiftTypeS);
            }
        }
    }


    public void addToHashMapFromInsert(int id, String shiftType, String date) {
        if (!EmployeeConstraintsCache.containsKey(id)) {
            EmployeeConstraintsCache.put(id, new HashMap<>());
        }
        if (EmployeeConstraintsCache.get(id).get(date) == null)
            EmployeeConstraintsCache.get(id).put(date, new LinkedList<>());
        EmployeeConstraintsCache.get(id).get(date).add(shiftType);
    }

    public void resetCache() {
        EmployeeConstraintsCache.clear();
    }
}
