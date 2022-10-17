package InterfaceLayer;


import BussinessLayer.Objects.Supplier;
import DataAccessLayer.Dal.DBHandler;
import DataAccessLayer.Dal.SupplierDAO;
import ServiceLayer.*;
import ServiceLayer.ModulesServices.IntegratedService;
import ServiceLayer.ServiceObjects.*;
import ServiceLayer.ModulesServices.SuppliersService;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

public class SupplierCLI {
    private final Scanner scanner;
    private final IntegratedService service;
    private static boolean stop;
    private static boolean loginUser;
    private static boolean mainMenu;

    public SupplierCLI() {
        scanner = new Scanner(System.in);
        service = IntegratedService.getInstance();
        DBHandler.getInstance();
        stop = false;
        mainMenu = false;
    }

//    private void loadData() {
//        service.loadData();
//    }

    public int runProgram() {
        int user = 0;
        stop = false;
        mainMenu = false;
        loginUser = true;
        while (!stop && !mainMenu && loginUser) {
            supplierMenu();
        }
        if (stop)//bye
            return 0;
        if (!loginUser)//logout
            return 1;
        return 2;//return main menu
    }


//
//        System.out.println("Hi there! Do you want to upload existing data or start over?  \n1. Load Data\n2. New Data \n3. EXIT");
//        int n = reader.nextInt();
//        reader.nextLine();
//        if (n == 1)
//            loadData();
//        if (n == 3)
//            return;
//
//        while (user != 3) {
//            System.out.println("Hello! What kind of worker are you?  \n1. Supplier manager\n2. Inventory Manager\n3. EXIT");
//            user = reader.nextInt();
//            reader.nextLine();
//            if (user < 1 || user > 3) {
//                System.out.println("Please choose one of the following options \n1. Supplier manager\n2. Inventory Manager\n3. EXIT");
//                user = reader.nextInt();
//                reader.nextLine();
//            }
//            if (user == 1)
//                supplierManagerCLI();
//            if (user == 2)
//                cliInventory.runProgram();
//        }
//    }


    private void supplierMenu() {
        System.out.println("Welcome to Suppliers menu!\n" +
                "Please choose an action:\n" +
                "1. Add new Supplier\n" +
                "2. Delete Supplier \n" +
                "3. Edit Supplier's information \n" +
                "4. Show all suppliers \n" +
                "5. Return to main menu");
        try {
            String action = scanner.nextLine();
            int actionNum = parseIntBetween(action, 5);
            switch (actionNum) {
                case 1:
                    addNewSupplier();
                    break;
                case 2:
                    deleteSupplier();
                    break;
                case 3:
                    editSupplier();
                    break;
                case 4:
                    showAllSuppliers();
                    break;
                case 5:
                    mainMenu = true;
                    break;
            }
        } catch (IllegalArgumentException e) {
            printError(e);
        }
    }

    private void showAllSuppliers() {
        ResponseT<List<ServiceSupplier>> res = SuppliersService.getInstance().getAllSuppliers();
        List<ServiceSupplier> suppliers = res.getValue();
        for (ServiceSupplier s : suppliers) {
            System.out.println("Supplier Name: " + s.getSupplierCard().getName() + ", Supplier ID: " + s.getSupplierCard().getId() + ", Supplier Address: " + s.getSupplierCard().getAddress());
        }
    }

    public void printError(IllegalArgumentException e) {
        System.out.println("Action Failed: " + e.getMessage());
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    private void purchaseManagerCLI() {
//        System.out.println("choose an action  \n1. Place new order\n2. Reorder last order \n3. Back");
//        int action = reader.nextInt();
//        reader.nextLine();
//        switch (action) {
//            case 1:
//                placeOrder();
//                break;
//            case 2:
//                reorder();
//                break;
//            case 3:
//                return;
//        }
//    }

    private Map<String, ServiceProductS> getSupplierItems(int id) {
        ResponseT<Map<String, ServiceProductS>> res = service.getSupplierItems(id);
        if (res.isErrorOccurred()) {
            System.out.println(res.getErrorMessage());
            System.out.print("Lets try again, ");
        }
        Map<String, ServiceProductS> items = res.getValue();
        return items;
    }
    //parsing
    private int parseInt(String data) {
        String s = data;
        boolean suc = false;
        int num = 0;
        while (!suc) {
            try {
                num = Integer.parseInt(s);
                suc = true;
            } catch (Exception e) {
                System.out.println("please enter a valid number");
                s = scanner.nextLine();
            }
        }
        return num;
    }
    //purchase manager story
//    private void placeOrder() {
//        System.out.println("Please choose products from the list by typing id number and amount separated by comma (<id>,<amount>)");
//        //show all items
//        Map<String, ServiceProductS> products = getSupplierItems(id);
//        displayItemsOfSupplier(items);
//        Map<String, Integer> catalogNumbersAmount = chooseItemsFromSupplierList(items);
//        ResponseT<ServiceOrder> order = service.createOrder(id, catalogNumbersAmount);
//        if (order.isErrorOccurred()) {
//            System.out.println(order.getErrorMessage());
//            System.out.print("Lets try again, ");
//            placeOrder();
//        }
//
//        orderConfirmation(order.getValue());
//        ServiceOrder o = order.getValue();
//
//        if (o.getItemCount().size() != 0) {
//            System.out.println("Order received!");
//        }
//        System.out.println("\n1. Back to menu \n2. Exit");
//        int in = reader.nextInt();
//        reader.nextLine();
//        switch (in) {
//            case 1:
//                purchaseManagerCLI();
//                break;
//            case 2:
//                return;
//        }
//    }

//    private void orderConfirmation(ServiceOrder order) {
//        double beforeDis = order.getTotalPriceBeforeDiscount();
//        double afterDis = order.getTotalPriceAfterDiscount();
//        double discount = beforeDis - afterDis;
//        System.out.println("Your order: ");
//        displayOrder(order);
//        System.out.println("Total price: " + beforeDis);
//        if (discount > 0) {
//            System.out.println("Discount: " + discount + "\nFinal price: " + afterDis);
//        }
//        System.out.println("\nAre you sure you want to order \n1. Yes \n2. Cancel");
//        int input = reader.nextInt();
//        reader.nextLine();
//        switch (input) {
//            case 1:
//                if (order.getItemCount().size() != 0) {
//                    Response res = service.placeAnOrder(order.getOrderId());
//                    if (res.isErrorOccurred()) {
//                        System.out.println(res.getErrorMessage());
//                    }
//                } else {
//                    System.out.println("Order is empty");
//                }
//                break;
//            case 2:
//                editOrder(order);
//                break;
//            case 3:
//                purchaseManagerCLI();
//                break;
//        }
//    }
//
//    private Map<Integer, Integer> chooseItemsFromSupplierList(Map<String, ServiceProductS> suppliersItems) {
//        Map<String, Integer> catalogNumbersAmount = new HashMap<>();
//        String catalogNumAmount = reader.nextLine();
//        String[] pairCatAmount = new String[2];
//        while (!catalogNumAmount.equals("-1")) {
//            pairCatAmount = catalogNumAmount.split(",");
//            if (pairCatAmount.length == 2 && isNumber(pairCatAmount[1])) {
//                if (suppliersItems.containsKey(pairCatAmount[0]))
//                    catalogNumbersAmount.put(pairCatAmount[0], Integer.parseInt(pairCatAmount[1]));
//                else
//                    System.out.println("Catalog Number : " + pairCatAmount[0] + " doesn't exist in the chosen supplier's items");
//            } else {
//                System.out.println("Please make sure to enter exactly two arguments - catalog number and a positive integer as the amount");
//            }
//            catalogNumAmount = reader.nextLine();
//        }
//        return catalogNumbersAmount;
//    }


    private int getLegalSupplierID() {
        int supplierID = parseInt(scanner.nextLine());
        scanner.nextLine();
        ResponseT<Boolean> res = service.isLegalSupplierID(supplierID);
        while ((res.isErrorOccurred() || !res.getValue())) {
            System.out.println("There is no supplier with this ID, Please try again");
            supplierID = parseInt(scanner.nextLine());
            scanner.nextLine();
            res = service.isLegalSupplierID(supplierID);
        }
        return supplierID;
    }

    private int getLegalNewID() {
        int supplierID = parseInt(scanner.nextLine());
        scanner.nextLine();
        ResponseT<Boolean> res = service.isLegalSupplierID(supplierID);
        while ((res.isErrorOccurred() || !res.getValue())) {
            System.out.println("A supplier with this ID already exist!, Please try again");
            supplierID = parseInt(scanner.nextLine());
            scanner.nextLine();
            res = service.isLegalSupplierID(supplierID);
        }
        return supplierID;
    }


    private boolean isNumber(String str) {
        try {
            int num = Integer.parseInt(str);
            if (num > 0)
                return true;
            else
                return false;
        } catch (Exception e) {
            return false;
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void editSupplier() {
        System.out.println("Please Enter The ID of the Supplier you wish to edit");
        int id = getLegalSupplierID();
        int keepEditing = 1;
        while (keepEditing == 1) {
            System.out.println("What would you like to edit?  \n1. Supplier's SupplierCard \n2. Supplier's Agreement details ");
            int action = parseInt(scanner.nextLine());
            scanner.nextLine();
            switch (action) {
                case 1:
                    editSupplierCard(id);
                    break;
                case 2:
                    editSupplierAgreement(id);
                    break;
            }
            System.out.println("Would you like to edit anything else?  \n1. Yes\n2. No");
            keepEditing = parseInt(scanner.nextLine());
            scanner.nextLine();
        }
    }

    private Supplier getSupplier(int id) {
        ResponseT<Supplier> res = service.getSupplier(id);
        if (res.isErrorOccurred()) {
            System.out.println(res.getErrorMessage());
            System.out.print("Lets try again, ");
        }
        return res.getValue();
    }

    ////////////////
    public void editSupplierCard(int id) {
        System.out.println("What would you like to edit?  \n1. Supplier's contacts \n2. Supplier's address \n3. Supplier's bank account \n4. Supplier's paymentMethod");
        int action = parseInt(scanner.nextLine());
        scanner.nextLine();
        while (action < 1 || action > 4) {
            System.out.println("Please Enter a valid number (1 to 4)");
            action = parseInt(scanner.nextLine());
            scanner.nextLine();
        }
        switch (action) {
            case 1:
                editContacts(id);
                break;
            case 2:
                editAddress(id);
                break;
            case 3:
                editBankAccount(id);
                break;
            case 4:
                editPaymentMethod(id);
                break;
        }
    }

    private void editAddress(int id) {
        System.out.println("Please enter the new address");
        String address = scanner.nextLine();

        Response res = service.changeAddress(id, address);
        if (res.isErrorOccurred())
            System.out.println(res.getErrorMessage());
    }

    private void editBankAccount(int id) {
        System.out.println("Please enter the new bank account number");
        String bankAccount = scanner.nextLine();
        Response res = service.changeSupplierBankAccount(id, bankAccount);
        if (res.isErrorOccurred())
            System.out.println(res.getErrorMessage());
    }

    private void editPaymentMethod(int id) {
        String payment = choosePaymentMethod();
        Response res = service.changeSupplierPaymentMethod(id, payment);
        if (res.isErrorOccurred())
            System.out.println(res.getErrorMessage());
    }

    private String choosePaymentMethod() {
        System.out.println("Choose one of the following Payment methods according to the supplier's agreement \n1. Cash \n2. TransitToAccount \n3. Credit");
        int paymentMethod = parseInt(scanner.nextLine());
        scanner.nextLine();
        while (paymentMethod < 1 || paymentMethod > 3) {
            System.out.println("Please Enter a valid number (1 to 3)");
            paymentMethod = parseInt(scanner.nextLine());
            scanner.nextLine();
        }
        switch (paymentMethod) {
            case 1:
                return "Cash";
            case 2:
                return "TransitToAccount";
            case 3:
                return "Credit";
        }
        return "";
    }

    private void editContacts(int id) {
        System.out.println("Choose an action \n1. Add a contact \n2. Delete a contact \n3. Edit contact's email \n4. Edit contact's phone number");
        int action = Integer.parseInt(scanner.nextLine());
        while (action < 1 || action > 4) {
            System.out.println("Please Enter a valid number (1 to 4)");
            action = Integer.parseInt(scanner.nextLine());
        }
        switch (action) {
            case 1:
                addContact(id);
                break;
            case 2:
                deleteContact(id);
                break;
            case 3:
                editContactEmail(id);
                break;
            case 4:
                editContactPhone(id);
                break;
        }

    }

    private void addContact(int id) {
        System.out.println("Please enter a name for the new contact");
        String name = scanner.nextLine();
        System.out.println("Please enter an email for the new contact");
        String email = scanner.nextLine();
        System.out.println("Please enter a phone number for the new contact");
        String phone = scanner.nextLine();

        Response res = service.addContactsToSupplier(id, name, email, phone);
        if (res.isErrorOccurred())
            System.out.println(res.getErrorMessage());
    }

    private void deleteContact(int id) {
        System.out.println("Please enter the name of the contact you wish to delete");
        String name = scanner.nextLine();

        Response res = service.removeSupplierContact(id, name);
        if (res.isErrorOccurred())
            System.out.println(res.getErrorMessage());
    }

    private void editContactEmail(int id) {
        System.out.println("Please enter the name of the contact you wish to edit");
        String name = scanner.nextLine();
        System.out.println("Please enter the name of the new email");
        String email = scanner.nextLine();
        Response res = service.editSupplierContacts(id, name, email, "", true);
        if (res.isErrorOccurred())
            System.out.println(res.getErrorMessage());
    }

    private void editContactPhone(int id) {
        System.out.println("Please enter the name of the contact you wish to edit");
        String name = scanner.nextLine();
        System.out.println("Please enter the name of the new phone number");
        String phone = scanner.nextLine();

        Response res = service.editSupplierContacts(id, name, "", phone, false);
        if (res.isErrorOccurred())
            System.out.println(res.getErrorMessage());
    }

    //////////////
    private void editSupplierAgreement(int id) {
        System.out.println("What would you like to edit? \n1. Supplier's delivery Method or delivery Days \n2. Supplier's items");
        int action = parseInt(scanner.nextLine());
        scanner.nextLine();
        while (action < 1 || action > 3) {
            System.out.println("Please Enter a valid number (1 to 3)");
            action = parseInt(scanner.nextLine());
            scanner.nextLine();
        }
        switch (action) {
            case 1:
                editDeliveryMethodAndDays(id);
                break;
            case 2:
                editItems(id);
                break;
        }
    }

    private void editItems(int supplierID) {
        System.out.println("Please choose an action?  \n1. Add a new item\n2. Delete an item");
        int action = parseInt(scanner.nextLine());
        scanner.nextLine();
        if (action == 1) {
            printAllProducts();
            System.out.println("Please enter the item in the following format:");
            System.out.println("Product ID, Price, Catalog number, Amount:Discount percentages,...,Amount:Discount percentages");
            String details = scanner.nextLine();
            String[] arr = details.split(",");
            List<String> discounts = new ArrayList<>();
            if (arr.length < 3) {
                System.out.println("Please enter the item according to the format mentioned above!");
            } else {
                if (arr.length > 3) {
                    discounts = Arrays.stream(Arrays.copyOfRange(arr, 3, arr.length)).collect(Collectors.toList());
                }
                Response res = service.addItemToAgreement(supplierID, Integer.parseInt(arr[0]), arr[1], arr[2], discounts);
                if (res.isErrorOccurred())
                    System.out.println(res.getErrorMessage());
            }

        } else if (action == 2) {
            ResponseT<Map<String, ServiceProductS>> resSupplierItems = service.getSupplierItems(supplierID);
            if (resSupplierItems.isErrorOccurred())
                System.out.println(resSupplierItems.getErrorMessage());
            else {
                displaySupplierItems(resSupplierItems.getValue());
                System.out.println("please enter the product ID you wish to delete from suppliers agreement");
                String itemToDelete = scanner.nextLine();
                Response res = service.removeItemFromAgreement(supplierID, Integer.parseInt(itemToDelete));
                if (res.isErrorOccurred())
                    System.out.println(res.getErrorMessage());
            }
        } else
            System.out.println("Sorry, The number you entered isn't valid");
    }

    private void displaySupplierItems(Map<String, ServiceProductS> supplierItems) {
        for (Map.Entry<String, ServiceProductS> entry : supplierItems.entrySet()) {
            System.out.println("Product ID: " + entry.getValue().getProductID() + ", Catalog Number: " + entry.getKey());
        }
    }

    private void editDeliveryMethodAndDays(int id) {
        String supplyMethod = "";
        List<DayOfWeek> days = new ArrayList<>();
        supplyMethod = chooseSupplyMethod();
        days = chooseSupplyDays(supplyMethod);

        Response res = service.changeSupplyMethod(id, supplyMethod, days);
        if (res.isErrorOccurred())
            System.out.println(res.getErrorMessage());
    }

    ////////////////////////////////
    private void deleteSupplier() {
        int keepDeleting = 1;
        while (keepDeleting == 1) {
            System.out.println("Please Enter The ID of the Supplier you wish to delete");
            int id = getLegalSupplierID();
            Response res = service.removeSupplier(id);
            if (res.isErrorOccurred())
                System.out.println(res.getErrorMessage());
            System.out.println("Would you like to delete another supplier?  \n1. Yes\n2. No");
            keepDeleting = parseInt(scanner.nextLine());
            scanner.nextLine();
        }
    }

    ///////////////////////////////////////////////
    private void addNewSupplier() {
        ///////// everytime we create new supplier we create new site **
        int keepAdding = 1;
        while (keepAdding == 1) {
            ServiceSupplierCard supplierCard = createSupplierCard();
            ServiceAgreement agreement = createAgreement(supplierCard.getName());
            ResponseT<Supplier> res = service.addSupplier(supplierCard, agreement);
            if (res.isErrorOccurred())
                System.out.println(res.getErrorMessage());
            System.out.println("In order to create site for this supplier we need extra information");
            System.out.println("Please enter the contact person");
            String contactPerson = scanner.nextLine();
            System.out.println("Please enter phone-number ");
            String phoneNumber = scanner.nextLine();
            System.out.println("Please enter shipping area");
            String shippingArea = scanner.nextLine();
            System.out.println("Would you like to add another supplier?  \n1. Yes\n2. No");
            Supplier supplier = res.getValue();
            try {
                service.AddSite(supplier.getSupplierCard().getId(), contactPerson, phoneNumber, supplier.getSupplierCard().getAddress(), shippingArea);
            }
            catch (Exception e){
                service.removeSupplier(supplier.getSupplierCard().getId());
                throw new IllegalArgumentException("the action fail");
            }
            keepAdding = parseInt(scanner.nextLine());
            scanner.nextLine();
        }
    }

    private ServiceSupplierCard createSupplierCard() {
        ///////// everytime we create new supplier we create new site **

//        private static int counter = 1;
//        private int idSite;
//        private String contactPerson;
//        private String phoneNumber;
//        private String address;
//        private Site.ShippingArea shippingArea;
        int id = SupplierDAO.getInstance().getMaxID() + 1;
        System.out.println("Please enter the supplier's name");
        String name = scanner.nextLine();
        System.out.println("Please enter the supplier's address");
        String address = scanner.nextLine();
        System.out.println("Please enter the supplier's bank account number");
        String bankAccount = scanner.nextLine();
        String paymentMethod = choosePaymentMethod();
        Map<String, ServiceContact> contactMap = createContacts(id);
        return new ServiceSupplierCard(id, name, address, bankAccount, paymentMethod, contactMap);
    }

    private Map<String, ServiceContact> createContacts(int supplierID) {
        Map<String, ServiceContact> contactMap = new HashMap<>();
        int keepAdding = 1;
        while (keepAdding == 1) {
            System.out.println("Please enter a contact name");
            String nameC = scanner.nextLine();
            System.out.println("Please enter " + nameC + "'s email");
            String email = scanner.nextLine();
            System.out.println("Please enter " + nameC + "'s phone number");
            String phone = scanner.nextLine();
            contactMap.put(nameC, new ServiceContact(supplierID, nameC, email, phone));
            System.out.println("Would you like to add another contact? \n1. Yes\n2. No");
            keepAdding = parseInt(scanner.nextLine());
            scanner.nextLine();
        }
        return contactMap;
    }

    private String chooseSupplyMethod() {
        System.out.println("Choose one of the following Supply methods according to the supplier's agreement \n1. By Days \n2. By Order \n3. By Super-Lee");
        int supplyMethod = parseInt(scanner.nextLine());
        scanner.nextLine();
        while (supplyMethod < 1 || supplyMethod > 3) {
            System.out.println("Please Enter a valid number (1 to 3)");
            supplyMethod = parseInt(scanner.nextLine());
            scanner.nextLine();
        }
        switch (supplyMethod) {
            case 1:
                return "byDays";
            case 2:
                return "byOrder";
            case 3:
                return "bySuperLee";

        }
        return "";
    }

    private List<DayOfWeek> chooseSupplyDays(String supplyMethod) {
        List<DayOfWeek> days = new ArrayList<>();
        if (supplyMethod.equalsIgnoreCase("byDays")) {
            System.out.println("Which days do you wish to add to the supplier's supply days?");
            System.out.println("\n1. Monday \n2. Tuesday \n3. Wednesday \n4. Thursday \n5. Friday \n6. Saturday \n7. Sunday \n8.That will be all...");
            String d = scanner.nextLine();
            int day = parseInt(d);
            while (day != 8) {
                day = parseInt(scanner.nextLine());
                if (day >= 1 && day <= 7)
                    days.add(DayOfWeek.of(day));
                else if (day != 8)
                    System.out.println("Please enter a valid number : 1 to 7, or 8 if you done adding days");
            }
        }
        return days;
    }

    private ServiceAgreement createAgreement(String name) {
        String supplyMethod = chooseSupplyMethod();
        List<DayOfWeek> days = chooseSupplyDays(supplyMethod);
        System.out.println("Would you like to add Items Specifications to the agreement? \n1. Yes\n2. No");
        int keepAdding = parseInt(scanner.nextLine());
        scanner.nextLine();
        Map<String, ServiceProductS> itemSpecMap = new HashMap<>();
        if (keepAdding == 1)
            printAllProducts();
        while (keepAdding == 1) {
            System.out.println("Please enter the item in the following format:");
            System.out.println("product ID, price, catalog number, amount:Discount percentages,amount:Discount percentages,...,amount:Discount percentages");
            String[] arr = scanner.nextLine().split(",");
            List<String> discounts = new ArrayList<>();
            if (arr.length < 3) {
                System.out.println("Please enter the item according to the format mentioned above!");
            } else {
                if (arr.length > 3) {
                    discounts = Arrays.stream(Arrays.copyOfRange(arr, 3, arr.length)).collect(Collectors.toList());
                }
                ServiceProductS itemS = new ServiceProductS(Integer.parseInt(arr[0].trim()), arr[1].trim(), arr[2].trim(), discounts);
                itemSpecMap.put(arr[2], itemS);
                System.out.println("Would you like to add another Item? \n1. Yes\n2. No");
                keepAdding = parseInt(scanner.nextLine());
                scanner.nextLine();
            }

        }
        return new ServiceAgreement(name, days, supplyMethod, itemSpecMap);
    }

    private void printAllProducts() {
        ResponseT<List<productS>> allProducts = service.getAllProducts();
        StringBuilder allProductsString = new StringBuilder();
        for (productS product : allProducts.getValue()) {
            allProductsString.append(product.getProductID()).append("  ").append(product.getName() + "\n");
        }
        System.out.println(allProductsString);
    }


    private int parseIntBetween(String data, int end) {
        String s = data;
        boolean suc = false;
        int num = 0;
        while (!suc) {
            try {
                num = Integer.parseInt(s);
                if (num >= 1 & num <= end) {
                    suc = true;
                }
            } catch (Exception e) {
                System.out.println("please enter a valid number");
                s = scanner.nextLine();
            }
        }
        return num;
    }
}



