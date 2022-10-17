package BussinessLayer.Objects;

public class SupplierCard {
    private int id;
    private String name;
    private String address;
    private PaymentMethod paymentMethod;
    private String bankAccount;


    public SupplierCard(int id, String name, String address,String bankAccount,PaymentMethod paymentMethod) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.bankAccount = bankAccount;
        this.paymentMethod = paymentMethod;
    }



    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public String getBankAccount() {
        return bankAccount;
    }

}
