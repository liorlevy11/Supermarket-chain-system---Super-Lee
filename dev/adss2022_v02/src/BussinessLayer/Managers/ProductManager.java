package BussinessLayer.Managers;

import BussinessLayer.Objects.Category;
import BussinessLayer.Objects.Item;
import BussinessLayer.Objects.Product;
import BussinessLayer.Objects.CategoryType;
import DataAccessLayer.Dal.ProductDAO;
import DataAccessLayer.Dal.CategoryDAO;
import DataAccessLayer.Dal.SuperItemDAO;

import java.sql.Date;
import java.util.List;

public class ProductManager {
    private CategoryManager categoryManagers = CategoryManager.getInstance();
    private static ProductManager ManagerItems = null;
    private ProductDAO productDAO = ProductDAO.getInstance();

    private ProductManager() {
    }

    public static ProductManager getInstance() {
        if (ManagerItems == null)
            ManagerItems = new ProductManager();
        return ManagerItems;
    }
    public void DelData(){
        productDAO.Delete();
    }
    public void loadData(){

        createProduct("Animal Food", "shlomo",3,15.0,1,5,8,1.01);
        createProduct("Puppies", "moshe",1,1500.0,2,4,7,1.01);
        createProduct("OrangeJuice","zipi",1,7.0,3,4,7,1.01);
        createProduct("Pineapple","gal", 3,20.0,1,6,9,1.01);

        addItems(1,1,3,"Shelf-A1",10,Date.valueOf("2011-10-05"),"Shelf",true);
        addItems(1,1,3,"Shelf-A1",10,Date.valueOf("2040-10-05"),"Shelf",false);
        addItems(1,1,3,"Storage-B1",10,Date.valueOf("2022-10-05"),"Storage",true);
        addItems(1,1,3,"Storage-B1",10,Date.valueOf("2011-10-05"),"shelf",true);

        addItems(1,2,3,"Shelf-A3",10,Date.valueOf("2023-10-05"),"Shelf",true);
        addItems(1,2,3,"Storage-C2",10,Date.valueOf("2023-10-05"),"Shelf",false);

        addItems(1,3,3,"Storage-C4",10,Date.valueOf("2023-10-05"),"Shelf",false);
        addItems(1,4,3,"Storage-A3",10,Date.valueOf("2023-10-05"),"Shelf",false);

        addItems(1,4,3,"Shelf-A4",10,Date.valueOf("2023-10-05"),"Storage",false);
        addItems(1,4,3,"Shelf-A4",10,Date.valueOf("2023-10-05"),"shelf",false);

    }

    public Product createProduct(String name, String manufacturer,int MinQuantity, double sellPrice, int category, int subCat, int subSubCat,double weight) {
        Product product = new Product(productDAO.getMaxID()+1,name, manufacturer, MinQuantity, sellPrice,-1,category,subCat, subSubCat,weight);
        productDAO.Insert(product);
        return product;
    }

    public List<Product> getAllProduct(){
        return this.productDAO.getAllProducts();
    }

    public Product getProduct(Integer ID) {
        Product product = productDAO.getProductById(ID);
        return product;
    }

    public boolean removeProduct(Integer ID) {
        boolean deleteItems = SuperItemDAO.getInstance().deleteByProduct(ID);
        return productDAO.Delete(ID) && deleteItems;
    }

    private boolean isIProductExist(int ID) {
        if (productDAO.getProductById(ID) == null) {
            throw new IllegalArgumentException("this product isn't exist in the system");
        }
        return true;
    }

    public void updateItemName(int ID, String newName) {
        Product product = productDAO.getProductById(ID);
        product.setName(newName);
        productDAO.Update("NAME",newName,ID);
    }

    public void updateItemManufacturer(int ID, String newManufacturer) {
        Product product = productDAO.getProductById(ID);
        product.setManufacturer(newManufacturer);
        productDAO.Update("MANUFACTURER",newManufacturer,ID);
    }

    public void calculateMinQuantity(int ID, double popularity, double supplierTime) {
        Product product = productDAO.getProductById(ID);
        product.setMinQuantity((int) (popularity + supplierTime));
        productDAO.Update("MIN_QUANTITY",(int) (popularity + supplierTime),ID);
    }


    public void updateCategory(int productID,int categoryID,String categoryName, String categoryType) {
        if(!categoryType.equalsIgnoreCase("subsub")  && !categoryType.equalsIgnoreCase("sub") && !categoryType.equalsIgnoreCase("main"))
            throw new IllegalArgumentException("The category type: " + categoryType +" is illegal. please choose one of the following: main,sub,subSub");
        switch (categoryType.toLowerCase()) {

            case "main":
                updateMainCategory(productID,categoryID,categoryName);
                break;
            case "sub":
                updateSubCategory(productID, categoryID, categoryName);
                break;
            case "subsub":
                updateSubSubCategory(productID, categoryID, categoryName);
        }
    }

    public void updateMainCategory(int productID,int categoryID, String categoryName) {
        Product product = this.getProduct(productID);
        List<Category> catl = CategoryDAO.getInstance().getAllCategories();
        Category getC=null;
        for(Category c:catl){
            if(c.getID()==categoryID){
                getC = c;
            }
        }
        if(getC==null){
            CategoryDAO.getInstance().Insert(new Category(categoryID,categoryName, CategoryType.Main, -1));
        }
        product.setSubSubCategory(categoryID);
        ProductDAO.getInstance().UpdateProductCategory(productID, getC.getID(),"main");
    }
    public void updateSubCategory(int productID,int categoryID, String categoryName){
        Product product = this.getProduct(productID);
        List<Category> catl = CategoryDAO.getInstance().getAllCategories();
        Category getC=null;
        for(Category c:catl){
            if(c.getID()==categoryID){
                getC = c;
            }
        }
        if(getC==null){
            CategoryDAO.getInstance().Insert(new Category(categoryID,categoryName, CategoryType.Sub, -1));
        }
        product.setSubSubCategory(categoryID);
        ProductDAO.getInstance().UpdateProductCategory(productID, getC.getID(),"sub");
    }
    public void updateSubSubCategory(int productID,int categoryID, String categoryName) {
        Product product = this.getProduct(productID);
        List<Category> catl = CategoryDAO.getInstance().getAllCategories();
        Category getC=null;
        for(Category c:catl){
            if(c.getID()==categoryID){
                getC = c;
            }
        }
        if(getC==null){
            CategoryDAO.getInstance().Insert(new Category(categoryID,categoryName, CategoryType.SubSub, -1));
        }
        product.setSubSubCategory(categoryID);
        ProductDAO.getInstance().UpdateProductCategory(productID, getC.getID(),"subSub");
    }
    public void updateSellPrice(int productID, double newPrice) {
        Product product = productDAO.getProductById(productID);
        product.setSellPrice(newPrice);
        productDAO.Update("SELL_PRICE",newPrice,productID);
    }

    public void moveProductToStorage(int productID, int amountToMove, String newPlace){
        Product product = productDAO.getProductById(productID);
        product.moveItemsToStorage(productID,amountToMove,newPlace);
    }

    public void moveProductToShelf(int productID,int amountToMove, String newPlace){
        getProduct(productID).moveItemsToShelf(productID,amountToMove, newPlace);
    }
    public void moveOneItemToShelf(int productID,int ItemID, String newPlace){
        getProduct(productID).moveOneItemToShelf(ItemID, newPlace);
    }
    public void moveOneItemToStorage(int productID,int ItemID, String newPlace){
        getProduct(productID).moveOneItemToStorage(ItemID, newPlace);
    }

    //String from should be Storage or Shelf
    public void removeItems(int productID,List<Integer> itemsID){
        getProduct(productID).removeItems(itemsID);
    }

    public void removeSpecificItem(int productID ,int itemID){
        getProduct(productID).removeSpecificItem(itemID);
    }

    public void addItems(int amountToAdd,int productID,int OrderID , String place, int buyPrice, Date expressionDate, String StorageShelf, boolean def){
        getProduct(productID).addItems(amountToAdd,productID,OrderID, place,buyPrice,StorageShelf, expressionDate, def);
    }

    public void moveItem(int productID,int id,String newPlace){
        getProduct(productID).updateItemPlace(id,newPlace);
    }

    public double getPriceAfterDiscount(int productID,double Discount){
        double curPrice = this.getProduct(productID).getSellPrice();
        return curPrice*((100-Discount)/100);
    }

    public void deleteItemsOrder(int orderID) {
        try {
            List<Item> items = SuperItemDAO.getInstance().getSuperItemsByOrderID(orderID);
            for(Item item:items)
                SuperItemDAO.getInstance().UpdateOrderID(item.getId(),-1);
        }
        catch (Exception e){

        }
    }
}
