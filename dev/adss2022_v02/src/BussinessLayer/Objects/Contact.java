package BussinessLayer.Objects;

public class Contact {
    private int supplierId;
    private String name;
    private String email;
    private String phoneNumber;

    public Contact(int supplierId, String name, String email, String phoneNumber) {
        this.supplierId = supplierId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getSupplierId() {
        return supplierId;
    }
}
