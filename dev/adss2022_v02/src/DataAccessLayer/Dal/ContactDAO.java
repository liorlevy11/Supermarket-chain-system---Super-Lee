package DataAccessLayer.Dal;

import BussinessLayer.Objects.Contact;
import javafx.util.Pair;

import java.sql.*;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ContactDAO extends DAO {
    private static final String SUPPLIER_ID = "SUPPLIER_ID";
    private static final String NAME = "NAME";
    private static final String EMAIL = "EMAIL";
    private static final String PHONE_NUMBER = "PHONE_NUMBER";
    private HashMap<Pair<Integer, String>, Contact> contactsHashMap; //suppID, name - contact
    private static ContactDAO instance = null;

    public static ContactDAO getInstance(){
        if(instance == null)
            instance = new ContactDAO();
        return instance;
    }
    private ContactDAO() {
        super("CONTACTS");
        contactsHashMap = new HashMap<>();
    }

    public void resetCache(){
        contactsHashMap.clear();
    }

    @Override
    public boolean Insert(Object contactObj) {
        Contact contact = (Contact) contactObj;
        boolean res = true;
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4}) VALUES(?, ?, ?, ?) "
                , _tableName, SUPPLIER_ID, NAME, EMAIL, PHONE_NUMBER
        );
        try (
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            //Connection connection=open
            //pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, contact.getSupplierId());
            pstmt.setString(2, contact.getName());
            pstmt.setString(3, contact.getEmail());
            pstmt.setString(4, contact.getPhoneNumber());
            pstmt.executeUpdate();
            if (!isInContactsMap(contact))
                contactsHashMap.put(new Pair<>(contact.getSupplierId(), contact.getName()), contact);


        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    private boolean isInContactsMap(Contact c) {
        for (Map.Entry<Pair<Integer, String>, Contact> entry : contactsHashMap.entrySet()) {
            if (entry.getKey().getKey() == c.getSupplierId() && entry.getKey().getValue().equals(c.getName()))
                return true;
        }
        return false;
    }

    @Override
    public boolean Delete(Object contactObj) {
        Contact contact = (Contact) contactObj;
        boolean res = true;
        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = ? AND {2} = ? AND {3} = ? AND {4} = ?"
                , _tableName, SUPPLIER_ID, NAME, EMAIL, PHONE_NUMBER);

        try (
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
//            Connection connection=open
//            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, contact.getSupplierId());
            pstmt.setString(2, contact.getName());
            pstmt.setString(3, contact.getEmail());
            pstmt.setString(4, contact.getPhoneNumber());
            pstmt.executeUpdate();
            for (Map.Entry<Pair<Integer, String>, Contact> entry : contactsHashMap.entrySet()) {
                if (entry.getKey().getKey() == contact.getSupplierId() && entry.getKey().getValue().equals(contact.getName())){
                    contactsHashMap.remove(entry.getKey());
                    break;
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
    public Contact convertReaderToObject(ResultSet rs) throws SQLException {
        Contact contact = new Contact(rs.getInt(SUPPLIER_ID), rs.getString(NAME), rs.getString(EMAIL), rs.getString(PHONE_NUMBER));
        return contact;
    }


    public Contact getContactBySuppIdAndName(int supplierId, String contactName) {
        for (Map.Entry<Pair<Integer, String>, Contact> entry : contactsHashMap.entrySet()) {
            if (entry.getKey().getKey() == supplierId && entry.getKey().getValue().equals(contactName))
                return contactsHashMap.get(entry.getKey());
        }
        Contact contact = null;
        try {
            List<Contact> lst = getContactsBySupplierID(supplierId);
            for (Contact cont : lst) {
                if (cont.getName().equalsIgnoreCase(contactName)) {
                    contact = cont;
                    contactsHashMap.put(new Pair<>(contact.getSupplierId(), contact.getName()), contact);
                    break;
                }
            }
            return contact;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contact;
    }


    public List<Contact> getAllContacts() {
        List<Contact> contList = new LinkedList<>();
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBHandler.CONTACTS + ";");
            while (rs.next()) {
                Contact contact = convertReaderToObject(rs);
                contList.add(contact);
                if (!isInContactsMap(contact))
                    contactsHashMap.put(new Pair<>(contact.getSupplierId(), contact.getName()), contact);
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contList;
    }
    public List<Contact> getContactsBySupplierID(int supplierID) {
        List<Contact> contList = new LinkedList<>();
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBHandler.CONTACTS + " WHERE " + SUPPLIER_ID + " = " + supplierID + ";");
            while (rs.next()) {
                Contact contact = convertReaderToObject(rs);
                contList.add(contact);
                if (!isInContactsMap(contact))
                    contactsHashMap.put(new Pair<>(contact.getSupplierId(), contact.getName()), contact);;
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contList;
    }

    public boolean UpdateContactEmail(int supplierID,String name, String email){
        boolean res = true;
        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? WHERE {2} = ? and {3} = ? "
                , _tableName,EMAIL,SUPPLIER_ID,NAME);
        try (
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setInt(2, supplierID);
            pstmt.setString(3, name);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public boolean UpdateContactPhoneNumber(int supplierID,String name, String phoneNumber){
        boolean res = true;
        String sql = MessageFormat.format("UPDATE {0} SET {1} = ? WHERE {2} = ? and {3} = ? "
                , _tableName,PHONE_NUMBER,SUPPLIER_ID,NAME);
        try (
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, phoneNumber);
            pstmt.setInt(2, supplierID);
            pstmt.setString(3, name);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public int getNumOfContacts() {
        int counter = 0;
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBHandler.CONTACTS + ";");
            while (rs.next()) {
                counter++;
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return counter;
    }
}
