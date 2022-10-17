package DataAccessLayer;

import BussinessLayer.Objects.Driver;

import java.sql.*;
import java.text.MessageFormat;
import java.text.ParseException;

public class DriverDAO extends DAO {

    private static String TableName = "Driver";
    public static final String IDColumnName = "ID";
    public static final String AvgDistanceColumnName = "AvgDistance";

    public DriverDAO() {
        super(TableName);
    }

    public boolean InsertDriver(int id, double AvgDistance) {
        boolean res = true;
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}) VALUES(?, ?) "
                , _tableName, IDColumnName, AvgDistanceColumnName
        );
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setDouble(2, AvgDistance);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public void setDriverDistance(int id,double distance){
        Update(AvgDistanceColumnName,distance,makeList(IDColumnName),makeList(String.valueOf(id)));
    }

    public double selectDriverDistance(int id) {
        double distance=0;
        /// keys is for tables that have more that one key
        String sql = MessageFormat.format("SELECT {0} From {1} WHERE {2} = ?",
                AvgDistanceColumnName,  _tableName, IDColumnName);
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                // Fetch each row from the result set
                distance=resultSet.getDouble(1);
            }

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
        }
        return distance;
    }

    @Override
    public boolean Insert(Object obj) {
        Driver driver=(Driver)obj;
        boolean res = true;
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}) VALUES(?, ?) "
                , _tableName, IDColumnName, AvgDistanceColumnName
        );

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, driver.getId());
            pstmt.setDouble(2, driver.getAvgDistance());
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
    public boolean Delete(Object obj) {
        return false;
    }

    @Override
    public Object convertReaderToObject(ResultSet res) throws SQLException, ParseException {
        return null;
    }
}
