
package DataAccessLayer;


import BussinessLayer.Objects.Shift;
import BussinessLayer.Objects.ShiftType;
//import javafx.util.Pair;


import java.sql.*;
import java.text.MessageFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;

public class ShiftDAO extends DAO {

    public static final String TypeColumnName = "Type";
    public static final String DateColumnName = "Date";
    public static final String ShiftManagerIDColumnName = "ShiftManagerID";
    private static String TableName = "Shift";
    //<DAta TYPE> Id
    private HashMap<List<String>, Shift> shiftsCache;


    //constructor
    public ShiftDAO() {
        super(TableName);
        shiftsCache = new HashMap<>();
    }

    @Override
    public boolean Insert(Object shiftObj) {
        Shift shift = (Shift) shiftObj;
        boolean res = true;
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}) VALUES(?, ?, ?) "
                , _tableName, TypeColumnName, DateColumnName, ShiftManagerIDColumnName
        );

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, shift.getShiftType().toString());
            pstmt.setString(2, shift.getDate().format(formatters));
            pstmt.setInt(3, shift.getManager());
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
    public boolean Delete(Object shiftObj) {
        Shift shift = (Shift) shiftObj;
        boolean res = true;
        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = ? AND {2} = ? "
                , _tableName, TypeColumnName, DateColumnName);

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, shift.getShiftType().toString());
            pstmt.setString(2, shift.getDate().toString());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }


    public boolean Delete(String shift, String date) {
        boolean res = true;
        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = ? AND {2} = ? "
                , _tableName, TypeColumnName, DateColumnName);

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, shift);
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

    public Shift getShift(LocalDate date, ShiftType shiftType) {
        List<Shift> l = Select(makeList(DateColumnName, TypeColumnName), makeList(date.format(formatters), shiftType.toString()));
        if (l.size() == 0)
            return null;
        return l.get(0);
    }


    public List<Shift> SelectAllShifts() {
        List<Shift> list = (List<Shift>) (List<?>) Select();
        return list;
    }


    @Override
    public Shift convertReaderToObject(ResultSet rs) throws SQLException, ParseException {
        String dateString = rs.getString(2);
        Shift shift = new Shift(parseLocalDate(dateString), rs.getString(1), rs.getInt(3));
        return shift;
    }


    public int getShiftManagerIdByShift(String type, String date) {
        List<Shift> l = Select(makeList(TypeColumnName, DateColumnName), makeList(type, date));
        if (l.size()==0)
            return 0;
        return l.get(0).getManager();
    }

    public void SetIDmanager(int id, String type, String date) {
        Update(ShiftManagerIDColumnName, id, makeList(TypeColumnName, DateColumnName), makeList(type, date));
    }

    public void resetCache() {
        shiftsCache.clear();
    }
}


