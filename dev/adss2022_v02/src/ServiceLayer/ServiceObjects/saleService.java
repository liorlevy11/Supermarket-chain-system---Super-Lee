package ServiceLayer.ServiceObjects;

import BussinessLayer.Managers.SaleManager;
import ServiceLayer.Response;
import ServiceLayer.ResponseT;

public class saleService {
    private SaleManager saleManager;
    public saleService(){
        this.saleManager = SaleManager.getInstance();
    }

    public Response loadData(){
        try {
            saleManager.loadData();
            return new Response();
        }
        catch (Exception e){
            return new Response(e.getMessage());
        }
    }

    public Response AddSale(String StartDateStr, String EndDateStr, String Category, String Product, double Discount){
        this.saleManager.AddSale(StartDateStr,EndDateStr,Category,Product,Discount);
        return new Response("");
    }
    public Response RemoveSale(int id){
        boolean rem = this.saleManager.RemoveSale(id);
        if(rem){
            return new Response("");
        }else{
            return new Response("Fail");
        }
    }
    public ResponseT<Integer> SaleOnProductAtDate(int ProductID, String ThisDate){
        int SaleID = saleManager.SaleOnProductAtDate(ProductID,ThisDate);
        if(SaleID==-1){
            return new ResponseT<Integer>(null,"there is no sale");
        }else{
            return new ResponseT<Integer>(SaleID,"");
        }
    }
    public ResponseT<Integer> SaleOnCategoryAtDate(Integer parCategory, String ThisDateStr){
        int SaleID = saleManager.SaleOnCategoryAtDate(parCategory,ThisDateStr);
        if(SaleID==-1){
            return new ResponseT<Integer>(null,"there is no sale");
        }else{
            return new ResponseT<Integer>(SaleID,"");
        }
    }
    public ResponseT<Double> getSaleDiscount(int SaleID){
        double dis = saleManager.getSaleDiscount(SaleID);
        if(dis==0){
            return new ResponseT<Double>(dis,"there is no sale on that product");
        }else{
            return new ResponseT<Double>(dis,"");
        }
    }
}
