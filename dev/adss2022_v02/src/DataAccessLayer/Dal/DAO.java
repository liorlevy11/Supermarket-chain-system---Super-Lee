package DataAccessLayer.Dal;

import java.sql.*;
import java.text.MessageFormat;
import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class DAO {

    //public final String _connectionString;
    public final String _tableName;
    //  public static Connection connection;
    protected Connection connection;
    DateTimeFormatter formatter_2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    {
        try {
            connection = DBHandler.getInstance().open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //constructor
    public DAO(String tableName) {
        this._tableName = tableName;
    }

    public boolean deleteTable() {
        boolean res = true;
        String sql = MessageFormat.format("DROP TABLE {0}"
                , _tableName);

        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public boolean Delete() {
        boolean res = true;
        String sql = MessageFormat.format("DELETE FROM {0}"
                , _tableName);

        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public boolean Update(String ColumnName, int value, int key) {
        boolean res = true;
        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? WHERE id = ? "
                , _tableName, ColumnName);
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, value);
            pstmt.setInt(2, key);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public boolean Update(String ColumnName, double value, int key) {
        boolean res = true;
        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? WHERE id = ? "
                , _tableName, ColumnName);
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, value);
            pstmt.setInt(2, key);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }


    public boolean Update(String ColumnName, String value, int key) {
        boolean res = true;
        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? WHERE id = ? "
                , _tableName, ColumnName);
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, value);
            pstmt.setInt(2, key);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public boolean Update(String ColumnName, String value, String key) {
        boolean res = true;
        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? WHERE id = ? "
                , _tableName, ColumnName);
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, value);
            pstmt.setString(2, key);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public boolean Update(String ColumnName, String value, List<String> Columnkeys, List<String> keys) {
        boolean res = true;
        String keysQuery;
        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? WHERE" + keysQuery(Columnkeys)
                , _tableName, ColumnName);

        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, value);
            int i = 2;
            for (String key : keys) {
                pstmt.setString(i, key);
                i++;
            }
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public boolean Update(String ColumnName, int value, List<String> Columnkeys, List<String> keys) {
        boolean res = true;
        String keysQuery;
        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? WHERE" + keysQuery(Columnkeys)
                , _tableName, ColumnName);

        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, value);
            int i = 2;
            for (String key : keys) {
                pstmt.setString(i, key);
                i++;
            }
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public boolean Update(String ColumnName, double value, List<String> Columnkeys, List<String> keys) {
        boolean res = true;
        String keysQuery;
        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? WHERE" + keysQuery(Columnkeys)
                , _tableName, ColumnName);

        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, value);
            int i = 2;
            for (String key : keys) {
                pstmt.setString(i, key);
                i++;
            }
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public boolean Update(String ColumnName, Date value, List<String> Columnkeys, List<String> keys) {
        boolean res = true;
        String keysQuery;
        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? WHERE" + keysQuery(Columnkeys)
                , _tableName, ColumnName);

        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDate(1, (java.sql.Date) value);
            int i = 2;
            for (String key : keys) {
                pstmt.setString(i, key);
                i++;
            }
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public boolean Update(String ColumnName, boolean value, List<String> Columnkeys, List<String> keys) {
        boolean res = true;
        String keysQuery;
        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? WHERE" + keysQuery(Columnkeys)
                , _tableName, ColumnName);

        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, value);
            int i = 2;
            for (String key : keys) {
                pstmt.setString(i, key);
                i++;
            }
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    protected String keysQuery(List<String> Columnkeys) {
        String keysQuery = "";
        for (String key : Columnkeys) {
            keysQuery += " " + key + " = ? AND";
        }
        keysQuery = keysQuery.substring(0, keysQuery.length() - 4);

        return keysQuery;
    }

    public boolean Update(String ColumnName, int value, String key) {
        boolean res = true;
        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? WHERE id = ? "
                , _tableName, ColumnName);
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, value);
            pstmt.setString(2, key);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public boolean Update(String ColumnName, double value, String key) {
        boolean res = true;
        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? WHERE id = ? "
                , _tableName, ColumnName);
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, value);
            pstmt.setString(2, key);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    protected List Select() {
        List list = new ArrayList<>();
        String sql = MessageFormat.format("SELECT * From {0}"
                , _tableName);
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                // Fetch each row from the result set
                list.add(convertReaderToObject(resultSet));
            }

        } catch (SQLException | ParseException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
        }
        return list;
    }

    public List<String> makeList(String... strings) {
        List<String> list = new ArrayList<String>();
        for (String s : strings)
            list.add(s);
        return list;
    }

    protected List Select(List<String> Columnkeys, List<String> keys) {
        List list = new ArrayList<>();
        /// keys is for tables that have more that one key
        String sql = MessageFormat.format("SELECT * From {0} WHERE" + keysQuery(Columnkeys)
                , _tableName);
        try (
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
            int i = 1;
            for (String key : keys) {
                pstmt.setString(i, key);
                i++;
            }
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                // Fetch each row from the result set
                list.add(convertReaderToObject(resultSet));
            }

        } catch (SQLException | ParseException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
        }
        return list;
    }


    //abstract functions
    public abstract boolean Insert(Object obj);

    public abstract boolean Delete(Object obj);

    public abstract void resetCache();

    public abstract Object convertReaderToObject(ResultSet res) throws SQLException, ParseException;


}
