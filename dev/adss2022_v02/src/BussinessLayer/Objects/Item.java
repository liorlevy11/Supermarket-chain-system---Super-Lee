package BussinessLayer.Objects;

import DataAccessLayer.Dal.SuperItemDAO;

import java.sql.Date;

enum StorageORShelf {
    Storage,
    Shelf
}

public class Item {
    private int id;
    private int productId;
    private int orderid;
    private double buyPrice;
    private String location;
    private StorageORShelf storageOrShelf;
    private Date expressionDate;
    private boolean defective;
    public Item(int ID, int proID,int OrderID,String Location,double buyPrice,String StorageorShelf, Date ExpDate, boolean def){
        this.buyPrice = buyPrice;
        this.id = ID;
        this.productId = proID;
        this.orderid = OrderID;
        this.location = Location;
        this.expressionDate = ExpDate;
        if (StorageorShelf.equals("Storage")) {
            storageOrShelf = StorageORShelf.Storage;
        } else {
            storageOrShelf = StorageORShelf.Shelf;
        }
        this.defective = def;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public int getOrderid() {
        return orderid;
    }



    public boolean getDefective() {
        return defective;
    }


    public String getStorageShelf() {
        if (storageOrShelf.equals(StorageORShelf.Storage)) {
            return "Storage";
        } else {
            return "Shelf";
        }
    }


    public int getId() {
        return this.id;
    }

    public String getLocation() {
        return this.location;
    }

    public Date getExpressionDate() {
        return this.expressionDate;
    }

    public int getProductid() {
        return productId;
    }

    public void setOrderID(int orderID){
        this.orderid = orderID;
    }
}
