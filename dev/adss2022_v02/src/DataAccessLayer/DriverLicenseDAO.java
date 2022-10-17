package DataAccessLayer;

import BussinessLayer.Objects.Driver;

import java.sql.*;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.List;

public class DriverLicenseDAO extends DAO {

    private static String TableName = "DriverLicense";
    public static final String IDColumnName = "ID";
    public static final String LicenseColumnName = "License";

    public DriverLicenseDAO() {
        super(TableName);
    }

    @Override
    public boolean Insert(Object obj) {
        Driver driver = (Driver)obj;
        List<String> licenses=driver.getStringlicenses();
        boolean res = true;
        for(String license: licenses) {
            String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}) VALUES(?, ?) "
                    , _tableName, IDColumnName, LicenseColumnName
            );
            try (Connection connection = DriverManager.getConnection(url);
                 PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, driver.getId());
                pstmt.setString(2, license);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Got Exception:");
                System.out.println(e.getMessage());
                System.out.println(sql);
                res = false;
            }
        }
        return res;
    }

    public boolean InsertDriverLicense(int id, String license) {
        boolean res = true;
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}) VALUES(?, ?) "
                , _tableName, IDColumnName, LicenseColumnName
        );

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.setString(2, license);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public List<String> selectDriverLicense(int id) {
       return SelectString(LicenseColumnName,makeList(IDColumnName),makeList(String.valueOf(id)));
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
