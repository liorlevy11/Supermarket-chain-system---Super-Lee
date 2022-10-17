package DataAccessLayer;

import BussinessLayer.Objects.Site;
import BussinessLayer.Objects.Transport;


import java.sql.*;
import java.text.MessageFormat;
import java.util.*;

public class TransportDesDAO extends DAO {

    public static final String IdTransportColumnName = "IdTransport";
    public static final String IdSiteColumnName = "IdSite";
    private static String TableName = "TransportDes";
    public static final String url = "jdbc:sqlite:SuperLee.db";
    private HashMap<Integer , List<Site>> transportDesCache;

        //constructor
    public TransportDesDAO() {
            super(TableName);
            transportDesCache = new HashMap<>();
        }

    public boolean Insert(Object obj) {
        Transport transport = (Transport)obj;

        boolean res = true;
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}) VALUES(?, ?) "
                , _tableName, IdTransportColumnName, IdSiteColumnName
        );
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, transport.getIdTransport());
            pstmt.setInt(2, transport.getSource().getIdSite());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    // DINA : when deleting transport- delete all transport destinations
    public boolean Delete(Object obj) {
        Transport transport = (Transport)obj;
        boolean res = true;
        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = ?"
                , _tableName, IdTransportColumnName);
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, transport.getIdTransport());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public  List<Site> getDesByID(int idTransport){
        List result;
        if(transportDesCache.containsKey(idTransport))
            return transportDesCache.get(idTransport);
        else{
            result = Select(makeList(IdTransportColumnName),makeList(String.valueOf(idTransport)));
            return result; }
    }

    public List<Site> convertReaderToObject(ResultSet rs) throws SQLException {
        List<Site> des = null;
        if (transportDesCache.containsKey(rs.getInt(1)))
            des =  transportDesCache.get(rs.getInt(1));
        else {
            if (!transportDesCache.containsKey(rs.getInt(1)))
                des = new LinkedList<>(); }
        return des;
    }

    public boolean addDestination(int idTransport, int idSite) {
        boolean res = true;
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}) VALUES(?, ?) "
                , _tableName,IdTransportColumnName,IdSiteColumnName);
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
             pstmt.setInt(1, idTransport);
             pstmt.setInt(2, idSite);
             pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public boolean removeTransportDestination(int idTransport, int idSite) {
        boolean res = true;
        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = ? AND {2} = ?"
                , _tableName,IdTransportColumnName,IdSiteColumnName);
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
             pstmt.setInt(1, idTransport);
             pstmt.setInt(2, idSite);
             pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public List<Integer> SelectTransportDestanations(int idTransport) {
        List<Integer> list = new ArrayList<>();
        /// keys is for tables that have more that one key
        String sql = MessageFormat.format("SELECT {0} From {1} WHERE {2} = ?" ,
                IdSiteColumnName, _tableName,IdTransportColumnName);
        try (Connection connection = DriverManager.getConnection(url);
            PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idTransport);
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



    public static void main(String[] args) {

//         transportDesDataMapper.Insert()
//         TransportDesDTO transportDesDAO1 = new TransportDesDTO(transportDesDataMapper,1,1);
//        transportDesDAO1.Insert();
//        transportDesDAO1.Delete();
        //transportDesDataMapper.Delete();
    }


    public void resetCache() {
        transportDesCache.clear();
    }
}

