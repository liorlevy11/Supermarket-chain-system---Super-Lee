package DataAccessLayer;


import BussinessLayer.Objects.Employee;
import BussinessLayer.Objects.ShiftType;

import java.sql.*;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.*;

public class EmployeeShiftDAO extends DAO {
    private static String TableName = "EmployeeShift";
    //table column names
    public static final String ShiftTypeColumnName = "ShiftType";
    public static final String ShiftDateColumnName = "ShiftDate";
    public static final String EmployeeIDColumnName = "EmployeeID";
    private Map<Integer, HashMap<String, List<String>>> EmployeeShiftsCache;//<ID,<date,List<shiftType>>

    public EmployeeShiftDAO() {
        super(TableName);
        EmployeeShiftsCache = new HashMap<>();
    }


    @Override
    public boolean Insert(Object employeeObj) {
        boolean res = true;
        Employee employee = (Employee) employeeObj;
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}) VALUES(?, ?, ?) "
                , _tableName, ShiftTypeColumnName, ShiftDateColumnName, EmployeeIDColumnName
        );

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (LocalDate l : employee.getConstraint().keySet()) {
                for (ShiftType s : employee.getConstraint().get(l)) {
                    String shiftType = s.toString();
                    String date = l.format(formatters);
                    System.out.println("date in employee shift" + date);
                    pstmt.setString(1, shiftType);
                    pstmt.setString(2, date);
                    pstmt.setInt(3, employee.getId());
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


    public boolean InsertOne(int id, String shiftType, String date) {
        boolean res = true;
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}) VALUES(?, ?, ?) "
                , _tableName, ShiftTypeColumnName, ShiftDateColumnName, EmployeeIDColumnName
        );
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, shiftType);
            pstmt.setString(2, date);
            pstmt.setInt(3, id);
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
                , _tableName, ShiftTypeColumnName, ShiftDateColumnName, EmployeeIDColumnName);

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (LocalDate l : employee.getConstraint().keySet()) {
                for (ShiftType s : employee.getConstraint().get(l)) {
                    String shiftType = s.toString();
                    String date = l.toString();
                    pstmt.setString(1, shiftType);
                    pstmt.setString(2, date);
                    pstmt.setInt(3, employee.getId());

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


    public boolean DeleteOne(int id, String shiftType, String date) {
        boolean res = true;

        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = ? AND {2} = ? AND {3} = ?"
                , _tableName, ShiftTypeColumnName, ShiftDateColumnName, EmployeeIDColumnName);

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, shiftType);
            pstmt.setString(2, date);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }


    public boolean DeleteShift(String shiftType, String date) {
        boolean res = true;

        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = ? AND {2} = ?"
                , _tableName, ShiftTypeColumnName, ShiftDateColumnName);

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, shiftType);
            pstmt.setString(2, date);

            pstmt.executeUpdate();
            for (int id : EmployeeShiftsCache.keySet()
            ) {
                if (EmployeeShiftsCache.get(id).get(date) != null && EmployeeShiftsCache.get(id).get(date).contains(shiftType)) {
                    EmployeeShiftsCache.get(id).get(date).remove(shiftType);
                    if (EmployeeShiftsCache.get(id).get(date).isEmpty()) {
                        EmployeeShiftsCache.get(id).remove(date);
                        if (EmployeeShiftsCache.get(id).keySet().isEmpty()) ;
                        EmployeeShiftsCache.remove(id);
                    }
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

    @Override
    public Map<String, List<String>> convertReaderToObject(ResultSet rs) throws SQLException {
        if (!EmployeeShiftsCache.containsKey(rs.getInt(3)))
            EmployeeShiftsCache.put(rs.getInt(3), new HashMap<>());
        if (EmployeeShiftsCache.get(rs.getInt(3)).get(rs.getString(2)) == null) {
            EmployeeShiftsCache.get(rs.getInt(3)).put(rs.getString(2), new LinkedList<>());
        }
        EmployeeShiftsCache.get(rs.getInt(3)).get(rs.getString(2)).add(rs.getString(3));//<ID,<date,List<shiftType>>
        return EmployeeShiftsCache.get(rs.getInt(3));
    }

    public Map<LocalDate, List<String>> getEmployeeShiftsById(int id) {
        Map<String, List<String>> employeeShiftFromDB = SelectEmployeeShift(id);

        //convertStringToLocalDate
        Map<LocalDate, List<String>> employeeShifts = new HashMap<>();
        for (String s : employeeShiftFromDB.keySet()) {
            employeeShifts.put(parseLocalDate(s), employeeShiftFromDB.get(s));
        }
        return employeeShifts;
    }

    protected Map<String, List<String>> SelectEmployeeShift(int id) {
        Map<String, List<String>> map = new HashMap<>(); //ALL CONSTRAINT OF EMPLOYEE (ID)
        /// keys is for tables that have more that one key
        String sql = MessageFormat.format("SELECT {0},{1} From {2} WHERE {3} = ?",
                ShiftTypeColumnName, ShiftDateColumnName, _tableName, EmployeeIDColumnName);
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                // Fetch each row from the result set
                if (!map.containsKey(resultSet.getString(2))) //INSERT ALL Shifts TO MAP
                    map.put(resultSet.getString(2), new ArrayList<>());
                map.get(resultSet.getString(2)).add(resultSet.getString(1));
            }

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
        }
        return map;
    }

    //    private Map<JobType,LinkedList<Integer>> employeesID;
    public List<Integer> getEmployeeIDInShift(String shiftType, String date) {
        List<Integer> list = new ArrayList<>();
        /// keys is for tables that have more that one key
        String sql = MessageFormat.format("SELECT {0} From {1} WHERE {2} = ? AND {3} = ?" ,
                EmployeeIDColumnName, _tableName,ShiftTypeColumnName,ShiftDateColumnName);
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, shiftType);
            pstmt.setString(2, date);
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                // Fetch each row from the result set
                list.add(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
        }
        return list;
    }

    public void resetCache() {
        EmployeeShiftsCache.clear();
    }
}




