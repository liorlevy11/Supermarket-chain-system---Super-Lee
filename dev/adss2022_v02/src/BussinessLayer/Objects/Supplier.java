package BussinessLayer.Objects;

public class Supplier {
    private SupplierCard supplierCard;
    private Agreement agreement;


    public Supplier(SupplierCard supplierCard, Agreement agreement) {
        this.supplierCard = supplierCard;
        this.agreement = agreement;

    }

    public Agreement getAgreement() {
        return agreement;
    }

    public SupplierCard getSupplierCard() {
        return supplierCard;
    }

}
