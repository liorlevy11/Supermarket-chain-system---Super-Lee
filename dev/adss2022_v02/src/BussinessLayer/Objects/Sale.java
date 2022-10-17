package BussinessLayer.Objects;

import DataAccessLayer.Dal.CategoryDAO;
import DataAccessLayer.Dal.ProductDAO;

import java.sql.Date;
import java.util.List;

public class Sale {

    //Variables
    private int SaleId;
    private Date startdate;
    private Date endDate;
    private List<Integer> category;
    private List<Integer> products;
    private double Discount;
    //Constructors
    // if there is no Category or no Items just put in an empty list
    public Sale(int id,Date StartDate, Date EndDate, double discount){
        this.SaleId = id;
        this.startdate = StartDate;
        this.endDate = EndDate;
        this.Discount = discount;
    }

    //Getters and Setters
    //Discount

    public double getDiscount() {
        return Discount;
    }
    public void setDiscount(double discount) {
        Discount = discount;
    }
    //SaleId
    public int getSaleID() {
        return SaleId;
    }
    public void setSaleId(int saleId) {
        SaleId = saleId;
    }
    //StartDate
    public Date getStartDate(){
        return this.startdate;
    }
    public void setStartDate(Date StartDate){
        this.startdate = StartDate;
    }
    //EndDate
    public Date getEndDate(){
        return this.endDate;
    }
    public void setEndDate(Date endDate){
        this.endDate = endDate;
    }
    //Category
    public List<Integer> GetCategory(){
        return this.category;
    }
    public void SetCategory(List<Integer> Category){
        this.category = Category;
    }
    //Items
    public List<Integer> GetItems(){
        return this.products;
    }
    public void SetProduct(List<Integer> products){
        this.products = products;
    }
    //add item to the sale
    public void addProduct(int adding){
        this.products.add(adding);
    }
    //add category to the sale
    public void addCategory(Integer adding){
        this.category.add(adding);
    }
    //removing an item from the sale
    public void removeProduct(int removing){
        this.products.remove(removing);
    }
    //removing a Category from the sale
    public void removeCategory(String removing){
        this.category.remove(removing);
    }
    //checking if the sale contains an item
    public boolean ContainProduct(int ProductID){
        for(Product p:ProductDAO.getInstance().getAllProducts()){
            if(p.getProductId()==ProductID&p.getSaleId()==this.SaleId);
            return true;
        }
        return false;
    }
    //checking if the sale contains a category
    public boolean ContainCategory(Integer parCategory){

        for(Category cat: CategoryDAO.getInstance().getAllCategories()){
            if(cat.getID() == parCategory&cat.getSale_ID()==this.SaleId){
                return true;
            }
        }
        return false;
    }

}
