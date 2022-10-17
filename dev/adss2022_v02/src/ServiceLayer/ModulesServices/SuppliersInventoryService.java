package ServiceLayer.ModulesServices;

import BussinessLayer.Objects.Supplier;
import DataAccessLayer.Dal.ItemOrderDAO;
import ServiceLayer.Response;
import ServiceLayer.ResponseT;
import ServiceLayer.ServiceObjects.productS;
import ServiceLayer.ServiceObjects.reportS;
import ServiceLayer.ServiceObjects.ServiceAgreement;
import ServiceLayer.ServiceObjects.ServiceOrder;
import ServiceLayer.ServiceObjects.ServiceProductS;
import ServiceLayer.ServiceObjects.ServiceSupplierCard;

import java.sql.Date;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SuppliersInventoryService {
    private InventoryService inventoryService;
    private SuppliersService suppliersService = SuppliersService.getInstance();
    private static SuppliersInventoryService instance;

    private SuppliersInventoryService() {
        inventoryService = InventoryService.getInstance();
    }

    public static SuppliersInventoryService getInstance() {
        if (instance == null)
            instance = new SuppliersInventoryService();
        return instance;
    }

    public void loadData() {
       inventoryService.loadData();
        suppliersService.loadData();
//        if (inventoryResponse.isErrorOccurred())
//            return inventoryResponse;
//        return supplierResponse;
    }

    public Response deleteOrder(int orderID) {
        try {
            this.suppliersService.deleteOrder(orderID);
            this.inventoryService.deleteItemsOrder(orderID);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }
    public Response receive_Defective_Orders(java.sql.Date ThisDate,String strExp,String strDef){
        try {
            this.suppliersService.receive_Defective_Orders(ThisDate,strExp,strDef);
            return new Response();
        }
        catch (Exception e) {
            return new Response(e.getMessage());
        }
    }
    public Response PrintAllReceivedOrders(java.sql.Date strdate){
        try {
            return this.suppliersService.PrintAllReceivedOrders(strdate);
        }
        catch (Exception e) {
            return ResponseT.fromError(e.getMessage());
        }
    }


    public Response orderItems(String productToOrder, int periodic, List<DayOfWeek> days) {
        ResponseT<reportS> items = inventoryService.AddItemsToOrderReport(productToOrder);
        Map<Integer, Integer> productMap = items.getValue().getProducts();
        ResponseT<ServiceOrder> order = suppliersService.createPeriodicOrder(productMap, periodic, days);
        int orderID;
        if (order.isErrorOccurred())
            return order;
        else
            orderID = order.getValue().getOrderId();
        if (items.isErrorOccurred())
            return items;

        Response response = new Response(null);
        for (Integer productID : items.getValue().getProducts().keySet()) {
            response = this.inventoryService.addItems(productID, productMap.get(productID), orderID, "shelf", ItemOrderDAO.getInstance().getItemBuyPrice(orderID, productID), Date.valueOf("2022-06-01"), "Shelf", false);
            if (response.isErrorOccurred())
                break;
        }
        return response;
    }

    public ResponseT addItemsToOrder(int orderId, String itemIdAmountMap) {
        Map<Integer, Integer> order = new HashMap<Integer, Integer>();
        String[] partsOfOrder = itemIdAmountMap.split(",");
        for (String str : partsOfOrder) {
            if (!str.equals("") & str.contains(" ")) {
                String[] ProductNameAndQuantity = str.split(" ");
                int ID = 0;
                try {
                    ID = Integer.parseInt(ProductNameAndQuantity[0]);
                } catch (Exception e) {
                }
                order.putIfAbsent(ID, Integer.parseInt(ProductNameAndQuantity[1]));
            }
        }
        return suppliersService.addItemsToOrder(orderId, order);
    }

    public ResponseT<ServiceOrder> removeItemsFromOrder(int orderId, String[] itemIdsToRemove) {
        List<Integer> itemsToRemove = new LinkedList<>();
        for (String str : itemIdsToRemove) {
            itemsToRemove.add(Integer.parseInt(str));
        }
        return suppliersService.removeItemsFromOrder(orderId, itemsToRemove);
    }

    public Response receiveAllOrders(java.sql.Date date) {
        return suppliersService.receiveAllOrders(date);
    }

    public ResponseT<List<productS>> getAllProducts() {
        return this.inventoryService.getAllProducts();
    }

    public ResponseT<productS> getProductByID(int productId) {
        return this.inventoryService.getProductByID(productId);
    }

    public Response addProduct(String name, String manufacturer, int MinQuantity, double sellPrice,
                               String category, String subCat, String subSubCaty, double weight) {
        return this.inventoryService.addProduct(name, manufacturer, MinQuantity, sellPrice, category, subCat, subSubCaty, weight);
    }

    public Response removeProduct(int ProID) {
        return this.inventoryService.removeProduct(ProID);
    }


    public Response updateSellPrice(int ProID, double sellPrice) {
        return this.inventoryService.updateSellPrice(ProID, sellPrice);
    }

    //enter category id or the category name if the category doesn't exist
    public Response updateCategory(int ProID, String category, String categoryType) {
        return this.inventoryService.updateCategory(ProID, category, categoryType);
    }

    public Response moveItem(int ProID, int amountToMove, String newPlace, boolean toShelf) {
        return this.inventoryService.moveItem(ProID, amountToMove, newPlace, toShelf);
    }

    public Response moveOneItem(int ProID, int ItemID, String newPlace, boolean toShelf) {
        return this.inventoryService.moveOneItem(ProID, ItemID, newPlace, toShelf);
    }

    public Response removeItems(int ProID, List<Integer> itemsToRemove) {
        return this.inventoryService.removeItems(ProID, itemsToRemove);
    }

    public Response removeSpecificItem(int ProID, int itemID) {
        return this.inventoryService.removeSpecificItem(ProID, itemID);
    }

    public Response addItems(int ProID, int amountToAdd, int OrderID, String place, int buyPrice, Date expressionDate, String StorageShelf, boolean def) {
        return this.inventoryService.addItems(ProID, amountToAdd, OrderID, place, buyPrice, expressionDate, StorageShelf, def);
    }

    public Response changeCategory(int ProID, String newCat, String typeCat) {
        return this.inventoryService.changeCategory(ProID, newCat, typeCat);
    }

    //REPORTS
    public ResponseT<reportS> AddItemsToOrderReport(String productToOrder) {
        return this.inventoryService.AddItemsToOrderReport(productToOrder);
    }

    public ResponseT addInventoryReport(String CategoryString) {
        return this.inventoryService.addInventoryReport(CategoryString);
    }

    public ResponseT<reportS> addDefectiveReport() {
        return this.inventoryService.addDefectiveReport();
    }

    //SALES
    public Response AddSale(String StartDate, String EndDate, String Category, String Product, double Discount) {
        return this.inventoryService.AddSale(StartDate, EndDate, Category, Product, Discount);
    }

    public Response RemoveSale(int id) {
        return this.inventoryService.RemoveSale(id);
    }

    public ResponseT<Integer> SaleOnCategoryAtDate(Integer parCategory, String ThisDate) {
        return this.inventoryService.SaleOnCategoryAtDate(parCategory, ThisDate);
    }

    public ResponseT<Integer> SaleOnProductAtDate(int ProductID, String ThisDate) {
        return this.inventoryService.SaleOnProductAtDate(ProductID, ThisDate);
    }

    public ResponseT<Double> getSaleDiscount(int SaleID) {
        return this.inventoryService.getSaleDiscount(SaleID);
    }

    //Combination
    public ResponseT<Double> getPriceAfterDiscount(int productID, String date) {
        return inventoryService.getPriceAfterDiscount(productID, date);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public ResponseT<Supplier> getSupplier(int id) {
        return suppliersService.getSupplier(id);
    }

    public Response placeAnOrder(int orderId) {
        return suppliersService.placeAnOrder(orderId);
    }


    public ResponseT<ServiceOrder> getOrderByOrderID(int orderId) {
        return suppliersService.getOrderByOrderID(orderId);
    }


    public Response reorder(int orderId) {
        return suppliersService.reorder(orderId);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //SUPPLIERS
    public ResponseT<Supplier> addSupplier(ServiceSupplierCard supplierCard, ServiceAgreement agreement) {
        return suppliersService.addSupplier(supplierCard, agreement);
    }

    public ResponseT<Map<String, ServiceProductS>> getSupplierItems(int supplierId) {
        return suppliersService.getSupplierItems(supplierId);
    }

    public ResponseT removeSupplier(int supplierID) {
        return suppliersService.removeSupplier(supplierID);
    }


    public Response changeSupplierPaymentMethod(int supplierId, String paymentMethode) {
        return suppliersService.changeSupplierPaymentMethod(supplierId, paymentMethode);
    }

    public Response changeSupplierBankAccount(int supplierID, String bankAccount) {
        return suppliersService.changeSupplierBankAccount(supplierID, bankAccount);
    }

    public Response editSupplierContacts(int supplierID, String name, String email, String phoneNumber, boolean editEmail) {
        return suppliersService.editSupplierContacts(supplierID, name, email, phoneNumber, editEmail);
    }

    public Response removeSupplierContact(int supplierID, String name) {
        return suppliersService.removeSupplierContact(supplierID, name);
    }

    public Response addContactsToSupplier(int supplierID, String name, String email, String phoneNumber) {
        return suppliersService.addContactsToSupplier(supplierID, name, email, phoneNumber);
    }

//    public ServiceLayer.Response addBrandsToSupplier(int supplierID, List<String> brandsName) {
//        return service.addBrandsToSupplier(supplierID, brandsName);
//    }
//
//    public ServiceLayer.Response removeBrandsFromSupplier(int supplierID, List<String> brandsName) {
//        return service.removeBrandsFromSupplier(supplierID, brandsName);
//    }
//
//
//    public ServiceLayer.Response changeOverallDiscount(int supplierID, List<String> discounts) {
//        return service.changeOverallDiscount(supplierID, discounts);
//    }


    public Response changeSupplyMethod(int supplierID, String supplyMethod, List<DayOfWeek> days) {
        return suppliersService.changeSupplyMethod(supplierID, supplyMethod, days);
    }

    public Response changeAddress(int supplierID, String address) {
        return suppliersService.changeAddress(supplierID, address);
    }

    public ResponseT addItemToAgreement(int supplierID, int productID, String priceList, String catalogNumber, List<String> discounts) {
        return suppliersService.addItemToAgreement(supplierID, productID, priceList, catalogNumber, discounts);

    }

    public ResponseT removeItemFromAgreement(int supplierID, int productID) {
        return suppliersService.removeItemFromAgreement(supplierID, productID);
    }

    public ResponseT<Boolean> isLegalSupplierID(int supplierID) {
        return suppliersService.isLegalSupplierID(supplierID);
    }

    public ResponseT<Boolean> isLegalOrderID(int orderId) {
        return suppliersService.isLegalOrderID(orderId);
    }
}
