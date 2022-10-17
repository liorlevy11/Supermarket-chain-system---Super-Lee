package ServiceLayer.ServiceObjects;

import BussinessLayer.Objects.DefectiveReport;
import BussinessLayer.Objects.InventoryReport;
import BussinessLayer.Objects.ItemsToOrderReport;
import BussinessLayer.Managers.ReportsManager;
import ServiceLayer.ResponseT;

public class reportService {
    private ReportsManager reportsManager;
    public reportService(){
        this.reportsManager = reportsManager.getInstance();
    }
    //the string should be in format     ProductName int,ProductName int,...
    public ResponseT<reportS> AddItemsToOrderReport(String productToOrder){
        try {
            ItemsToOrderReport c = this.reportsManager.addItemToOrderReport(productToOrder);
            reportS report = new reportS(c.getReportDate(), c.getReportID(),c.getProductToOrder(),"Oredr");
            return new ResponseT<reportS>(report,"");
        }
        catch (Exception e){
            return new ResponseT<>(null,e.getMessage());
        }
    }
    public ResponseT<reportS> addInventoryReport(String CategoryString){
        try {
            InventoryReport c = reportsManager.addInventoryReport(CategoryString);
            reportS report = new reportS(c.getReportDate(), c.getReportID(),c.getProductList(),"Inventory");
            return new ResponseT<reportS>(report,"");
        }
        catch (Exception e){
            return new ResponseT<>(null,e.getMessage());
        }
    }
    public ResponseT<reportS> addDefectiveReport(){
        try {
            DefectiveReport c = reportsManager.addDefectiveReport();;
            reportS report = new reportS(c.getReportDate(),c.getReportID(),c.getDefective(),"Defective");
            return new ResponseT<reportS>(report,"");
        }
        catch (Exception e){
            return new ResponseT<>(null,e.getMessage());
        }
    }
}
