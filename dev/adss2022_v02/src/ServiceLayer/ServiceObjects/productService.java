package ServiceLayer.ServiceObjects;

import BussinessLayer.Objects.Category;
import BussinessLayer.Objects.Product;
import BussinessLayer.Managers.CategoryManager;
import BussinessLayer.Managers.ProductManager;
import DataAccessLayer.Dal.CategoryDAO;
import ServiceLayer.Response;
import ServiceLayer.ResponseT;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

public class productService {
    private ProductManager productManager;
    private CategoryManager categoryManager;

    public productService(){
        this.productManager = ProductManager.getInstance();
        this.categoryManager = CategoryManager.getInstance();
    }
    public Response loadData(){
        try {
            categoryManager.loadData();
            productManager.loadData();
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }


    public Response addProduct(String name, String manufacturer, int minQuantity, double sellPrice,
                               String category, String subCat, String subSubCaty,double weight){
        try {
            int catID = -1;
            int subID = -1;
            int subsubID = -1;
            try{
                catID = Integer.parseInt(category);
            }catch(Exception e){
                catID = this.categoryManager.addCategory(category,"main").getID();
            }
            try{
                subID = Integer.parseInt(subCat);
            }catch(Exception e){
                subID = this.categoryManager.addCategory(subCat,"sub").getID();
            }
            try{
                subsubID = Integer.parseInt(subSubCaty);
            }catch(Exception e){
                subsubID = this.categoryManager.addCategory(subSubCaty,"subSub").getID();
            }
            this.productManager.createProduct(name,  manufacturer,minQuantity, sellPrice, catID, subID, subsubID,weight);
            return new Response();
        }
        catch (Exception e){
            return new Response(e.getMessage());
        }

    }

    public Response removeProduct(int Proid){
        try {
            this.productManager.removeProduct(Proid);
            return new Response();
        }
        catch (Exception e){
            return new Response(e.getMessage());
        }

    }

    public Response updateSellPrice(int ProID, double sellPrice){
        try {
            this.productManager.updateSellPrice(ProID,sellPrice);
            return new Response();
        }
        catch (Exception e){
            return new Response(e.getMessage());
        }
    }

    public Response updateCategory(int ProID,String category,String categoryType){
        try {
            Category cat = null;
            try{
                cat = CategoryDAO.getInstance().getCategoryById(Integer.parseInt(category));
            }catch(Exception e){
                cat = this.categoryManager.addCategory(category,"main");
            }
            this.productManager.updateCategory(ProID ,cat.getID(),cat.getName(), categoryType);
            return new Response();
        }
        catch (Exception e){
            return new Response(e.getMessage());
        }
    }

    public Response moveItem(int ProID, int amountToMove, String newPlace,boolean toShelf){
        try {
            if (toShelf)
                this.productManager.moveProductToShelf(ProID, amountToMove, newPlace);
            else
                this.productManager.moveProductToStorage(ProID, amountToMove, newPlace);
            return new Response();
        }
        catch (Exception e){
            return new Response(e.getMessage());
        }
    }
    public Response moveOneItem(int ProID, int ItemID, String newPlace,boolean toShelf){
        try {
            if (toShelf)
                this.productManager.moveOneItemToShelf(ProID, ItemID, newPlace);
            else
                this.productManager.moveOneItemToStorage(ProID, ItemID, newPlace);
            return new Response();
        }
        catch (Exception e){
            return new Response(e.getMessage());
        }
    }
    //String from should be Storage or Shelf
    public Response removeItems(int ProID, List<Integer> itemsToRemove){
        try {
            this.productManager.removeItems(ProID, itemsToRemove);
            return new Response();
        }
        catch (Exception e){
            return new Response(e.getMessage());
        }
    }



    public Response removeSpecificItem(int ProID ,int itemID){
        try {
            this.productManager.removeSpecificItem(ProID, itemID);
            return new Response();
        }
        catch (Exception e){
            return new Response(e.getMessage());
        }
    }

    public ResponseT<List<productS>> getAllProducts(){
        List<productS> products = new LinkedList<>();
        List<Product> prod = this.productManager.getAllProduct();
        for (Product product:prod) {
            products.add(new productS(product.getProductId(),product.getName(),product.getManufacturer(),product.getMinQuantity(),product.getSellPrice()));
        }
        try {
            return new ResponseT<List<productS>>(products,"");
        }
        catch (Exception e) {
            return new ResponseT<>(products, e.getMessage());
        }
    }

    public ResponseT<productS> getProductByID(int productID){
        try {
            Product product = productManager.getProduct(productID);
            productS serviceProduct = new productS(product.getProductId(),product.getName(),product.getManufacturer(),product.getMinQuantity(),product.getSellPrice());
            return new ResponseT<productS>(serviceProduct, "");
        }
        catch (Exception e){
            return new ResponseT<productS>(null, e.getMessage());
        }
    }

    public Response addItems(int ProID, int amountToAdd, int OrderID, String place, int buyPrice, Date expressionDate, String StorageShelf, boolean def){
        try {
            this.productManager.addItems(amountToAdd,ProID ,OrderID, place, buyPrice, expressionDate,StorageShelf, def);
            return new Response();
        }
        catch (Exception e){
            return new Response(e.getMessage());
        }
    }

    public Response changeCategory(int ProID , String newCat, String typeCat){
        try {
            Category cat = null;
            try{
                cat = CategoryDAO.getInstance().getCategoryById(Integer.parseInt(newCat));
            }catch(Exception e){
                cat = this.categoryManager.addCategory(newCat,typeCat);
            }
            this.productManager.updateCategory(ProID,cat.getID(), cat.getName(),typeCat);
            return new Response();
        }
        catch (Exception e){
            return new Response(e.getMessage());
        }
    }
    public ResponseT<Double> getPriceAfterDiscount(int productID, double Discount){
        return new ResponseT<Double>(this.productManager.getPriceAfterDiscount(productID,Discount),"");
    }

    public void deleteItemsOrder(int orderID) {
        this.productManager.deleteItemsOrder(orderID);
    }
}
