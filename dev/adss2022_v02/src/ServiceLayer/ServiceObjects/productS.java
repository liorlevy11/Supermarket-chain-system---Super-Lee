package ServiceLayer.ServiceObjects;


import java.util.HashMap;
import java.util.Map;

public class productS {
    private Map<Integer, itemS> items;

    private double sellPrice;
    private String name;
    private String manufacturer;
    private int amountStorage;
    private int amountShelf;
    private int minQuantity;
    private int productID;
    private Map<String, categoryS> category;

    public productS(int productID,String Nname, String Nmanufacturer, int Nminqua, double Nsell){
        this.productID = productID;
        this.name = Nname;
        this.sellPrice = Nsell;
        this.manufacturer = Nmanufacturer;
        this.amountStorage = 0;
        this.amountShelf = 0;
        this.minQuantity = Nminqua;
        this.items = new HashMap<>();
        this.category = new HashMap<>();
    }

    public Map<Integer, itemS> getItems() {
        return items;
    }

    public String getName() {
        return name;
    }

    public int getProductID() {
        return productID;
    }
}

