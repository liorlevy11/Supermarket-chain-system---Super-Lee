package ServiceLayer.ServiceObjects;

import java.util.List;

public class ServiceProductS {
    private int productID;
    private int supplierID;
    private String price;
    private String catalogNumber;
    private List<String> discountByAmount;

    public ServiceProductS(int productID, String price, String catalogNumber, List<String> discountByAmount) {
        this.productID = productID;
        this.price = price;
        this.catalogNumber = catalogNumber;
        this.discountByAmount = discountByAmount;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public void setCatalogNumber(String catalogNumber) {
        this.catalogNumber = catalogNumber;
    }

    public void setDiscountByAmount(List<String> discountByAmount) {
        this.discountByAmount = discountByAmount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }


    public List<String> getDiscountByAmount() {
        return discountByAmount;
    }


    public int getSupplierID() {
        return supplierID;
    }

    public int getProductID() {
        return productID;
    }
}
