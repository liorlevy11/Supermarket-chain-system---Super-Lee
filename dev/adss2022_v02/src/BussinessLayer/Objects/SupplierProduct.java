package BussinessLayer.Objects;

import java.util.Map;
import java.util.NavigableMap;

public class SupplierProduct {
    private int productID;
    private int supplierID;
    private String name;
    private Double price;
    private String catalogNumber;
    private NavigableMap<Integer,Double> discountByAmount;

    public SupplierProduct(int productID,int supplierID,String name, Double price, String catalogNumber, NavigableMap<Integer, Double> discountByAmount) {
        this.productID = productID;
        this.supplierID = supplierID;
        this.name = name;
        this.price = price;
        this.catalogNumber = catalogNumber;
        this.discountByAmount = discountByAmount;
    }

    public int getProductID() {
        return productID;
    }

    public int getSupplierID() {
        return supplierID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public NavigableMap<Integer, Double> getDiscountByAmount() {
        return discountByAmount;
    }

    public String discountsToString() {
        String dis = "";

        for (Map.Entry<Integer, Double> pair : discountByAmount.entrySet()) {
            dis += pair.getKey() + ":" + pair.getValue() + ",";
        }

        return dis;
    }

    public String toString() {
        String str = "product--- Name: " + name + ", price: " + price +  ", catalogNumber: " + catalogNumber +", discountByAmount: " + discountsToString() + "\n";
        return str;
    }
}
