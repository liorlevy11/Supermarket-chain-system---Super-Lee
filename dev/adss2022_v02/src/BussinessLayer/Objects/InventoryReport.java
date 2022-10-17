package BussinessLayer.Objects;

import DataAccessLayer.Dal.SuperItemDAO;
import DataAccessLayer.Dal.ProductDAO;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryReport extends Report {
    //the name of the categories that would be in the report
    List<Integer> categoryID;
    //list of Products that would be shown in the report, and the sum
    Map<Integer, Integer> ProductList;
    //building the report in the constructor
    public InventoryReport(List<Integer> categories, Date someDate, int ID){
        super(someDate, ID);
        ProductList = new HashMap<Integer,Integer>();
        for (int cat:categories){
            for(Product pro: ProductDAO.getInstance().getAllProductsOfCategory(cat)){
                for(Item itm: SuperItemDAO.getInstance().getAllSuperItemsOfProduct(pro.getProductId())){
                    ProductList.putIfAbsent(pro.getProductId(),SuperItemDAO.getInstance().getAllSuperItemsOfProduct(pro.getProductId()).size());
                }
            }
        }
        categoryID = categories;
    }
    public Map<Integer,Integer> getProductList(){
        return this.ProductList;
    }
    public boolean ContainProduct(int proID){
        return ProductList.containsKey(proID);
    }
    public int getQuantityOfProduct(int proID){
        return ProductList.get(proID);
    }

}
