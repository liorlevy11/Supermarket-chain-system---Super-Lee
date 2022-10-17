package ServiceLayer.ServiceObjects;

public class ServiceContact {
    private int supplierID;
    private String name;
    private String email;
    private String phoneNumber;

    public ServiceContact(int supplierID,String name, String email, String phoneNumber) {
        this.supplierID = supplierID;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

}
