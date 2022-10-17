package DataAccessLayer;


import BussinessLayer.Objects.Site;

import java.sql.*;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;

public class SiteDAO extends DAO {

    public static final String IDColumnName = "ID";
    public static final String ContactPersonColumnName = "ContactPerson";
    public static final String PhoneNumberColumnName = "PhoneNumber";
    public static final String AddressColumnName = "Address";
    public static final String ShippingAreaColumnName = "ShippingArea";
    private static String TableName = "Site";
    private HashMap<Integer, Site> sitesCache;

    public SiteDAO() {
        super(TableName);
        sitesCache = new HashMap<>();
    }

    @Override
    public boolean Insert(Object siteObj) {
        Site site = (Site) siteObj;
        boolean res = true;
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4}, {5}) VALUES(?, ?, ?, ?, ?) "
                , _tableName, IDColumnName, ContactPersonColumnName, ShippingAreaColumnName, PhoneNumberColumnName, AddressColumnName
        );
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, site.getIdSite());
            pstmt.setString(2, site.getContactPerson());
            pstmt.setString(3, site.getStringShippingArea());
            pstmt.setString(4, site.getPhoneNumber());
            pstmt.setString(5, site.getAddress());
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
    public boolean Delete(Object siteObj) {
        Site site = (Site) siteObj;
        boolean res = true;
        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = ? "
                , _tableName, IDColumnName);
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, site.getIdSite());
            pstmt.executeUpdate();
            if (sitesCache.containsKey(site.getIdSite()))
                sitesCache.remove(site.getIdSite(), site);

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public List<Site> SelectAllSites() {
        List<Site> list = (List<Site>) (List<?>) Select();
        return list;
    }

    public Site getSite(int idSite) {
        if (sitesCache.containsKey(idSite)) //site in cache
            return sitesCache.get(idSite);
        else { //site in db
            List<Site> result = Select(makeList(IDColumnName), makeList(String.valueOf(idSite)));
            if (result.size() == 0)
                throw new IllegalArgumentException("site " + idSite + " does not exist");
            Site site = result.get(0);
            sitesCache.put(site.getIdSite(), site);
            return site;
        }
    }

    public int getMaxid() {//for the Site.counter utilize at program beginning
        int val = 0;
        String sql = MessageFormat.format("SELECT MAX({0}) From {1}",
                IDColumnName, _tableName);
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            ResultSet resultSet = pstmt.executeQuery();
            val = resultSet.getInt(1);

        } catch (SQLException e) {
            //no sites in db
        }
        return val;
    }

    @Override
    public Site convertReaderToObject(ResultSet rs) throws SQLException {
        if(sitesCache.containsKey(rs.getInt(1)))
            return sitesCache.get(rs.getInt(1));
        Site site = new Site(rs.getInt(1), rs.getString(2), rs.getString(4), rs.getString(5), rs.getString(3));
        sitesCache.put(site.getIdSite(),site);
        return site;
    }


    // setters
    public void setContactPerson(String contactPerson, int idSite) {
        Update(ContactPersonColumnName, contactPerson, makeList(IDColumnName), makeList(String.valueOf(idSite)));
    }

    public void setPhoneNumber(String phoneNumber, int idSite) {
        Update(PhoneNumberColumnName, phoneNumber, makeList(IDColumnName), makeList(String.valueOf(idSite)));
    }

    public void setAddress(String address, int idSite) {
        Update(AddressColumnName, address, makeList(IDColumnName), makeList(String.valueOf(idSite)));
    }

    public void setShippingArea(String shippingArea, int idSite) {
        Update(ShippingAreaColumnName, shippingArea, makeList(IDColumnName), makeList(String.valueOf(idSite)));
    }

    public void resetCache() {
        sitesCache.clear();
    }
}