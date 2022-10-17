package ServiceLayer.ServiceObjects;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

public class ServiceAgreement {
    private String SupplierName;
    private List<DayOfWeek> deliveryDays;
    private String supplyMethod;
    private Map<String, ServiceProductS> items;


    public ServiceAgreement(String supplierName, List<DayOfWeek> deliveryDays, String supplyMethod, Map<String, ServiceProductS> items) {
        SupplierName = supplierName;
        this.deliveryDays = deliveryDays;
        this.supplyMethod = supplyMethod;
        this.items = items;
    }

    public String getSupplierName() {
        return SupplierName;
    }

    public void setSupplierName(String supplierName) {
        SupplierName = supplierName;
    }

    public List<DayOfWeek> getDeliveryDays() {
        return deliveryDays;
    }

    public void setDeliveryDays(List<DayOfWeek> deliveryDays) {
        this.deliveryDays = deliveryDays;
    }

    public String getSupplyMethod() {
        return supplyMethod;
    }

    public void setSupplyMethod(String supplyMethod) {
        this.supplyMethod = supplyMethod;
    }

    public Map<String, ServiceProductS> getItems() {
        return items;
    }

    public void setItems(Map<String, ServiceProductS> items) {
        this.items = items;
    }

}
