package DataAccessLayer;
import BussinessLayer.Objects.Truck;
import java.sql.*;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;

public class TruckDAO extends DAO {
    public static final String IDColumnName = "ID";
    public static final String LicenseNumberColumnName = "LicenseNumber";
    public static final String ModelColumnName = "Model";
    public static final String TruckWeightColumnName = "TruckWeight";
    public static final String MaxWeightColumnName = "MaxWeight";
    public static final String TypeOfLicenseColumnName = "TypeOfLicense";
    private static String TableName = "Truck";
    private HashMap<Integer, Truck> trucksCache;

    public TruckDAO() {
        super(TableName);
        trucksCache = new HashMap<>();
    }

    @Override
    public boolean Insert(Object truckObj) {
        Truck truck = (Truck) truckObj;
        boolean res = true;
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4}, {5} ,{6}) VALUES(?, ?, ?, ?, ? ,?) "
                , _tableName, IDColumnName, LicenseNumberColumnName, ModelColumnName, TruckWeightColumnName, MaxWeightColumnName, TypeOfLicenseColumnName
        );
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, truck.getIdTrack());
            pstmt.setString(2, truck.getLicenseNumber());
            pstmt.setString(3, truck.getModel());
            pstmt.setDouble(4, truck.getTruckWeight());
            pstmt.setDouble(5, truck.getMaxWeight());
            pstmt.setString(6, truck.getStringTypeOfLicence());
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
    public boolean Delete(Object truckObj) {
        Truck truck = (Truck) truckObj;
        boolean res = true;
        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = ? "
                , _tableName, truck.getIdTrack());

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, truck.getIdTrack());
            pstmt.executeUpdate();
            if (trucksCache.containsKey(truck.getIdTrack()))
                trucksCache.remove(truck.getIdTrack(), truck);

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public List<Truck> SelectAllTrucks() {
        List<Truck> list = (List<Truck>) (List<?>) Select();
        return list;
    }



    public Truck getTruck(int idTruck) {
        if (trucksCache.containsKey(idTruck)) //truck in cache
            return trucksCache.get(idTruck);
        else { //truck in db
            List<Truck> result = Select(makeList(IDColumnName), makeList(String.valueOf(idTruck)));
            if (result.size() == 0)
                throw new IllegalArgumentException("truck " + idTruck + " does not exist");
            Truck truck = result.get(0);
            trucksCache.put(truck.getIdTrack(), truck); //insert to cache
            return truck;
        }
    }

    @Override
    public Truck convertReaderToObject(ResultSet rs) throws SQLException {
        if(trucksCache.containsKey(rs.getInt(1)))
            return trucksCache.get(rs.getInt(1));
        Truck truck = new Truck(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getDouble(4), rs.getDouble(5)
                , rs.getString(6));
        trucksCache.put(truck.getIdTrack(),truck);
        return truck;
    }

    public boolean inCache(int idtruck){
        return trucksCache.containsKey(idtruck);
    }

    ///// SETTERS
    public void setLicenseNumber(String licenseNumber, int idTrack) {
        Update(LicenseNumberColumnName, licenseNumber, makeList(IDColumnName), makeList(String.valueOf(idTrack)));
    }

    public void setModel(String model, int idTrack) {
        Update(ModelColumnName, model, makeList(IDColumnName), makeList(String.valueOf(idTrack)));
    }

    public void setTruckWeight(double truckWeight, int idTrack) {
        Update(TruckWeightColumnName, truckWeight, makeList(IDColumnName), makeList(String.valueOf(idTrack)));
    }

    public void setMaxWeight(double maxWeight, int idTrack) {
        Update(MaxWeightColumnName, maxWeight, makeList(IDColumnName), makeList(String.valueOf(idTrack)));
    }

    public void setTypeOfLicence(String typeOfLicence, int idTrack) {
        Update(TypeOfLicenseColumnName, typeOfLicence, makeList(IDColumnName), makeList(String.valueOf(idTrack)));
    }

    public int getMaxid() {//for the Truck.counter utilize at program beginning
        int val = 0;
        String sql = MessageFormat.format("SELECT MAX({0}) From {1}",
                IDColumnName, _tableName);
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet resultSet = pstmt.executeQuery();
            val = resultSet.getInt(1);
        } catch (SQLException e) {
            //no trucks in db
        }
        return val;
    }

    public boolean ExistLicenseNumber(String licenseNumber){
        boolean exist =false;
        /// keys is for tables that have more that one key
        String sql = MessageFormat.format("SELECT {0} From {1} WHERE {2} = ?",
                IDColumnName,  _tableName, LicenseNumberColumnName);
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, licenseNumber);
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                // Fetch each row from the result set
               exist=true;
            }

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
        }
        return exist;
    }

    public void resetCache() {
        trucksCache.clear();
    }
}