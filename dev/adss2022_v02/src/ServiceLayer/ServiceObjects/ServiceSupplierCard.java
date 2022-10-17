package ServiceLayer.ServiceObjects;

import java.util.Map;

public class ServiceSupplierCard {
    private int id;
    private String name;
    private String address;
    private String paymentMethod;
    private String bankAccount;
    private Map<String, ServiceContact> contactsMap;

    public ServiceSupplierCard(int id, String name, String address,String bankAccount,String paymentMethod,Map<String,ServiceContact> contactMap) {
        this.id = id;
        this.name = name;
        this.contactsMap = contactMap ;
        this.address = address;
        this.bankAccount = bankAccount;
        this.paymentMethod = paymentMethod;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Map<String, ServiceContact> getContactsMap() {
        return contactsMap;
    }

    public void setContactsMap(Map<String, ServiceContact> contactsMap) {
        this.contactsMap = contactsMap;
    }

}
