package ServiceLayer.ModulesServices;

import BussinessLayer.Objects.Order;
import ServiceLayer.Response;
import ServiceLayer.ResponseT;
import ServiceLayer.ServiceObjects.*;
import BussinessLayer.Objects.Supplier;
import ServiceLayer.ServiceObjects.ServiceOrderManager;
import ServiceLayer.ServiceObjects.ServiceSupplierManager;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SuppliersService {
    private ServiceOrderManager serviceOrderMan;
    private ServiceSupplierManager serviceSuppliersMan;
    private static SuppliersService instance = null;


    private SuppliersService() {
        this.serviceOrderMan = ServiceOrderManager.getInstance();
        this.serviceSuppliersMan = ServiceSupplierManager.getInstance();
    }

    public static SuppliersService getInstance() {
        if (instance == null) {
            instance = new SuppliersService();
        }
        return instance;
    }

    public void loadData() {
      serviceSuppliersMan.loadData();
      serviceOrderMan.loadData();
//        if(supplierResponse.isErrorOccurred())
//            return supplierResponse;
//        return orderResponse;
    }


    //ORDERS
    public ResponseT<ServiceOrder> createOrder(Map<Integer, Integer> itemIdAmountMap) {
        return serviceOrderMan.createOrder(itemIdAmountMap);
    }
    public Response receiveAllOrders(java.sql.Date ThisDate) {
        return serviceOrderMan.receiveAllOrders(ThisDate);
    }
    //#orderID
    //ProID,dd/mm/yyyy
    //ProID,Date...
    //#orderID
    //ProID,Date
    //ProID,Date...

    //#orderID
    //proid,amount
    //proid,amount
    public Response receive_Defective_Orders(java.sql.Date ThisDate,String strExp,String strDef) throws ParseException {
        Map<Integer,Map<Integer,Date>> exp_map = new HashMap<>();
        String[] Hashtag_Split = strExp.split("#");
        for(String orderSpl:Hashtag_Split){
            Map<Integer, Date> exp_SmallMap = new HashMap<>();
            if(!orderSpl.isEmpty()&&!orderSpl.equals(Hashtag_Split[0])&&
                    Hashtag_Split.length>1){
                String[] Orders_Split = orderSpl.split("-");
                for(String orSplit:Orders_Split){
                    if(!orSplit.equals(Orders_Split[0])&&!orSplit.isEmpty()){
                        String[] Second_Split = orSplit.split(",");
                        java.util.Date date=new SimpleDateFormat("dd/MM/yyyy").parse(Second_Split[1]);
                        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                        exp_SmallMap.putIfAbsent(Integer.parseInt(Second_Split[0]),sqlDate);
                    }
                }
                exp_map.putIfAbsent(Integer.parseInt(Orders_Split[0]),exp_SmallMap);
            }
        }
        Map<Integer,Map<Integer,Integer>> Def_map = new HashMap<>();
        String[] Hashtag_SplitDef = strDef.split("#");
        for(String orderSplDef:Hashtag_SplitDef){
            Map<Integer, Integer> Def_SmallMap = new HashMap<>();
            if(!orderSplDef.isEmpty()&&!orderSplDef.equals(Hashtag_Split[0])&&
                    Hashtag_Split.length>1){
                String[] Orders_Split = orderSplDef.split("-");
                for(String orSplit:Orders_Split){
                    if(!orSplit.equals(Orders_Split[0])&&!orSplit.isEmpty()){
                        String[] Second_Split = orSplit.split(",");
                        Def_SmallMap.putIfAbsent(Integer.parseInt(Second_Split[0]),Integer.parseInt(Second_Split[1]));
                    }
                }
                Def_map.putIfAbsent(Integer.parseInt(Orders_Split[0]),Def_SmallMap);
            }
        }

        return serviceOrderMan.receive_Defective_Orders(ThisDate,exp_map,Def_map);
    }

    public  ResponseT<String>  PrintAllReceivedOrders(java.sql.Date date) throws ParseException {

        return serviceOrderMan.PrintAllReceivedOrders(date);
    }

    public ResponseT<ServiceOrder> createPeriodicOrder(Map<Integer, Integer> itemIdAmountMap, int periodic ,List<DayOfWeek> days) {
        if (periodic == 1)
            return serviceOrderMan.createPeriodicOrder(itemIdAmountMap,days);
        else return createOrder(itemIdAmountMap);
    }

    public  ResponseT<Supplier>  getSupplier(int id){
        return serviceSuppliersMan.getSupplier(id);
    }

    public Response placeAnOrder(int orderId) {
        return serviceOrderMan.placeAnOrder(orderId);
    }


    public ResponseT addItemsToOrder(int orderId, Map<Integer, Integer> productIdAmountMap) {
        return serviceOrderMan.addItemsToOrder(orderId, productIdAmountMap);
    }

    public ResponseT<ServiceOrder> removeItemsFromOrder(int orderId, List<Integer> productsIdsToRemove) {
        return serviceOrderMan.removeItemsFromOrder(orderId, productsIdsToRemove);
    }

    public ResponseT<ServiceOrder> getOrderByOrderID(int orderId) {
        return serviceOrderMan.getOrderByOrderId(orderId);
    }


    public Response reorder(int supplierId) {
        return serviceOrderMan.reorder(supplierId);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //SUPPLIERS
    public ResponseT<Supplier> addSupplier(ServiceSupplierCard supplierCard, ServiceAgreement agreement) {
        return serviceSuppliersMan.addSupplier(supplierCard, agreement);
    }

    public ResponseT<Map<String, ServiceProductS>> getSupplierItems(int supplierId) {
        return serviceSuppliersMan.getSupplierItems(supplierId);
    }

    public ResponseT removeSupplier(int supplierID) {
        return serviceSuppliersMan.removeSupplier(supplierID);
    }

    public ResponseT<List<ServiceSupplier>> getAllSuppliers() {
        return serviceSuppliersMan.getAllSuppliers();
    }

    public Response changeSupplierPaymentMethod(int supplierId, String paymentMethode) {
        return serviceSuppliersMan.changeSupplierPaymentMethod(supplierId, paymentMethode);
    }

    public Response changeSupplierBankAccount(int supplierID, String bankAccount) {
        return serviceSuppliersMan.changeSupplierBackAccount(supplierID, bankAccount);
    }

    public Response editSupplierContacts(int supplierID, String name, String email, String phoneNumber, boolean editEmail) {
        return serviceSuppliersMan.editSupplierContacts(supplierID, name, email, phoneNumber, editEmail);
    }

    public Response removeSupplierContact(int supplierID, String name) {
        return serviceSuppliersMan.removeSupplierContact(supplierID, name);
    }

    public Response addContactsToSupplier(int supplierID, String name, String email, String phoneNumber) {
        return serviceSuppliersMan.addContactsToSupplier(supplierID, name, email, phoneNumber);
    }

    public Response changeSupplyMethod(int supplierID, String supplyMethod, List<DayOfWeek> days) {
        return serviceSuppliersMan.changeSupplyMethod(supplierID, supplyMethod, days);
    }

    public Response changeAddress(int supplierID, String address) {
        return serviceSuppliersMan.changeAddress(supplierID, address);
    }

    public ResponseT addItemToAgreement(int supplierID, int productID, String priceList, String catalogNumber, List<String> discounts) {
        return serviceSuppliersMan.addItemToAgreement(supplierID, productID, priceList, catalogNumber, discounts);

    }

    public ResponseT removeItemFromAgreement(int supplierID, int productID) {
        return serviceSuppliersMan.removeItemFromAgreement(supplierID, productID);
    }

    public ResponseT<Boolean> isLegalSupplierID(int supplierID) {
        return serviceSuppliersMan.isLegalSupplierID(supplierID);
    }

    public void deleteOrder(int orderID){
        this.serviceOrderMan.deleteOrder(orderID);
    }

    public ResponseT<Boolean> isLegalOrderID(int orderId) {
        return serviceOrderMan.isLegalOrderID(orderId);
    }

    public ResponseT<StringBuilder> createOrderReport() {
        return this.serviceOrderMan.createOrderReport();
    }
}
