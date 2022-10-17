package BussinessLayer.Objects;

import DataAccessLayer.Dal.SuperItemDAO;
import DataAccessLayer.Dal.ProductDAO;

import java.sql.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Product {
    private int productId;
    private String name;
    private List<Integer> items;
    private double sellPrice;
    private String manufacturer;
    private int saleId;
    private int amountStorage;
    private int amountShelf;
    private int minQuantity;
    private double weight;
    private Map<String,Integer> categoriesID;

    public Product(int productId, String name, String manufacturer, int minQuantity, double sellPrice, int saleId, int mainCategory, int subCat, int subSubCat,double weight) {
        this.productId = productId;
        this.name = name;
        this.manufacturer = manufacturer;
        this.saleId = saleId;
        this.sellPrice = sellPrice;
        this.amountStorage = 0;
        this.amountShelf = 0;
        this.minQuantity = minQuantity;
        this.items = new LinkedList<>();
        this.categoriesID = new HashMap<>();
        this.categoriesID.put("main",mainCategory);
        this.categoriesID.put("sub", subCat);
        this.categoriesID.put("subSub",subSubCat);
        this.weight = weight;
    }

    public int getProductId() {
        return productId;
    }

    public double getWeight(){ return  weight;}

    public void setWeight(double weight){
        this.weight = weight;
    }

    public int getSaleId() {
        return saleId;
    }


    public void setName(String Name) {
        this.name = Name;
    }

    public String getName() {
        return this.name;
    }

    public List<Integer> getItemsList() {
        return this.items;
    }


    public void setManufacturer(String Manufacturer) {

        this.manufacturer = Manufacturer;
        ProductDAO.getInstance().Update("MANUFACTURER", Manufacturer, this.productId);
    }

    public String getManufacturer() {
        return this.manufacturer;
    }

    public void setAmountStorage(int amountStorage) {
        this.amountStorage = amountStorage;
        alertToInviteMore();
    }

    public int getAmountStorage() {
        this.amountStorage = 0;
        this.amountShelf = 0;
        for(Item it:SuperItemDAO.getInstance().getAllSuperItemsOfProduct(this.productId)){
            if(it.getStorageShelf().equals("Storage")){
                this.amountStorage++;
            }else{
                this.amountShelf++;
            }
        }
        return this.amountStorage;
    }

    public void setAmountShelf(int AmountShelf) {
        this.amountShelf = AmountShelf;
        alertToInviteMore();

    }

    public int getAmountShelf() {
        this.amountStorage = 0;
        this.amountShelf = 0;
        for(Item it:SuperItemDAO.getInstance().getAllSuperItemsOfProduct(this.productId)){
            if(it.getStorageShelf().equals("Storage")){
                this.amountStorage++;
            }else{
                this.amountShelf++;
            }
        }
        return this.amountShelf;
    }

    public void setMinQuantity(int MinQuantity) {
        ProductDAO.getInstance().Update("MIN_QUANTITY", MinQuantity, this.productId);
        this.minQuantity = MinQuantity;
        alertToInviteMore();
    }

    public int getMinQuantity() {
        return this.minQuantity;
    }

    public Map<String, Integer> getCategory() {
        return this.categoriesID;
    }


    public void setMainCategory(int cat) {

        this.categoriesID.replace("main", this.categoriesID.get("main"), cat);
    }

    public void setSubCategory(int cat) {
        this.categoriesID.replace("sub", this.categoriesID.get("sub"), cat);
    }

    public void setSubSubCategory(int cat) {
        this.categoriesID.replace("subSub", this.categoriesID.get("subSub"), cat);
    }

    private void alertToInviteMore() {
        this.amountStorage = 0;
        this.amountShelf = 0;
        for(Item it:SuperItemDAO.getInstance().getAllSuperItemsOfProduct(this.productId)){
            if(it.getStorageShelf().equals("Storage")){
                this.amountStorage++;
            }else{
                this.amountShelf++;
            }
        }
        if (this.amountShelf + this.amountStorage < this.minQuantity)
            throw new IllegalArgumentException("you need to order more " + this.name);
    }

    private Item getItem(int id) {
        Item item = SuperItemDAO.getInstance().getSuperItemById(id);
        if (item == null)
            throw new IllegalArgumentException("this item isn't exist in the system");
        return item;
    }

    public void updateItemPlace(int id, String place) {

        SuperItemDAO.getInstance().UpdateItemLocation(id,this.productId,SuperItemDAO.getInstance().getSuperItemById(id).getStorageShelf(),place);
    }


    public void moveItemsToStorage(int productID, int amountToMove, String newPlace) {
        List<Item> itemsID = getAlStorageOrShelfItems(productID, false);
        if (getAmountShelf() < amountToMove) {
            throw new IllegalArgumentException("we currently dont have enough items of this product in the shelf you can move " + itemsID.size() + " items if you want");
        }
        if(getAmountShelf()>=amountToMove){
            int count = 0;
            for (Item item : itemsID) {
                if(item.getStorageShelf().equals("Shelf")){
                    if (count < amountToMove)
                        SuperItemDAO.getInstance().UpdateItemLocation(item.getId(), this.productId, "Storage", newPlace);
                    else
                        break;
                    count++;
                }
            }
        }
    }


    public void addItems(int amountToAdd, int ProID ,int OrderID,String Loc,int buyPrice , String storageShelf, Date expressionDate, boolean def) {
        this.amountStorage = 0;
        this.amountShelf = 0;
        for(Item it:SuperItemDAO.getInstance().getAllSuperItemsOfProduct(this.productId)){
            if(it.getStorageShelf().equals("Storage")){
                this.amountStorage++;
            }else{
                this.amountShelf++;
            }
        }
        for (int i = 0; i < amountToAdd; i++) {
            int newID =SuperItemDAO.getInstance().getMaxID()+1;
            this.items.add(newID);
            SuperItemDAO.getInstance().Insert(new Item(newID,ProID, OrderID,Loc,buyPrice,storageShelf,expressionDate,def));
            if (storageShelf.equals("Storage")) {
                this.amountStorage++;
            } else {
                amountShelf++;
            }
        }
    }

    public void removeItems(List<Integer> itemsID) {
        if (itemsID == null) {
            throw new IllegalArgumentException("you should choose the items you want to remove");
        }
        for (int Id : itemsID) {
            removeSpecificItem(Id);
        }
        this.amountStorage = 0;
        this.amountShelf = 0;
        for(Item it:SuperItemDAO.getInstance().getAllSuperItemsOfProduct(this.productId)){
            if(it.getStorageShelf().equals("Storage")){
                this.amountStorage++;
            }else{
                this.amountShelf++;
            }
        }
        alertToInviteMore();
    }

    public void removeSpecificItem(int itemID) {
        Item item = SuperItemDAO.getInstance().getSuperItemByIdAndProductID(itemID,this.productId);
        List<Item> itemsID = SuperItemDAO.getInstance().getAllSuperItemsOfProduct(this.productId);
        if (item.getStorageShelf().equals("Storage")) {
            SuperItemDAO.getInstance().Delete(item);
            for(int i=0;i<itemsID.size();i++){
                if(itemsID.get(i).getId()==itemID){
                    itemsID.remove(i);
                }
            }

        } else if (item.getStorageShelf().equals("Shelf")) {
            SuperItemDAO.getInstance().Delete(item);
            for(int i=0;i<itemsID.size();i++){
                if(itemsID.get(i).getId()==itemID){
                    itemsID.remove(i);
                }
            }
        }
        this.amountStorage = 0;
        this.amountShelf = 0;
        for(Item it:SuperItemDAO.getInstance().getAllSuperItemsOfProduct(this.productId)){
            if(it.getStorageShelf().equals("Storage")){
                this.amountStorage++;
            }else{
                this.amountShelf++;
            }
        }
        alertToInviteMore();
    }

    public void moveOneItemToShelf(int ID, String newPlace) {
        this.amountStorage = 0;
        this.amountShelf = 0;
        for(Item it:SuperItemDAO.getInstance().getAllSuperItemsOfProduct(this.productId)){
            if(it.getStorageShelf().equals("Storage")){
                this.amountStorage++;
            }else{
                this.amountShelf++;
            }
        }
        if (SuperItemDAO.getInstance().getSuperItemById(ID).getStorageShelf().equals("Storage")) {
            SuperItemDAO.getInstance().UpdateItemLocation(ID,this.productId,"Shelf",newPlace);
            this.amountStorage--;
            this.amountShelf++;
        } else {
            throw new IllegalArgumentException("the item wasn't in the Storage");
        }
    }

    public void moveOneItemToStorage(int ID, String newPlace) {
        this.amountStorage = 0;
        this.amountShelf = 0;
        for(Item it:SuperItemDAO.getInstance().getAllSuperItemsOfProduct(this.productId)){
            if(it.getStorageShelf().equals("Storage")){
                this.amountStorage++;
            }else{
                this.amountShelf++;
            }
        }
        if (SuperItemDAO.getInstance().getSuperItemById(ID).getStorageShelf().equals("Shelf")) {
            SuperItemDAO.getInstance().UpdateItemLocation(ID,this.productId,"Storage",newPlace);
            this.amountStorage++;
            this.amountShelf--;
        } else {
            throw new IllegalArgumentException("the item wasn't in the shelf");
        }
    }

    private List<Item> getAlStorageOrShelfItems(int productID, boolean shOrSt) {
        List<Item> itemsID = SuperItemDAO.getInstance().getAllSuperItemsOfProduct(productID);
        List<Item> relevantItems = new LinkedList<>();
        if (!shOrSt) {
            for (Item item : itemsID) {
                if (item.getStorageShelf().equals("Shelf")) {
                    relevantItems.add(item);
                }
            }
        } else {
            for (Item item : itemsID) {
                if (item.getStorageShelf().equals("Storage")) {
                    relevantItems.add(item);
                }
            }
        }
        return itemsID;
    }

    public void moveItemsToShelf(int productID, int amountToMove, String newPlace) {
        List<Item> itemsID = getAlStorageOrShelfItems(productID, true);
        if (getAmountStorage() < amountToMove) {
            throw new IllegalArgumentException("we currently dont have enough items of this product in the storage you can move " + itemsID.size() + " items if you want");
        }
        if(getAmountStorage()>=amountToMove){
            int count = 0;
            for (Item item : itemsID) {
                if(item.getStorageShelf().equals("Storage")){
                    if (count < amountToMove)
                        SuperItemDAO.getInstance().UpdateItemLocation(item.getId(), this.productId, "Shelf", newPlace);
                    else
                        break;
                    count++;
                }
            }
        }
    }


    public void setSellPrice(double SellPrice) {
        this.sellPrice = SellPrice;
    }

    public double getSellPrice() {
        return this.sellPrice;
    }
}
