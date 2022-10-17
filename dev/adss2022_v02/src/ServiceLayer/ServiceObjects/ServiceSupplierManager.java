package ServiceLayer.ServiceObjects;

import BussinessLayer.Managers.SupplierManager;
import BussinessLayer.Objects.*;
import DataAccessLayer.Dal.ContactDAO;
import ServiceLayer.Response;
import ServiceLayer.ResponseT;

import java.time.DayOfWeek;
import java.util.*;

public class ServiceSupplierManager {

    private SupplierManager suppliersMan;
    private static ServiceSupplierManager instance;

    private ServiceSupplierManager() {
        this.suppliersMan = new SupplierManager();
    }

    public static ServiceSupplierManager getInstance() {
        if (instance == null) {
            instance = new ServiceSupplierManager();
        }
        return instance;
    }

    public Response loadData() {
        try {
            suppliersMan.loadData();
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }

    }

    public ResponseT<List<ServiceSupplier>> getAllSuppliers() {
        try {
            List<Supplier> suppliersB = suppliersMan.getAllSuppliers();
            List<ServiceSupplier> suppliersS = new ArrayList<>();
            for (Supplier s : suppliersB) {
                suppliersS.add(convertSupplierBtoS(s));
            }
            return ResponseT.fromValue(suppliersS);
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ServiceSupplier convertSupplierBtoS(Supplier supplier) {
        List<Contact> contacts = ContactDAO.getInstance().getContactsBySupplierID(supplier.getSupplierCard().getId());
        Map<String, ServiceContact> contactMap = new HashMap<>();
        for (Contact c : contacts) {
            contactMap.put(c.getName(), convertContactBtoS(c));
        }
        ServiceSupplierCard sCard = new ServiceSupplierCard(supplier.getSupplierCard().getId(), supplier.getSupplierCard().getName(), supplier.getSupplierCard().getAddress(), supplier.getSupplierCard().getBankAccount(), supplier.getSupplierCard().getPaymentMethod().name(), contactMap);
        ServiceAgreement sAgreement = new ServiceAgreement(supplier.getAgreement().getSupplierName(), supplier.getAgreement().getDeliveryDays(), supplier.getAgreement().getSupplyMethod().name(), null);
        return new ServiceSupplier(sCard, sAgreement);
    }

    public ServiceContact convertContactBtoS(Contact contact) {
        ServiceContact serviceContact = new ServiceContact(contact.getSupplierId(), contact.getName(), contact.getEmail(), contact.getPhoneNumber());
        return serviceContact;
    }

    public ResponseT getSupplier(int supplierId) {
        try {
            Supplier supplier = suppliersMan.getSupplier(supplierId);
            return ResponseT.fromValue(supplier);
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<Map<String, ServiceProductS>> getSupplierItems(int supplierId) {
        try {
            isLegalSupplierID(supplierId);
            Map<String, ServiceProductS> itemsS = new HashMap<>();
            List<SupplierProduct> lst = suppliersMan.getSupplierProducts(supplierId);
            Map<String, SupplierProduct> itemsB = new HashMap<>();
            lst.forEach(supplierProduct1 -> itemsB.put(supplierProduct1.getCatalogNumber(), supplierProduct1));
            itemsB.forEach((s, item) -> itemsBToSList(itemsS, s, item));
            return ResponseT.fromValue(itemsS);
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    //////////////////////////////////////////////////////////////////////////////


    private NavigableMap<Integer, Double> convertDiscountsListToMap(List<String> discountsString) {
        NavigableMap<Integer, Double> discountsMap = new TreeMap<>();
        for (String pair : discountsString) {
            String[] stringPair = pair.split(":");
            Integer amount = Integer.parseInt(stringPair[0]);
            String perString = stringPair[1];
            Double percentages = Double.parseDouble(perString);
            discountsMap.put(amount, percentages);
        }
        return discountsMap;
    }

    private List<String> convertDiscountsMapToList(NavigableMap<Integer, Double> discounts) {
        List<String> list = new ArrayList<String>();
        discounts.entrySet().forEach((integerDoubleEntry) -> mapToStringList(integerDoubleEntry, list));
        return list;
    }


    private void mapToStringList(Map.Entry entry, List<String> list) {
        list.add(entry.toString());
    }

    private void itemsBToSList(Map<String, ServiceProductS> newItemsMapS, String itemCatalogNum, SupplierProduct supplierProductSB) {
        newItemsMapS.put(itemCatalogNum, new ServiceProductS(supplierProductSB.getProductID(), supplierProductSB.getPrice().toString(), itemCatalogNum, convertDiscountsMapToList(supplierProductSB.getDiscountByAmount())));
    }

    private NavigableMap<Integer, Double> discountsFactory(ServiceProductS productS) {
        NavigableMap<Integer, Double> discounts = new TreeMap<>();
        List<String> lst = productS.getDiscountByAmount();
        for (String entry : lst) {
            String[] entries = entry.split(":");
            int key = Integer.parseInt(entries[0].trim());
            double value = Double.parseDouble(entries[1].trim());
            discounts.put(key, value);
        }
        return discounts;
    }

    public ResponseT<Supplier> addSupplier(ServiceSupplierCard supplierCard, ServiceAgreement agreement) {
        try {
            SupplierCard supplierCardB = suppliersMan.createSupplierCard(supplierCard.getId(), supplierCard.getName(), supplierCard.getAddress(), supplierCard.getBankAccount(), supplierCard.getPaymentMethod());
            Agreement agreementB = suppliersMan.createAnAgreement(agreement.getSupplierName(), agreement.getDeliveryDays(), agreement.getSupplyMethod());
            Supplier supplier = suppliersMan.addSupplier(supplierCardB, agreementB);
            int supplierID = supplier.getSupplierCard().getId();
            Map<String, ServiceContact> contactMapS = supplierCard.getContactsMap();
            contactMapS.forEach((s, contact) -> suppliersMan.addContactsToSupplier(supplierID, contact.getName(), contact.getEmail(), contact.getPhoneNumber()));
            Map<String, ServiceProductS> itemsMapS = agreement.getItems();
            itemsMapS.forEach((s, item) -> suppliersMan.addProductToAgreement(supplierID, item.getProductID(), item.getCatalogNumber(), Double.parseDouble(item.getPrice()), discountsFactory(item)));
            return ResponseT.fromValue(supplier);
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT removeSupplier(int supplierID) {
        try {
            boolean completed = suppliersMan.removeSupplier(supplierID);
            return ResponseT.fromValue(completed);
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }
    //AGREEMENT

    public Response addContactsToSupplier(int supplierID, String name, String email, String phoneNumber) {
        try {
            suppliersMan.addContactsToSupplier(supplierID, name, email, phoneNumber);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response removeSupplierContact(int supplierID, String name) {
        try {
            suppliersMan.removeSupplierContact(supplierID, name);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    //edit email if editEmail equals true, else edit phone number
    public Response editSupplierContacts(int supplierID, String name, String email, String phoneNumber, boolean editEmail) {
        try {
            suppliersMan.editSupplierContacts(supplierID, name, email, phoneNumber, editEmail);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }


    public Response changeSupplierBackAccount(int supplierID, String bankAccount) {
        try {
            suppliersMan.changeSupplierBackAccount(supplierID, bankAccount);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response changeSupplyMethod(int supplierID, String supplyMethod, List<DayOfWeek> days) {
        try {
            suppliersMan.changeSupplyMethod(supplierID, supplyMethod, days);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response changeSupplierPaymentMethod(int supplierID, String paymentMethod) {
        try {
            suppliersMan.changeSupplierPaymentMethod(supplierID, paymentMethod);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response changeAddress(int supplierID, String address) {
        try {
            suppliersMan.changeAddress(supplierID, address);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public ResponseT addItemToAgreement(int supplierID, int productID, String priceList, String catalogNumber, List<String> discounts) {
        try {
            NavigableMap<Integer, Double> discountsMap = convertDiscountsListToMap(discounts);
            SupplierProduct supplierProduct = suppliersMan.addProductToAgreement(supplierID, productID, catalogNumber, Double.parseDouble(priceList), discountsMap);
            return ResponseT.fromValue(supplierProduct);
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT removeItemFromAgreement(int supplierID, int productID) {
        try {
            SupplierProduct supplierProduct = suppliersMan.removeProductFromAgreement(supplierID, productID);
            return ResponseT.fromValue(supplierProduct);
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }

    public ResponseT<Boolean> isLegalSupplierID(int supplierID) {
        try {
            boolean ans = suppliersMan.isLegalSupplierID(supplierID);
            return ResponseT.fromValue(ans);
        } catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }
}
