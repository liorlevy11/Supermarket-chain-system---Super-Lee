package ServiceLayer.ModulesServices;

import ServiceLayer.ServiceObjects.productService;
import ServiceLayer.ServiceObjects.reportService;
import ServiceLayer.ServiceObjects.saleService;
import ServiceLayer.ServiceObjects.productS;
import ServiceLayer.ServiceObjects.reportS;
import ServiceLayer.Response;
import ServiceLayer.ResponseT;

import java.sql.Date;
import java.util.List;

public class InventoryService {
    private ServiceLayer.ServiceObjects.saleService saleService;
    private ServiceLayer.ServiceObjects.productService productService;
    private ServiceLayer.ServiceObjects.reportService reportService;
    private static InventoryService instance;

    private InventoryService() {
        this.productService = new productService();
        this.reportService = new reportService();
        this.saleService = new saleService();
    }

    public static InventoryService getInstance() {
        if (instance == null) {
            instance = new InventoryService();
        }
        return instance;
    }

    public void loadData() {
       productService.loadData();
      saleService.loadData();
//        if (productResponse.isErrorOccurred())
//            return productResponse;
//        return saleResponse;
    }


    public Response addProduct(String name, String manufacturer, int MinQuantity, double sellPrice,
                               String category, String subCat, String subSubCaty, double weight) {
        return this.productService.addProduct(name, manufacturer, MinQuantity, sellPrice, category, subCat, subSubCaty, weight);
    }

    public Response removeProduct(int ProID) {
        return this.productService.removeProduct(ProID);
    }


    public Response updateSellPrice(int ProID, double sellPrice) {
        return this.productService.updateSellPrice(ProID, sellPrice);
    }

    //enter category id or the category name if the category doesn't exist
    public Response updateCategory(int ProID, String category, String categoryType) {
        return this.productService.updateCategory(ProID, category, categoryType);
    }

    public Response moveItem(int ProID, int amountToMove, String newPlace, boolean toShelf) {
        return this.productService.moveItem(ProID, amountToMove, newPlace, toShelf);
    }

    public Response moveOneItem(int ProID, int ItemID, String newPlace, boolean toShelf) {
        return this.productService.moveOneItem(ProID, ItemID, newPlace, toShelf);
    }

    public Response removeItems(int ProID, List<Integer> itemsToRemove) {
        return this.productService.removeItems(ProID, itemsToRemove);
    }

    public Response removeSpecificItem(int ProID, int itemID) {
        return this.productService.removeSpecificItem(ProID, itemID);
    }

    public Response addItems(int ProID, int amountToAdd, int OrderID, String place, int buyPrice, Date expressionDate, String StorageShelf, boolean def) {
        return this.productService.addItems(ProID, amountToAdd, OrderID, place, buyPrice, expressionDate, StorageShelf, def);
    }

    public Response changeCategory(int ProID, String newCat, String typeCat) {
        return this.productService.changeCategory(ProID, newCat, typeCat);
    }

    public ResponseT<List<productS>> getAllProducts() {
        return this.productService.getAllProducts();
    }

    public ResponseT<productS> getProductByID(int productId) {
        return this.productService.getProductByID(productId);
    }

    //REPORTS
    public ResponseT<reportS> AddItemsToOrderReport(String productToOrder) {
        return this.reportService.AddItemsToOrderReport(productToOrder);
    }

    public ResponseT addInventoryReport(String CategoryString) {
        return this.reportService.addInventoryReport(CategoryString);
    }

    public ResponseT<reportS> addDefectiveReport() {
        return this.reportService.addDefectiveReport();
    }

    //SALES
    public Response AddSale(String StartDate, String EndDate, String Category, String Product, double Discount) {
        return this.saleService.AddSale(StartDate, EndDate, Category, Product, Discount);
    }

    public Response RemoveSale(int id) {
        return this.saleService.RemoveSale(id);
    }

    public ResponseT<Integer> SaleOnCategoryAtDate(Integer parCategory, String ThisDate) {
        return this.saleService.SaleOnCategoryAtDate(parCategory, ThisDate);
    }

    public ResponseT<Integer> SaleOnProductAtDate(int ProductID, String ThisDate) {
        return this.saleService.SaleOnProductAtDate(ProductID, ThisDate);
    }

    public ResponseT<Double> getSaleDiscount(int SaleID) {
        return this.saleService.getSaleDiscount(SaleID);
    }

    //Combination
    public ResponseT<Double> getPriceAfterDiscount(int productID, String date) {
        double Discount = 0.0;
        try {
            try {
                Discount = saleService.getSaleDiscount(saleService.SaleOnProductAtDate(productID, date).getValue()).getValue();
            } catch (Exception e) {
                //if there is a discount the var will be the discount, else the vat will be 0
            }

            return productService.getPriceAfterDiscount(productID, Discount);
        } catch (Exception e) {
        }
        return new ResponseT<Double>(0.0, "Something went Wrong");
    }

    public void deleteItemsOrder(int orderID) {
        this.productService.deleteItemsOrder(orderID);
    }
}
