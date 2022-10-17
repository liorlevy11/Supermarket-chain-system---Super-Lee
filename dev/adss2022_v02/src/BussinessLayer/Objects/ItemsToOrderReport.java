package BussinessLayer.Objects;

import DataAccessLayer.Dal.SuperItemDAO;
import DataAccessLayer.Dal.ProductDAO;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

public class ItemsToOrderReport extends Report {
    //Product name and the quantity in the store
    Map<Integer,Integer> ProductToOrder;
    public ItemsToOrderReport(Date someDate, int ID, Map<Integer, Integer> productToOr){
        super(someDate,ID);
        ProductToOrder = new HashMap<Integer,Integer>();
        for(Integer na: productToOr.keySet()){
            ProductToOrder.putIfAbsent(na,productToOr.get(na));
        }
        for(Product pro: ProductDAO.getInstance().getAllProducts()){
            for(Item itm: SuperItemDAO.getInstance().getAllSuperItemsOfProduct(pro.getProductId())){
                int calc = pro.getMinQuantity()-SuperItemDAO.getInstance().getAllSuperItemsOfProduct(pro.getProductId()).size();
                if(calc>0){
                    ProductToOrder.putIfAbsent(pro.getProductId(),pro.getMinQuantity()-SuperItemDAO.getInstance().getAllSuperItemsOfProduct(pro.getProductId()).size());
                }
            }
        }
    }
    public Map<Integer,Integer> getProductToOrder(){
        return this.ProductToOrder;
    }
    public boolean ContainProduct(int pro){
        return ProductToOrder.containsKey(pro);
    }
    public int ProductQuantity(int pro){
        return ProductToOrder.get(pro);
    }

}
