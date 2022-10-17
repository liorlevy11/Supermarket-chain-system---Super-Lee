package DataAccessLayer;


import BussinessLayer.Objects.Employee;

import java.sql.*;
import java.text.MessageFormat;
import java.util.*;

public class EmployeeTrainingDAO extends DAO {
    private static String TableName = "EmployeeTraining";
    //table column names
    private static final String IDColumnName = "ID";
    private static final String TrainingNameColumnName = "TrainingName";
    private HashMap<Integer, List<String>> EmployeeTrainingCache;

    public EmployeeTrainingDAO() {
        super(TableName);
        EmployeeTrainingCache = new HashMap<>();
    }

    @Override
    public boolean Insert(Object employeeObj) {
        boolean res = true;
        Employee employee = (Employee) employeeObj;
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}) VALUES(?, ?) "
                , _tableName, IDColumnName, TrainingNameColumnName
        );

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (String s : employee.getTrainingBystringList()) {
                pstmt.setInt(1, employee.getId());
                pstmt.setString(2, s);
                pstmt.executeUpdate();


            }
        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }


    public boolean InsertEmployeeTraining(int id, String training) {
        boolean res = true;
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}) VALUES(?, ?) "
                , _tableName, IDColumnName, TrainingNameColumnName
        );

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.setString(2, training);
            pstmt.executeUpdate();
            //addToHashMapFromInsert(id, training);

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
        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = ? AND {2} = ?"
                , _tableName, IDColumnName, TrainingNameColumnName);

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            for (String s : employee.getTrainingBystringList()) {
                pstmt.setInt(1, employee.getId());
                pstmt.setString(2, s);
                pstmt.executeUpdate();
            }

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }


    public List<String> getEmployeeTraining(int id) {
        return SelectTrainingOfEmployee(id);
    }

    public List<String> SelectTrainingOfEmployee(int idEmployee){
        return SelectString(TrainingNameColumnName,makeList(IDColumnName),makeList(String.valueOf(idEmployee)));
    }


    @Override
    public List<String> convertReaderToObject(ResultSet rs) throws SQLException {
        if (!EmployeeTrainingCache.containsKey(rs.getInt(1)))
            EmployeeTrainingCache.put(rs.getInt(1), new LinkedList<>());
        EmployeeTrainingCache.get(rs.getInt(1)).add(rs.getString(2));
        return EmployeeTrainingCache.get(rs.getInt(1));
        //String training=rs.getString(2);

        //return tra
    }


    public void resetCache() {
        EmployeeTrainingCache.clear();
    }
}
