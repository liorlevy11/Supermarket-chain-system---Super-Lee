package BussinessLayer.Managers;

import BussinessLayer.Objects.Product;
import BussinessLayer.Objects.*;

import DataAccessLayer.Dal.ProductDAO;
import DataAccessLayer.Dal.ContactDAO;
import DataAccessLayer.Dal.SupplierDAO;
import DataAccessLayer.Dal.SupplierProductDAO;
import DataAccessLayer.SiteDAO;


import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

public class SupplierManager {
    public SupplierManager() {
    }

    public void loadData() {
        //supplier 1
        SupplierCard sCard1 = new SupplierCard(1, "AnimalKingdom", "Tel Aviv Shaul st 11", "187444", PaymentMethod.Cash);
        List<DayOfWeek> deliveryDays1 = new ArrayList<>();
        deliveryDays1.add(DayOfWeek.of(2));
        //product1
        NavigableMap<Integer, Double> discountByAmount1 = new TreeMap<>();
        discountByAmount1.put(50, 10.0);
        discountByAmount1.put(100, 25.0);
        //product2
        NavigableMap<Integer, Double> discountByAmount2 = new TreeMap<>();
        discountByAmount2.put(5, 15.0);
        discountByAmount2.put(10, 40.0);
        SupplierProduct it2 = new SupplierProduct(2, 1, "Puppies", 1500.0, "1-101", discountByAmount2);
        SupplierProduct it1 = new SupplierProduct(1, 1, "Animal Food", 15.0, "1-100", discountByAmount1);
        Map<String, SupplierProduct> product1 = new HashMap<>();
        product1.put(it1.getCatalogNumber(), it1);
        product1.put(it2.getCatalogNumber(), it2);

        Agreement agr1 = new Agreement("Animal Kingdom", deliveryDays1, SupplyMethod.byDays, product1.keySet().stream().collect(Collectors.toList()));

        Supplier supplier1 = addSupplier(sCard1, agr1);
        Contact contact1 = addContactsToSupplier(1, "Harry", "poterH@gmail.com", "0501234567");
        addProductToAgreement(1, 1, "1-100", 15.0, discountByAmount1);
        addProductToAgreement(1, 2, "1-101", 1500.0, discountByAmount2);
        SiteManager.getInstance().addSiteBySupplier(supplier1,contact1,"Center");

        //supplier 2


        SupplierCard sCard2 = new SupplierCard(2, "Fresh", "Beer Sheva Yizhak Rager 12", "111761", PaymentMethod.Cash);
        List<DayOfWeek> deliveryDays2 = new ArrayList<>();

        //product1
        NavigableMap<Integer, Double> discountByAmount3 = new TreeMap<>();
        discountByAmount3.put(30, 5.0);
        discountByAmount3.put(50, 10.0);

        //product2
        NavigableMap<Integer, Double> discountByAmount4 = new TreeMap<>();
        discountByAmount4.put(20, 10.0);
        discountByAmount4.put(40, 25.0);
        SupplierProduct it3 = new SupplierProduct(3, 2, "OrangeJuice", 7.0, "2-100", discountByAmount3);
        SupplierProduct it4 = new SupplierProduct(4, 2, "Pineapple", 20.0, "2-101", discountByAmount4);

        List<String> product2 = new LinkedList<>();
        product2.add(it3.getCatalogNumber());
        product2.add(it4.getCatalogNumber());

        Agreement agr2 = new Agreement("Fresh", deliveryDays2, SupplyMethod.byOrder, product2);
        Supplier supplier2 = addSupplier(sCard2, agr2);
        addProductToAgreement(2, 3, "2-100", 7.0, discountByAmount3);
        addProductToAgreement(2, 4, "2-101", 20.0, discountByAmount4);

        Contact contact2 = addContactsToSupplier(2, "Ron", "weasleyR@gmail.com", "0507654321");
        SiteManager.getInstance().addSiteBySupplier(supplier2,contact2,"South");


        //supplier 3
        SupplierCard sCard3 = new SupplierCard(3, "Flourish and Blotts", "Diagon Alley, London", "111761", PaymentMethod.Credit);
        List<DayOfWeek> deliveryDays3 = new ArrayList<>();
        //product5
        NavigableMap<Integer, Double> discountByAmount5 = new TreeMap<>();
        discountByAmount5.put(10, 3.0);
        discountByAmount5.put(100, 30.0);
        SupplierProduct it5 = new SupplierProduct(3, 3, "OrangeJuice", 10.0, "3-100", discountByAmount5);
        //product6
        NavigableMap<Integer, Double> discountByAmount6 = new TreeMap<>();
        discountByAmount6.put(30, 10.0);
        discountByAmount6.put(50, 20.0);
        SupplierProduct it6 = new SupplierProduct(4, 4, "Pineapple", 17.0, "3-101", discountByAmount6);

        List<String> products3 = new LinkedList<>();
        products3.add(it5.getCatalogNumber());
        products3.add(it6.getCatalogNumber());
        Agreement agr3 = new Agreement("Flourish and Blotts", deliveryDays3, SupplyMethod.byOrder, products3);
        Supplier supplier3 = addSupplier(sCard3, agr3);

        Contact contact3 = addContactsToSupplier(3, "Hermione", "weasleyR@gmail.com", "0507654321");
        addProductToAgreement(3, 3, "2-100", 10.0, discountByAmount5);
        addProductToAgreement(3, 4, "2-101", 17.0, discountByAmount6);

        SiteManager.getInstance().addSiteBySupplier(supplier3,contact3,"Center");


//        List<String> products4 = new LinkedList<>();
//        products3.add(it5.getCatalogNumber());
//        products3.add(it6.getCatalogNumber());
//        //supplier 4
//        SupplierCard sCard4 = new SupplierCard(4, "flowers", "Ashdod Haim Moshe Shapira 6 Street", "112761", PaymentMethod.Credit);
//        Agreement agr4 = new Agreement("flowers", deliveryDays3, SupplyMethod.byOrder, products3);
//
//        addSupplier(sCard4, agr4);
//
//        //supplier 5
//        SupplierCard sCard5 = new SupplierCard(5, "booms", "Jerusalem Yitzhak Navon 22 ", "112762", PaymentMethod.Credit);
//        Agreement agr5 = new Agreement("booms", deliveryDays3, SupplyMethod.byOrder, products3);
//
//        addSupplier(sCard5, agr5);
//        //supplier 6
//        SupplierCard sCard6 = new SupplierCard(6, "Blala", "Haifa HaHagana Ave 58", "112763", PaymentMethod.Credit);
//        Agreement agr6 = new Agreement("Blala", deliveryDays3, SupplyMethod.byOrder, products3);
//
//        addSupplier(sCard6, agr6);
//        //supplier 7
//        SupplierCard sCard7 = new SupplierCard(7, "Shayus", "Be'er Sheva Yitzhak rager 89", "112764", PaymentMethod.Credit);
//        Agreement agr7 = new Agreement("Shayus", deliveryDays3, SupplyMethod.byOrder, products3);
//
//        addSupplier(sCard7, agr7);
//
//

    }

    public Supplier getSupplier(int supplierId) {
        return SupplierDAO.getInstance().getSupplier(supplierId);
    }

    public List<Supplier> getAllSuppliers() {
        return SupplierDAO.getInstance().SelectAllSuppliers();
    }

    public List<SupplierProduct> getSupplierProducts(int supplierId) {
        return SupplierProductDAO.getInstance().getProductsBySupplier(supplierId);
    }

    public Supplier addSupplier(SupplierCard supplierCard, Agreement agreement) {
        boolean completed = SupplierDAO.getInstance().Insert(new Supplier(supplierCard, agreement));
        if (completed){
            return SupplierDAO.getInstance().getSupplier(supplierCard.getId());
        }
        else {
            throw new IllegalArgumentException("Couldn't complete the action, please try adding a new supplier again ");
        }
        }

    public boolean removeSupplier(int supplierID) {
        if (!isLegalSupplierID(supplierID))
            throw new IllegalArgumentException("Couldn't complete the action, there is no supplier with the ID: " + supplierID);
        List<Contact> contacts = ContactDAO.getInstance().getContactsBySupplierID(supplierID);
        for (Contact con : contacts) {
            ContactDAO.getInstance().Delete(con);
        }
        List<SupplierProduct> supplierProducts = SupplierProductDAO.getInstance().getProductsBySupplier(supplierID);
        for (SupplierProduct supplierProduct : supplierProducts) {
            SupplierProductDAO.getInstance().Delete(supplierProduct);
        }
        boolean succ = SupplierDAO.getInstance().Delete(SupplierDAO.getInstance().getSupplier(supplierID));
         if(succ)
             succ = SiteManager.getInstance().deleteSite(supplierID);
         return succ;
    }

    //AGREEMENT
    public Agreement createAnAgreement(String supplierName, List<DayOfWeek> deliveryDays, String supplyMethod) {
        SupplyMethod supply = SupplyMethod.valueOf(supplyMethod);
        Agreement agreement = new Agreement(supplierName, deliveryDays, supply);
        return agreement;
    }

    public SupplierCard createSupplierCard(int id, String name, String address, String bankAccount, String paymentMethod) {
        SupplierCard card = new SupplierCard(id, name, address, bankAccount, PaymentMethod.valueOf(paymentMethod));
        return card;
    }

    public Contact addContactsToSupplier(int supplierID, String name, String email, String phoneNumber) {
        if (!isLegalSupplierID(supplierID))
            throw new IllegalArgumentException("no supplier with id :" + supplierID);
        if (ContactDAO.getInstance().getContactBySuppIdAndName(supplierID, name) != null)
            throw new IllegalArgumentException("The supplier with id :" + supplierID + " already have a contact with the name " + name);
        Contact contact = new Contact(supplierID, name, email, phoneNumber);
        boolean completed = ContactDAO.getInstance().Insert(contact);
        return contact;
    }

    public void removeSupplierContact(int supplierID, String name) {
        if (!isLegalSupplierID(supplierID))
            throw new IllegalArgumentException("no supplier with id :" + supplierID);
        if (ContactDAO.getInstance().getContactBySuppIdAndName(supplierID, name) == null)
            throw new IllegalArgumentException("The supplier with id :" + supplierID + " doesn't have a contact with the name " + name);
        ContactDAO.getInstance().Delete(ContactDAO.getInstance().getContactBySuppIdAndName(supplierID, name));
    }

    //edit email if editEmail equals true, else edit phone number
    public Contact editSupplierContacts(int supplierID, String name, String email, String phoneNumber, boolean editEmail) {
        if (!isLegalSupplierID(supplierID))
            throw new IllegalArgumentException("no supplier with id :" + supplierID);
        if (ContactDAO.getInstance().getContactBySuppIdAndName(supplierID, name) == null)
            throw new IllegalArgumentException("The supplier with id :" + supplierID + " doesn't have a contact with the name " + name);
        if (editEmail)
            ContactDAO.getInstance().UpdateContactEmail(supplierID, name, email);
        else
            ContactDAO.getInstance().UpdateContactPhoneNumber(supplierID, name, phoneNumber);
        return ContactDAO.getInstance().getContactBySuppIdAndName(supplierID, name);
    }


    public void changeSupplierBackAccount(int supplierID, String bankAccount) {
        if (!isLegalSupplierID(supplierID))
            throw new IllegalArgumentException("no supplier with id :" + supplierID);
        SupplierDAO.getInstance().UpdateBankAccount(supplierID, bankAccount);
    }

    public void changeSupplyMethod(int supplierID, String supplyMethod, List<DayOfWeek> days) {
        if (!isLegalSupplierID(supplierID))
            throw new IllegalArgumentException("no supplier with id :" + supplierID);
        SupplierDAO.getInstance().UpdateSupplyMethod(supplierID, supplyMethod, days.toString());
    }

    public void changeSupplierPaymentMethod(int supplierID, String paymentMethod) {
        if (!isLegalSupplierID(supplierID))
            throw new IllegalArgumentException("no supplier with id :" + supplierID);
        if (paymentMethod == PaymentMethod.Cash.name() || paymentMethod == PaymentMethod.Credit.name() || paymentMethod == PaymentMethod.TransitToAccount.name())
            SupplierDAO.getInstance().UpdatePaymentMethod(supplierID, paymentMethod);
        else
            throw new IllegalArgumentException("there is no payment method named: " + paymentMethod);
    }

    public void changeAddress(int supplierID, String address) {
        if (!isLegalSupplierID(supplierID))
            throw new IllegalArgumentException("no supplier with id :" + supplierID);
        boolean succ = SupplierDAO.getInstance().UpdateAddress(supplierID, address);
        if(succ)
            SiteManager.getInstance().UpdateSiteAddress(supplierID,address);
    }


    public SupplierProduct addProductToAgreement(int supplierID, int productID, String catalogNumber, double priceList, NavigableMap<Integer, Double> discounts) {
        if (!isLegalSupplierID(supplierID))
            throw new IllegalArgumentException("no supplier with id :" + supplierID);
        Product product = ProductDAO.getInstance().getProductById(productID);
        if (product == null)
            throw new IllegalArgumentException("no product with id :" + productID);
        if (SupplierProductDAO.getInstance().getSupplierProductById(productID, supplierID) != null)
            throw new IllegalArgumentException("The supplier's agreement contains the product: " + productID);
        SupplierProduct supplierProduct = new SupplierProduct(productID, supplierID, product.getName(), priceList, catalogNumber, discounts);
        SupplierProductDAO.getInstance().Insert(supplierProduct);
        return supplierProduct;
    }

    public SupplierProduct removeProductFromAgreement(int supplierID, int productID) {
        if (!isLegalSupplierID(supplierID))
            throw new IllegalArgumentException("no supplier with id :" + supplierID);
        SupplierProduct supplierProduct = SupplierProductDAO.getInstance().getSupplierProductById(productID, supplierID);
        if (supplierProduct == null)
            throw new IllegalArgumentException("The supplier's agreement doesn't contain the product with the id: " + productID);
        SupplierProductDAO.getInstance().Delete(supplierProduct);
        return supplierProduct;
    }

    public boolean isLegalSupplierID(int supplierID) {
        Supplier supplier = SupplierDAO.getInstance().getSupplier(supplierID);
        return (supplier != null);
    }

    public List<SupplierProduct> getProductsBySupplier(int supplierID) {
        return SupplierProductDAO.getInstance().getProductsBySupplier(supplierID);
    }

    public void DelData() {
        SupplierDAO supplierDAO = SupplierDAO.getInstance();
        supplierDAO.Delete();


    }

    public void startingProgram() {
    }
}
