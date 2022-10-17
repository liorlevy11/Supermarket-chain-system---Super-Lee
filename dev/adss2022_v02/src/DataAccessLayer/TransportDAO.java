package DataAccessLayer;


import BussinessLayer.Objects.Transport;

import java.sql.*;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class TransportDAO extends DAO {
    public static final String IDColumnName = "ID";
    public static final String DateAndTimeColumnName = "DateAndTime";
    public static final String DdnColumnName = "Ddn";
    public static final String SourceIDColumnName = "SourceID";
    public static final String TruckIDColumnName = "TruckID";
    public static final String DriverIDColumnName = "DriverID";
    public static final String TotalWeightColumnName = "TotalWeight";
    public static final String OrderIDColumnName = "orderID";
    public static final String IsReceived = "IsReceived";
    private static String TableName = "Transport";
    private HashMap<Integer, Transport> transportsCache;
    private TransportDesDAO desDAO;

    public TransportDAO() {
        super(TableName);
        transportsCache = new HashMap<>();
        desDAO = new TransportDesDAO();
    }

    @Override
    public boolean Insert(Object transportObj) {
        Transport transport = (Transport) transportObj;
        boolean res = true;
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4}, {5},{6},{7},{8},{9}) VALUES(?, ?, ?, ?, ?, ?,?,?,?) "
                , _tableName, IDColumnName, DateAndTimeColumnName, DdnColumnName, SourceIDColumnName, TruckIDColumnName,
                DriverIDColumnName, TotalWeightColumnName, OrderIDColumnName,IsReceived
        );

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, transport.getIdTransport());


            pstmt.setString(2, transport.getDate().format(formatter_2));
            pstmt.setInt(3, transport.getDriverDocNum());
            pstmt.setInt(4, transport.getSource().getIdSite());
            pstmt.setInt(5, transport.getTruck().getIdTrack());
            pstmt.setInt(6, transport.getDriver().getId());
            pstmt.setDouble(7, transport.getTotalWeight());
            pstmt.setInt(8, transport.getOrderID());
            pstmt.setBoolean(9, transport.isReceived());
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
    public boolean Delete(Object transportObj) {
        Transport transport = (Transport) transportObj;

        boolean res = true;
        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = ? "
                , _tableName, IDColumnName);

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, transport.getIdTransport());
            pstmt.executeUpdate();
            if (transportsCache.containsKey(transport.getIdTransport()))
                transportsCache.remove(transport.getIdTransport(), transport);

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public List<Transport> SelectAllTransports() {
        List<Transport> list = (List<Transport>) (List<?>) Select();
        return list;
    }

    public Transport getTransport(int idTransport) {
        if (transportsCache.containsKey(idTransport)) //Transport in cache
            return transportsCache.get(idTransport);
        else { //Transport in db
            List<Transport> result = Select(makeList(IDColumnName), makeList(String.valueOf(idTransport)));
            if (result.size() == 0)
                throw new IllegalArgumentException("Transport " + idTransport + " does not exist");
            Transport transport = result.get(0);
            transportsCache.put(transport.getIdTransport(), transport);
            return transport;
        }
    }

    public List<Transport> getTransportByDate(Date today) {
        List<Transport> result = Select(makeList(DateAndTimeColumnName), makeList(String.valueOf(today)));
        if (result.size() == 0)
            throw new IllegalArgumentException("there is no Transport on " + today);
        List<Transport> transports = new LinkedList<>();
        for (Transport transport : result) {
            if (!transportsCache.containsKey(transport))
                transportsCache.put(transport.getIdTransport(), transport);
            transports.add(transport);
        }
        return transports;
    }

    public boolean isTransportForOrder(int orderID){
        List<Transport> result = Select(makeList(OrderIDColumnName), makeList(String.valueOf(orderID)));
        if (result.size() == 0)
            return false;
        return true;
    }

    public Transport getTransportByOrderID(int orderID) {
        List<Transport> result = Select(makeList(OrderIDColumnName), makeList(String.valueOf(orderID)));
        if (result.size() == 0)
            throw new IllegalArgumentException("there is no Transport with this order id " + orderID);
        List<Transport> transports = new LinkedList<>();
        for (Transport transport : result) {
            if (!transportsCache.containsKey(transport))
                transportsCache.put(transport.getIdTransport(), transport);
            transports.add(transport);
        }
        Transport transport = transports.get(0);
        return transport;
    }

    public int getTransportSourceID(int id) {
        return selectIdsFromTransport(id, SourceIDColumnName);
    }

    public int getTransportTruckID(int id) {
        return selectIdsFromTransport(id, TruckIDColumnName);
    }



    public int getTransportDriverID(int id) {
        return selectIdsFromTransport(id, DriverIDColumnName);
    }

    public int selectIdsFromTransport(int idTransport, String ColumnName) {
        int id = 0;
        /// keys is for tables that have more that one key
        String sql = MessageFormat.format("SELECT {0} From {1} WHERE {2} = ?",
                ColumnName, _tableName, IDColumnName);
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idTransport);
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                // Fetch each row from the result set
                id = resultSet.getInt(1);
            }

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
        }
        return id;
    }

    public LocalDateTime getDateFromTransport(int idTransport, String DateAndTimeColumnName) {
        LocalDateTime dateTime = null;
        String date ;
        /// keys is for tables that have more that one key
        String sql = MessageFormat.format("SELECT {0} From {1} WHERE {2} = ?",
                DateAndTimeColumnName , _tableName, IDColumnName);
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idTransport);
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                // Fetch each row from the result set
                date = resultSet.getString(1);
                dateTime = parseLocalDateTime(date);

            }

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
        }
        return dateTime;
    }
    public boolean ExistDdnNumber(int Ddn) {
        boolean exist = false;
        /// keys is for tables that have more that one key
        String sql = MessageFormat.format("SELECT {0} From {1} WHERE {2} = ?",
                IDColumnName, _tableName, DdnColumnName);
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, Ddn);
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                // Fetch each row from the result set
                exist = true;
            }

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
        }
        return exist;
    }


    @Override
    public Transport convertReaderToObject(ResultSet rs) throws SQLException, ParseException {
        String dateString = rs.getString(2);
        Transport transport = new Transport(rs.getInt(1), parseLocalDateTime(dateString), rs.getInt(3)
                , rs.getDouble(7), rs.getInt(8),rs.getBoolean(9));
        return transport;
    }


    public void setDate(int idTransport, String date) {
        Update(DateAndTimeColumnName, date, makeList(IDColumnName), makeList(String.valueOf(idTransport)));
    }

    public void setDdn(int idTransport, int ddn) {
        Update(DdnColumnName, ddn, makeList(IDColumnName), makeList(String.valueOf(idTransport)));
    }

    public void setTruckID(int idTransport, int truckID) {
        Update(TruckIDColumnName, truckID, makeList(IDColumnName), makeList(String.valueOf(idTransport)));
    }

    public void setDriverID(int idTransport, int driverID) {
        Update(DriverIDColumnName, driverID, makeList(IDColumnName), makeList(String.valueOf(idTransport)));
    }

    public void setTotalWeight(int idTransport, double totalWeight) {
        Update(TotalWeightColumnName, totalWeight, makeList(IDColumnName), makeList(String.valueOf(idTransport)));
    }

    public void setIsReceived(int idTransport, boolean isReceived) {
        if(inCache(idTransport)){
            Transport transport = this.transportsCache.get(idTransport);
            transport.setIsReceived(isReceived);
        }
        Update(IsReceived, isReceived, makeList(IDColumnName), makeList(String.valueOf(idTransport)));
    }

    public void setSourceID(int idTransport, int sourceID) {
        Update(SourceIDColumnName, sourceID, makeList(IDColumnName), makeList(String.valueOf(idTransport)));
    }

    public int getMaxid() {
        int val = 0;
        String sql = MessageFormat.format("SELECT MAX({0}) From {1}",
                IDColumnName, _tableName);
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet resultSet = pstmt.executeQuery();
            val = resultSet.getInt(1);

        } catch (SQLException e) {
            //System.out.println("Got Exception:");
            //System.out.println(e.getMessage());
            //System.out.println(sql);
        }
        return val;
    }

    public List<LocalDateTime> getDriverTransports(int idDriver) {
        List<String> dates = SelectString(DateAndTimeColumnName, makeList(DriverIDColumnName), makeList(String.valueOf(idDriver)));
        List<LocalDateTime> transportDates = new ArrayList<>();
        for (String d : dates) {
            transportDates.add(parseLocalDateTime(d));
        }
        return transportDates;
    }

    public void resetCache() {
        transportsCache.clear();
    }

    public List<LocalDateTime> getTruckTransports(int idTrack) {
        List<String> dates = SelectString(DateAndTimeColumnName, makeList(TruckIDColumnName), makeList(String.valueOf(idTrack)));
        List<LocalDateTime> transportDates = new ArrayList<>();
        for (String d : dates) {
            transportDates.add(parseLocalDateTime(d));
        }
        return transportDates;
    }

    public boolean inCache(int idTransport) {
        return transportsCache.containsKey(idTransport);
    }


    public List<Transport> SelectAllTransportsOfDate(java.util.Date today) {
        List<Transport> list = (List<Transport>) (List<?>) Select();
        ZoneId defaultZoneId = ZoneId.systemDefault();
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        List<Transport> transList = new LinkedList<>();
        for (Transport t:list){
            java.util.Date date = Date.from(t.getDate().toLocalDate().atStartOfDay(defaultZoneId).toInstant());
            if(fmt.format(date).equals(fmt.format(today))){
                transList.add(t);
            }
        }
        return transList;
    }

    public void deleteTransportByOrderID(int orderID) {

    }
}
