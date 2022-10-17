package BussinessLayer.Managers;

import BussinessLayer.Objects.Product;
import BussinessLayer.Objects.Sale;
import DataAccessLayer.Dal.ProductDAO;
import DataAccessLayer.Dal.CategoryDAO;
import DataAccessLayer.Dal.SaleDAO;


import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

public class SaleManager {
    private int IdCounter = 1;
    private static SaleManager ManagerSales = null;
    private SaleManager(){

    }
    public static SaleManager getInstance(){
        if(ManagerSales==null){
            ManagerSales = new SaleManager();
        }
        return ManagerSales;
    }
    public void loadData(){
        AddSale("2022-05-16","2022-05-18","2","Animal Food,Puppies",5.0);
        AddSale("2022-05-17","2022-05-19","1","none",3.0);

    }
    //the category and the Product strings should be in format <String>,<String>,<String>,...
    //the date should be in format <year>-<month>-<day>
    public void AddSale(String StartDateStr, String EndDateStr, String CategoryString, String ProductString, double Discount){
        Date startDate;
        Date endDate;
        startDate = Date.valueOf(StartDateStr);
        endDate = Date.valueOf(EndDateStr);
        List<Integer> Category = new LinkedList<Integer>();
        List<Integer> Product = new LinkedList<Integer>();
        String[] CategoryNamesArr = CategoryString.split(",");
        for(String str:CategoryNamesArr){
            if(!str.isEmpty()){
                Category.add(Integer.parseInt(str));
            }
        }
        String[] ProductsIDsArr = ProductString.split(",");
        for(String str:ProductsIDsArr){
            if(!str.isEmpty()){
                try{
                    Product.add(Integer.parseInt(str));
                }catch(Exception e){}
            }
        }
        IdCounter = SaleDAO.getMaxID()+1;
        Sale sale = new Sale(IdCounter,startDate,endDate,Discount);
        SaleDAO.getInstance().Insert(sale);
        for(int pint:Product){
            ProductDAO.getInstance().UpdateProductSale(pint,sale.getSaleID());
        }
        for(int cint:Category){
            CategoryDAO.getInstance().Update("SALE_ID",sale.getSaleID(),cint);
        }
        ManagerSales.IdCounter++;
    }
    public double getSaleDiscount(int Id){
        List<Sale> sales = SaleDAO.getInstance().getAllSales();
        IdCounter = SaleDAO.getMaxID()+1;
        for(Sale sale:sales){
            if(sale.getSaleID()==Id){
                return sale.getDiscount();
            }
        }
        return 0;
    }
    //checking sale on item in some date(or the item category)
    //the Product string should be in format <String>,<String>,<String>,...
    //the date should be in format <year>-<month>-<day>
    public int SaleOnProductAtDate(int ProductID, String DateString){
        List<Sale> sales =  SaleDAO.getInstance().getAllSales();
        IdCounter = SaleDAO.getMaxID()+1;
        Date ThisDate;
        ThisDate = Date.valueOf(DateString);
        Product pro = ProductManager.getInstance().getProduct(ProductID);
        for(Sale sale:sales){
            if(sale.getEndDate().after(ThisDate) & sale.getStartDate().before(ThisDate) & (sale.ContainProduct(ProductID) | sale.ContainCategory(pro.getCategory().get("main"))))
            {
                return sale.getSaleID();
            }
        }
        return -1;
    }
    //the category strings should be in format <String>,<String>,<String>,...
    //the date should be in format <year>-<month>-<day>
    public int SaleOnCategoryAtDate(int categoryID, String ThisDateStr){
        List<Sale> sales = SaleDAO.getInstance().getAllSales();
        IdCounter = SaleDAO.getMaxID()+1;
        Date ThisDate;
        ThisDate = Date.valueOf(ThisDateStr);
        for(Sale sale:sales){
            if((sale.getEndDate().after(ThisDate)&sale.getStartDate().before(ThisDate))&
                    sale.ContainCategory(categoryID)){
                return sale.getSaleID();
            }
        }
        return -1;
    }
    public boolean RemoveSale(int SaleId){
        boolean check = false;
        List<Sale> sales = SaleDAO.getInstance().getAllSales();
        IdCounter = SaleDAO.getMaxID()+1;
        for(Sale sale:sales){
            if(sale.getSaleID()==SaleId){
                sales.remove(sale);
                check = true;
                break;
            }
        }
        SaleDAO.getInstance().DeleteByID(SaleId);
        return check;
    }

}
