package BussinessLayer.Managers;

import BussinessLayer.Objects.DefectiveReport;
import BussinessLayer.Objects.InventoryReport;
import BussinessLayer.Objects.ItemsToOrderReport;

import java.sql.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ReportsManager {
    private int ReportIDCounter = 1;
    public static ReportsManager ManagerReports  =null;
    private ReportsManager(){
    }
    public static ReportsManager getInstance(){
        if(ManagerReports == null){
            ManagerReports = new ReportsManager();
        }
        return ManagerReports;
    }
    public void AddToReportCounter(){
        ManagerReports.ReportIDCounter++;
    }

    public int getReportIDCounter(){
        return this.ReportIDCounter;
    }
    // the product to order string should be in format of <ProductName> <ProductQuantity>,<ProductName> <ProductQuantity>,...
    public ItemsToOrderReport addItemToOrderReport(String productToOrder){
        try{
            Map<Integer, Integer> Order = new HashMap<Integer,Integer>();
            String[] partsOfOrder = productToOrder.split(",");
            for(String str:partsOfOrder){
                if(!str.equals("")&str.contains(" ")){
                    String[] ProductNameAndQUantity = str.split(" ");
                    int ID = 0;
                    try{
                        ID = Integer.parseInt(ProductNameAndQUantity[0]);
                    }catch(Exception e){}
                    if(ID!=0){
                        Order.putIfAbsent(ID,Integer.parseInt(ProductNameAndQUantity[1]));
                    }
                }
            }
            ItemsToOrderReport rep = new ItemsToOrderReport(new Date(System.currentTimeMillis()),ManagerReports.getReportIDCounter(), Order);
            ManagerReports.AddToReportCounter();
            return rep;
        }catch(Exception e){
            throw new IllegalArgumentException("report didn't work");
        }

    }
    //the CategoryString should be in format <CategoryName>,<CategoryName>,...
    public InventoryReport addInventoryReport(String CategoryString){
        List<Integer> CategoryList = new LinkedList<Integer>();
        String[] CategoryNamesArr = CategoryString.split(",");
        for(String str:CategoryNamesArr){
            if(!str.isEmpty()){
                CategoryList.add(Integer.valueOf(str));
            }
        }
        InventoryReport rep = new InventoryReport(CategoryList,new Date(System.currentTimeMillis()), ManagerReports.getReportIDCounter());
        ManagerReports.AddToReportCounter();
        return rep;
    }
    public DefectiveReport addDefectiveReport(){
        DefectiveReport rep = new DefectiveReport(new Date(System.currentTimeMillis()), ManagerReports.getReportIDCounter());
        ManagerReports.AddToReportCounter();
        return rep;
    }
}
