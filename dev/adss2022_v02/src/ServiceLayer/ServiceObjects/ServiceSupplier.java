package ServiceLayer.ServiceObjects;

public class ServiceSupplier {

    private ServiceSupplierCard supplierCard;
    private ServiceAgreement agreement;


    public ServiceSupplier(ServiceSupplierCard supplierCard,ServiceAgreement agreement) {
        this.supplierCard = supplierCard;
        this.agreement = agreement;

    }

    public ServiceSupplierCard getSupplierCard() {
        return supplierCard;
    }

    public void setSupplierCard(ServiceSupplierCard supplierCard) {
        this.supplierCard = supplierCard;
    }

    public ServiceAgreement getAgreement() {
        return agreement;
    }

    public void setAgreement(ServiceAgreement agreement) {
        this.agreement = agreement;
    }
}
