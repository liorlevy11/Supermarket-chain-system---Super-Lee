package BussinessLayer.Objects;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class Agreement {
    private String SupplierName;
    private List<DayOfWeek> deliveryDays;
    private SupplyMethod supplyMethod;
    private List<String> productsCatalogNum;//catalogNumber - product

    public Agreement(String supplierName, List<DayOfWeek> deliveryDays, SupplyMethod supplyMethod, List<String> productsCatalogNum) {
        SupplierName = supplierName;
        this.deliveryDays = deliveryDays;
        this.supplyMethod = supplyMethod;
        this.productsCatalogNum = productsCatalogNum;
    }
    public Agreement(String supplierName, List<DayOfWeek> deliveryDays, SupplyMethod supplyMethod) {
        SupplierName = supplierName;
        this.deliveryDays = deliveryDays;
        this.supplyMethod = supplyMethod;
        this.productsCatalogNum = new ArrayList<>();
    }
    public List<DayOfWeek> getDeliveryDays() {
        return deliveryDays;
    }

    public String getSupplierName() {
        return SupplierName;
    }

    public SupplyMethod getSupplyMethod() {
        return supplyMethod;
    }

    public void setProductsCatalogNum(List<String> productsCatalogNum){
        this.productsCatalogNum = productsCatalogNum;
    }

}
