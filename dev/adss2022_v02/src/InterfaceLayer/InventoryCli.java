package InterfaceLayer;

import ServiceLayer.ModulesServices.IntegratedService;
import ServiceLayer.ServiceObjects.productS;
import ServiceLayer.Response;
import ServiceLayer.ResponseT;
import ServiceLayer.ServiceObjects.ServiceOrder;
import javafx.util.Pair;

import java.time.DayOfWeek;
import java.util.*;

public class InventoryCli<T> {
    private final IntegratedService integratedService;
    private Map<Integer, Actions> logisticManagerActions = new HashMap<>();
    private Map<Integer, Actions> storeKeeperActions = new HashMap<>();
    private Map<Integer, Actions> storeManagerActions = new HashMap<>();
    private Map<Integer, Actions> personalManagerActions = new HashMap<>();
    private List<String> logisticManager = new ArrayList<String>();
    private List<String> storeKeeper = new ArrayList<String>();
    private List<String> storeManager = new ArrayList<String>();
    private List<String> personalManager = new ArrayList<String>();
    private final Scanner scanner;
    private static boolean stop;
    private static boolean mainMenu;
    private static boolean loginUser;//t-> Inventory Manager| f-> Purchasing Manager
    private static int logInUser = -1;//0 for logisticManager 1 for stokeKeeper 2 for storeManager
    private SupplierCLI supplierCLI;

    public InventoryCli(SupplierCLI supplierCLI) {
        this.supplierCLI = supplierCLI;
        integratedService = IntegratedService.getInstance();
        scanner = new Scanner(System.in);
        //logistic manager optional functions
        logisticManager.add("Delete existing order");
        logisticManager.add("logout");
        logisticManager.add("bye");
        logisticManager.add("return");
        logisticManagerActions.put(0, deleteOrder());
        logisticManagerActions.put(1, logout());
        logisticManagerActions.put(2, bye());
        logisticManagerActions.put(3, returnToMain());

        //stokeKeeper optional functions
        storeKeeper.add("Add new product");
        storeKeeper.add("Move more than one item, without the item ID");
        storeKeeper.add("Move one item");
        storeKeeper.add("Remove specific items");
        storeKeeper.add("Change category");
        storeKeeper.add("Edit Supplier Card");
        storeKeeper.add("Add Sale");
        storeKeeper.add("Remove Sale");
        storeKeeper.add("Check Sale On Category");
        storeKeeper.add("Check Sale On Product");
        storeKeeper.add("Get Sale Discount");
        storeKeeper.add("Get product price after discount");
        storeKeeper.add("Order items");
        storeKeeper.add("Reorder by order ID");
        storeKeeper.add("Add items to existing order");
        storeKeeper.add("Remove product from existing order");
        storeKeeper.add("Delete existing order");
        storeKeeper.add("Create Items To Order Report");
        storeKeeper.add("Create Inventory Report");
        storeKeeper.add("Create Defective Items Report");
        storeKeeper.add("Create Orders Report");
        storeKeeper.add("logout");
        storeKeeper.add("bye");
        storeKeeper.add("return");
        //
        storeKeeperActions.put(0, createNewProduct());
        storeKeeperActions.put(1, moveItem());
        storeKeeperActions.put(2, moveOneItem());
        storeKeeperActions.put(3, removeSpecificItems());
        storeKeeperActions.put(4, changeCategory());
        storeKeeperActions.put(5, editSupplier());
        storeKeeperActions.put(6, AddSale());
        storeKeeperActions.put(7, RemoveSale());
        storeKeeperActions.put(8, SaleOnCategoryAtDate());
        storeKeeperActions.put(9, SaleOnProductAtDate());
        storeKeeperActions.put(10, getSaleDiscount());
        storeKeeperActions.put(11, getPriceAfterDiscount());
        storeKeeperActions.put(12, orderItems());
        storeKeeperActions.put(13, reorder());
        storeKeeperActions.put(14, addItemsToOrder());
        storeKeeperActions.put(15, removeItemsFromOrder());
        storeKeeperActions.put(16, deleteOrder());
        storeKeeperActions.put(17, AddItemsToOrderReport());
        storeKeeperActions.put(18, addInventoryReport());
        storeKeeperActions.put(19, addDefectiveReport());
        storeKeeperActions.put(20, orderReport());
        storeKeeperActions.put(21, logout());
        storeKeeperActions.put(22, bye());
        storeKeeperActions.put(23, returnToMain());

        //personal manager functions
        personalManager.add("Delete existing order");
        personalManager.add("logout");
        personalManager.add("bye");
        personalManager.add("return");
        //
        personalManagerActions.put(0, deleteOrder());
        personalManagerActions.put(1, logout());
        personalManagerActions.put(2, bye());
        personalManagerActions.put(3, returnToMain());

        //store manager optional functions
        storeManager.add("Create Items To Order Report");
        storeManager.add("Create Inventory Report");
        storeManager.add("Create Defective Items Report");
        storeManager.add("Create Orders Report");
        storeManager.add("Edit Supplier Card");
        storeManager.add("logout");
        storeManager.add("bye");
        storeManager.add("return");
        //
        storeManagerActions.put(0, AddItemsToOrderReport());
        storeManagerActions.put(1, addInventoryReport());
        storeManagerActions.put(2, addDefectiveReport());
        storeManagerActions.put(3, orderReport());
        storeManagerActions.put(4, editSupplier());
        storeManagerActions.put(5, logout());
        storeManagerActions.put(6, bye());
        storeManagerActions.put(7, returnToMain());
    }




    private StringBuilder buildStokeKeeperMap() {
        StringBuilder actionMap = new StringBuilder();
        for (Integer i : this.storeKeeperActions.keySet()) {
            actionMap.append(i).append(". ").append(storeKeeper.get(i)).append("\n");
        }
        return actionMap;
    }

    private StringBuilder buildStoreManagerMap() {
        StringBuilder actionMap = new StringBuilder();
        for (Integer i : this.storeManagerActions.keySet()) {
            actionMap.append(i).append(". ").append(storeManager.get(i)).append("\n");
        }
        return actionMap;
    }

    private StringBuilder buildLogisticManagerMap() {
        StringBuilder actionMap = new StringBuilder();
        for (Integer i : this.logisticManagerActions.keySet()) {
            actionMap.append(i).append(". ").append(logisticManager.get(i)).append("\n");
        }
        return actionMap;
    }
    private StringBuilder buildPersonalManagerMap() {
        StringBuilder actionMap = new StringBuilder();
        for (Integer i : this.personalManagerActions.keySet()) {
            actionMap.append(i).append(". ").append(personalManager.get(i)).append("\n");
        }
        return actionMap;
    }

    private void receiveAllShipment() {
        String orderReceived = "";
        Actions act2 = new Actions() {
            @Override
            public Object act() {
                return integratedService.PrintAllReceivedOrders(new java.sql.Date(System.currentTimeMillis()));
            }
        };
        Object response2 = act2.act();
        if (response2 == null) {

        } else if (response2.getClass().equals(ResponseT.class)) orderReceived = (((ResponseT) response2).Content());
        else if (response2.getClass().equals(Response.class))
            if (((Response) response2).isErrorOccurred()) orderReceived =(((Response) response2).Content());
        if(orderReceived !=null && !orderReceived.isEmpty()){
            System.out.println("your orders are ");
            System.out.println(orderReceived);
            System.out.println("Put N if you want to receive the orders with default values or Y if you want to fill out the Exp Dates and Def (Y)");
            String Y_N = scanner.nextLine();
            if (Y_N.equals("Y")) {
                System.out.println("enter the Exp Date int the following format (without going down aLine and without any spaces)");
                System.out.println("#OrderID-ProductID,DD/MM/YYYY-ProductID,DD/MM/YYYY");
                System.out.println("#OrderID-ProductID,DD/MM/YYYY-ProductID,DD/MM/YYYY");
                String Exp = scanner.nextLine();
                System.out.println("enter the Exp Date int the following format");
                System.out.println("#OrderID-ProductID,NumOfDefective-ProductID,NumOfDefective");
                System.out.println("#OrderID-ProductID,NumOfDefective-ProductID,NumOfDefective");
                String Def = scanner.nextLine();
                Actions act1 = new Actions() {
                    @Override
                    public Object act() {
                        return integratedService.receive_Defective_Orders(new java.sql.Date(System.currentTimeMillis()), Exp, Def);
                    }
                };
                Object response1 = act1.act();
                if (response1 == null) {

                } else if (response1.getClass().equals(ResponseT.class))
                    System.out.println(((ResponseT) response1).Content());
                else if (response1.getClass().equals(Response.class))
                    if (((Response) response1).isErrorOccurred()) System.out.println(((Response) response1).Content());
            } else {
                integratedService.receiveAllOrders(new java.sql.Date(System.currentTimeMillis()));
            }
            System.out.println("Do you want to delete All the Defective and Expired Items? (Y/N)");
            String Y_N_Def = scanner.nextLine();
            if(Y_N_Def.equals("Y")){
                integratedService.addDefectiveReport();
            }
        }

    }

    public int runProgram(String job_tittle) {
        StringBuilder actionMap = new StringBuilder();
        Map<Integer, Actions> actions = new HashMap<>();
        loginUser = true;
        stop = false;
        mainMenu = false;
        int currentAct = 0;
        while (!mainMenu && !stop && loginUser) {
            switch (job_tittle) {
                case "":
                    return 2;
                case "Storekeeper":
                    //System.out.println("Put Y if you want to receive Your Orders Today, And Anything else not to");
                    //String Rec = scanner.nextLine();
                    //if(Rec.equals("Y")){
                    receiveAllShipment();
                    //}
                    actionMap = buildStokeKeeperMap();
                    actions = storeKeeperActions;
                    break;
                case "StoreManager":
                    actionMap = buildStoreManagerMap();
                    actions = storeManagerActions;
                    break;
                case "TransportManager":
                    actionMap = buildLogisticManagerMap();
                    actions = logisticManagerActions;
                    break;
                case "PersonalManager":
                    actionMap = buildPersonalManagerMap();
                    actions = personalManagerActions;
                    break;
            }
            System.out.println("Welcome to the inventory please enter an action number");
            System.out.println(actionMap);
            try {
                currentAct = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            Actions act = actions.get(currentAct);
            Object response = act.act();
            if (response == null) {

            } else if (response.getClass().equals(ResponseT.class))
                System.out.println(((ResponseT) response).Content());
            else if (response.getClass().equals(Response.class))
                if (((Response) response).isErrorOccurred())
                    System.out.println(((Response) response).Content());
        }
        if (stop) return 0;
        else if (!loginUser) return 1;
        return 2;
    }

    private Actions returnToMain() {
        return new Actions() {
            @Override
            public Object act() {
                mainMenu = true;
                return null;
            }
        };
    }


    private Actions bye() {
        return new Actions() {
            @Override
            public Object act() {
                stop = true;
                return logout();
            }
        };
    }


    private Actions logout() {
        return new Actions() {
            @Override
            public Object act() {
                integratedService.logout();
                System.out.println("logout succeeded");
                loginUser = false;
                return loginUser;
            }
        };
    }

    private Actions reorder() {
        return new Actions() {
            @Override
            public Object act() {
                System.out.println("Please enter the order ID you want to re-order");
                int id = getLegalOrderID();
                Response res = integratedService.reorder(id);
                if (res.isErrorOccurred()) System.out.println(res.getErrorMessage());
                else System.out.println("Order received!");
                return res;
            }
        };
    }

    private int getLegalOrderID() {
        int orderId = scanner.nextInt();
        scanner.nextLine();
        ResponseT<Boolean> res = integratedService.isLegalOrderID(orderId);
        while ((res.isErrorOccurred() || !res.getValue())) {
            System.out.println("There is no order with this ID, Please try again");
            orderId = scanner.nextInt();
            scanner.nextLine();
            res = integratedService.isLegalOrderID(orderId);
        }
        return orderId;
    }

    private void displayOrder(ServiceOrder order) {
        Map<Pair<Integer, Integer>, Integer> orderItemsCount = order.getItemCount();
        for (Map.Entry<Pair<Integer, Integer>, Integer> pair : orderItemsCount.entrySet()) {
            ResponseT<productS> res = integratedService.getProductByID(pair.getKey().getKey());
            if (res.isErrorOccurred()) System.out.println(res.getErrorMessage());
            else {
                productS serviceProduct = res.getValue();
                System.out.println("Product Name: " + serviceProduct.getName() + ", Product ID: " + pair.getKey().getKey() + ", Amount: " + pair.getValue());
            }
        }
    }

    /////order
    private Actions addItemsToOrder() {
        return new Actions() {
            @Override
            public Object act() {
                boolean idFound = false;
                int option = -1;
                while (!idFound) {
                    System.out.println("please enter orderID to add to");
                    int orderId = 0;
                    try {
                        orderId = Integer.parseInt(scanner.nextLine());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    ResponseT<ServiceOrder> sOrder = integratedService.getOrderByOrderID(orderId);
                    if (sOrder.isErrorOccurred()) {
                        System.out.println(sOrder.getErrorMessage());
                        System.out.println("Choose an option: \n1. Try Again \n2. Back To Menu");
                        option = scanner.nextInt();
                        scanner.nextLine();
                        if (option == 2) idFound = true;
                    } else {
                        idFound = true;
                        System.out.println("\"Please enter the Product ID you want to order and their quantity in the format: <ProductID><Space><int><,><ProductID><Space><int><,>...\"");
                        printAllProducts();
                        String itemsToAdd = scanner.nextLine();
                        ResponseT<ServiceOrder> order = integratedService.addItemsToOrder(orderId, itemsToAdd);
                        return order;
                    }
                }
                return null;
            }
        };
    }

    protected void printAllProducts() {
        ResponseT<List<productS>> allProducts = this.integratedService.getAllProducts();
        StringBuilder allProductsString = new StringBuilder();
        for (productS product : allProducts.getValue()) {
            allProductsString.append(product.getProductID()).append("  ").append(product.getName() + "\n");
        }
        System.out.println(allProductsString);
    }

    private Actions removeItemsFromOrder() {
        return new Actions() {
            @Override
            public Object act() {
                boolean idFound = false;
                int option = -1;
                while (!idFound) {
                    System.out.println("Please enter order ID to remove from");
                    int orderId = 0;
                    try {
                        orderId = Integer.parseInt(scanner.nextLine());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    ResponseT<ServiceOrder> sOrder = integratedService.getOrderByOrderID(orderId);
                    if (sOrder.isErrorOccurred()) {
                        System.out.println(sOrder.getErrorMessage());
                        System.out.println("Choose an option: \n1. Try Again \n2. Back To Menu");
                        option = scanner.nextInt();
                        scanner.nextLine();
                        if (option == 2) idFound = true;
                    } else {
                        idFound = true;
                        System.out.println("\"Please enter the Product ID's you want to remove <ProductID>,<ProductID>,>...\"");
                        displayOrder(sOrder.getValue());
                        String itemsToRemove = scanner.nextLine();
                        String[] arrayToRemove = itemsToRemove.split(",");
                        ResponseT<ServiceOrder> order = integratedService.removeItemsFromOrder(orderId, arrayToRemove);
                        return order;
                    }
                }
                return null;
            }
        };
    }

    private Actions editSupplier() {
        return new Actions() {
            @Override
            public Object act() {
                System.out.println("Please Enter The ID of the Supplier you wish to edit his card");
                int id = getLegalSupplierID();
                int keepEditing = 1;
                while (keepEditing == 1) {
                    supplierCLI.editSupplierCard(id);
                    System.out.println("Would you like to edit anything else?  \n1. Yes\n2. No");
                    keepEditing = scanner.nextInt();
                    scanner.nextLine();
                }
                return null;
            }
        };
    }

    private int getLegalSupplierID() {
        int supplierID = scanner.nextInt();
        scanner.nextLine();
        ResponseT<Boolean> res = integratedService.isLegalSupplierID(supplierID);
        while ((res.isErrorOccurred() || !res.getValue())) {
            System.out.println("There is no supplier with this ID, Please try again");
            supplierID = scanner.nextInt();
            scanner.nextLine();
            res = integratedService.isLegalSupplierID(supplierID);
        }
        return supplierID;
    }

    private Actions deleteOrder() {
        return new Actions() {
            @Override
            public Object act() {
                System.out.println("please enter order ID");
                int orderID = 0;
                try {
                    orderID = Integer.parseInt(scanner.nextLine());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                return integratedService.deleteOrder(orderID);
            }
        };
    }


    private Actions moveItem() {
        return new Actions() {
            @Override
            public Object act() {
                System.out.println("please enter the product ID");
                int ProID = 0;
                try {
                    ProID = Integer.parseInt(scanner.nextLine());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                System.out.println("please enter the amount you want to move");
                int amount = 0;
                try {
                    amount = Integer.parseInt(scanner.nextLine());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                System.out.println("please enter the new location");
                String newPlace = scanner.nextLine();
                System.out.println("please enter toShelf or to toStorage");
                boolean toShelf = scanner.nextLine().equals("toShelf");
                return integratedService.moveItem(ProID, amount, newPlace, toShelf);
            }
        };
    }


    private Actions moveOneItem() {
        return new Actions() {
            @Override
            public Object act() {
                System.out.println("please enter the product ID");
                int ProID = 0;
                try {
                    ProID = Integer.parseInt(scanner.nextLine());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                System.out.println("please enter the ItemID of the item you want to move");
                int ItemID = 0;
                try {
                    ItemID = Integer.parseInt(scanner.nextLine());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                System.out.println("please enter the new place");
                String newPlace = scanner.nextLine();
                System.out.println("please enter toShelf or to toStorage");
                boolean toShelf = scanner.nextLine().equals("toShelf");
                return integratedService.moveOneItem(ProID, ItemID, newPlace, toShelf);
            }
        };
    }

    private Actions createNewProduct() {
        return new Actions() {
            @Override
            public Object act() {
                System.out.println("please enter the product name");
                String name = scanner.nextLine();
                System.out.println("please enter the product manufacturer");
                String manufacturer = scanner.nextLine();
                System.out.println("please enter the product Min Quantity");
                int MinQuantity = 0;
                try {
                    MinQuantity = Integer.parseInt(scanner.nextLine());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                System.out.println("please enter the product sell price");
                double sellPrice = 0;
                try {
                    sellPrice = Double.parseDouble(scanner.nextLine());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                System.out.println("please enter the product category");
                String category = scanner.nextLine();
                System.out.println("please enter the product sub category");
                String subCategory = scanner.nextLine();
                System.out.println("please enter the product sub sub category");
                String subSubCategory = scanner.nextLine();
                System.out.println("please enter the product weight");
                double weight = 0.0;
                try {
                    weight = Double.parseDouble(scanner.nextLine());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                return integratedService.addProduct(name, manufacturer, MinQuantity, sellPrice, category, subCategory, subSubCategory, weight);
            }
        };
    }

    private Actions orderItems() {
        return new Actions() {
            @Override
            public Object act() {
                System.out.println("\"Please enter the Product ID you want to order and their quantity in the format: <ProductID><Space><int><,><ProductID><Space><int><,>...\"");
                //show all items
                printAllProducts();
                String ProductNameAndQuantity = scanner.nextLine();
                System.out.println("Please type 1 if you wish to add new period order. If not type and other int");
                int periodic = 0;
                try {
                    periodic = Integer.parseInt(scanner.nextLine());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                if (periodic == 1) {
                    List<DayOfWeek> days = new ArrayList<>();
                    int day = 0;
                    System.out.println("Which days do you wish to add to the order periodic days?");
                    System.out.println("\n1. Monday \n2. Tuesday \n3. Wednesday \n4. Thursday \n5. Friday \n6. Saturday \n7. Sunday \n8.That will be all...");
                    while (day != 8) {
                        day = scanner.nextInt();
                        scanner.nextLine();
                        if (day >= 1 && day <= 7) days.add(DayOfWeek.of(day));
                        else if (day != 8)
                            System.out.println("Please enter a valid number : 1 to 7, or 8 if you done adding days");
                    }
                    return integratedService.orderItems(ProductNameAndQuantity, periodic, days);
                }
                return integratedService.orderItems(ProductNameAndQuantity, 0, null);
            }
        };
    }

    private Actions removeSpecificItems() {
        return new Actions() {
            @Override
            public Object act() {
                System.out.println("please enter the product ID");
                int ProID = 0;
                try {
                    ProID = Integer.parseInt(scanner.nextLine());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                System.out.println("please enter the ID of the item");
                int ID = 0;
                try {
                    ID = Integer.parseInt(scanner.nextLine());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                return integratedService.removeSpecificItem(ProID, ID);
            }
        };
    }

    private Actions changeCategory() {
        return new Actions() {
            @Override
            public Object act() {
                System.out.println("please enter the product ID");
                int ProID = 0;
                try {
                    ProID = Integer.parseInt(scanner.nextLine());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                System.out.println("please enter the new category ID (or you can enter the name if the category doesn't exist)");
                String newCat = scanner.nextLine();
                System.out.println("please enter main for main category,sub for sub category or subSub for subSub category that you want to change");
                String catType = scanner.nextLine();
                catType = catType.trim();
                return integratedService.changeCategory(ProID, newCat, catType);
            }
        };
    }

    //Sales
    private Actions AddSale() {
        return new Actions() {
            @Override
            public Object act() {
                System.out.println("please enter the Sale Start Date in format <Year>-<Month>-<Day>");
                String StartDate = scanner.nextLine();
                System.out.println("please enter the Sale End Date in format <Year>-<Month>-<Day>");
                String EndDate = scanner.nextLine();
                System.out.println("please enter the Categories ID's in format <ID>,<ID>,<ID>,...");
                String Categories = scanner.nextLine();
                System.out.println("please enter the Products Ids in format <ID>,<ID>,<ID>,...");
                String Products = scanner.nextLine();
                System.out.println("please enter the Sale Discount");
                String discountstr = scanner.nextLine();
                double Discount = 0;
                try {
                    Discount = Double.parseDouble(discountstr);
                } catch (Exception e) {
                }
                return integratedService.AddSale(StartDate, EndDate, Categories, Products, Discount);
            }
        };
    }

    private Actions RemoveSale() {
        return new Actions() {
            @Override
            public Object act() {
                System.out.println("Please enter the Sale ID");
                int SaleID = 0;
                try {
                    SaleID = Integer.parseInt(scanner.nextLine());
                } catch (Exception e) {
                }
                return integratedService.RemoveSale(SaleID);
            }
        };
    }

    private Actions SaleOnCategoryAtDate() {
        return new Actions() {
            @Override
            public Object act() {
                System.out.println("Please enter the Category ID");
                int categoryID = 0;
                try {
                    categoryID = Integer.parseInt(scanner.nextLine());
                } catch (Exception e) {
                }
                System.out.println("please enter the Date you want to check on in format <Year>-<Month>-<Day>");
                String CurrentDate = scanner.nextLine();
                Response res = integratedService.SaleOnCategoryAtDate(categoryID, CurrentDate);
                return res;
            }
        };
    }

    private Actions SaleOnProductAtDate() {
        return new Actions() {
            @Override
            public Object act() {
                System.out.println("Please enter the Product ID");
                String ProID = scanner.nextLine();
                int ProductID = 0;
                try {
                    ProductID = Integer.parseInt(ProID);
                } catch (Exception e) {
                }
                System.out.println("please enter the Date you want to check on in format <Year>-<Month>-<Day>");
                String CurrentDate = scanner.nextLine();
                Response res = integratedService.SaleOnProductAtDate(ProductID, CurrentDate);
                return res;
            }
        };
    }

    private Actions getSaleDiscount() {
        return new Actions() {
            @Override
            public Object act() {
                System.out.println("Please enter the Sale ID");
                String saleStr = scanner.nextLine();
                int SaleID = 0;
                try {
                    SaleID = Integer.parseInt(saleStr);
                } catch (Exception e) {
                }
                Response res = integratedService.getSaleDiscount(SaleID);
                System.out.println(((ResponseT) res).getValue());
                return res;
            }
        };
    }

    //Reports
    private Actions AddItemsToOrderReport() {
        return new Actions() {
            @Override
            public Object act() {
                System.out.println("Please note that the System will add automatically " + "items that's not reaching the min amount");
                System.out.println("Please enter the Product ID you want to order and their quantity in the format: <ProductID><Space><int><,><ProductID><Space><int><,>...");
                String ProductIDAndQuantity = scanner.nextLine();
                return integratedService.AddItemsToOrderReport(ProductIDAndQuantity);
            }
        };
    }

    private Actions orderReport() {
        return new Actions() {
            @Override
            public Object act() {
                return integratedService.createOrderReport();
            }
        };
    }

    private Actions addInventoryReport() {
        return new Actions() {
            @Override
            public Object act() {
                System.out.println("Please enter the Categories ID's you want to check in the format <CategoryID>,<CategoryID>,...");
                String CategoryString = scanner.nextLine();
                return integratedService.addInventoryReport(CategoryString);
            }
        };
    }

    private Actions addDefectiveReport() {
        return new Actions() {
            @Override
            public Object act() {
                return integratedService.addDefectiveReport();
            }
        };
    }

    private Actions getPriceAfterDiscount() {
        return new Actions() {
            @Override
            public Object act() {
                System.out.println("please enter the product ID");
                String PID = scanner.nextLine();
                int productID = 0;
                try {
                    productID = Integer.parseInt(PID);
                } catch (Exception e) {
                }
                System.out.println("please enter the Date you want to check in format <Year>-<Month>-<Day>");
                String date = scanner.nextLine();
                Response res = integratedService.getPriceAfterDiscount(productID, date);
                System.out.println(((ResponseT) res).getValue());
                return res;
            }
        };
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

//    public Boolean runProgram1() {
//        while (!stop && !mainMenu) {
//            InventoryMenu();
//        }
//        return stop;
//    }
//
//    private void InventoryMenu() {
//        System.out.println("welcome to employee menu!\n" +
//                "Please choose an action:\n" +
//                "1. Purchasing Manager activities \n" +
//                "2. Inventory Manager activities\n"+
//                "3. Return main menu");
//        try {
//            String action = scanner.nextLine();
//            int actionNum = parseIntBetween(action, 3);
//            switch (actionNum) {
//                case 1:
//                    if(!loginUser)
//                        PurchasingManagerAtivities();
//                    break;
//                case 2:
//                    if(loginUser)
//                        InventoryManagerAtivities();
//                    break;
//                case 3:
//                    mainMenu=true;
//            }
//        }
//        catch (IllegalArgumentException e) {
//            printError(e);}
//    }
//
//
//
//    private void InventoryManagerAtivities() {
//        System.out.println("Inventory menu: PurchasingManagerAtivities\n"+
//                "Please choose an action:\n"+
//                "1. Activities on products\n"+
//                "2. Activities on promotions\n"+
//                "3. Activities for reports\n"+
//                "4. return");
//
//        try {
//            String action = scanner.nextLine();
//            int actionNum = parseIntBetween(action, 4);
//            switch (actionNum) {
//                case 1:
//                    System.out.println("Inventory menu: PurchasingManagerAtivities -Activities on products \n" +
//                            "Please choose an action : \n" +
//                            "1.add prodact\n+" +
//                            "2. additem\n+"+
//                            "3. Move item\n"+
//                            "4. Move Oneitem\n"+
//                            "5. Remove item\n"+
//                            "6. Change category\n"+
//                            "7. Get price after Discount\n"+
//                            "8. return\n");//TODO
//                    while (!stop&&loginUser) {
//                        System.out.println("enter Action");
////                        try {
////                            String managerAction = scanner.nextLine();
////                            ActivitiesOnProductsAction(managerAction);
////                        } catch (IllegalArgumentException | ParseException e) {
////                            System.err.println(e.getMessage());
////                        }
//                    }
//                    break;
//
//                case 2:
//                    System.out.println("Inventory menu: PurchasingManagerAtivities - Activities on promotions \n" +
//                            "Please choose an action : \n" +
//                            "1. Add Sale\n" +
//                            "2. Remove Sale\n" +
//                            "3. Check sale on category \n" +
//                            "4. Check sale on product\n" +
//                            "5. Get sale discount\n" +
//                            "6. return\n");//TODO
//                    while (!stop&&loginUser) {
//                        System.out.println("enter Action");
////                        try {
////                            String managerAction = scanner.nextLine();
////                            ActivitiesOnPromotionsction(managerAction);
////                        } catch (IllegalArgumentException | ParseException e) {
////                            System.err.println(e.getMessage());
////                        }
//                    }
//                    break;
//                case 3:
//                    System.out.println("Inventory menu: PurchasingManagerAtivities - Activities for reports \n" +
//                            "Please choose an action : \n" +
//                            "1. create item to order report\n+" +
//                            "2. create inventory report\n" +
//                            "3. create defective Itemreport\n" +
//                            "4. return");
//                    while (!stop&&loginUser) {
//                        System.out.println("enter Action");
////                        try {
////                            String managerAction = scanner.nextLine();
////                            ActivitiesOnReportsAction(managerAction);
////                        } catch (IllegalArgumentException | ParseException e) {
////                            System.err.println(e.getMessage());
////                        }
//                    }
//
//                    break;
//                case 4:
//                    mainMenu = true;
//            }
//        }
//
//
//         catch (IllegalArgumentException e) {
//                printError(e);}
//    }
//
//
//
//    private void PurchasingManagerAtivities() {
//        System.out.println("Inventory menu: PurchasingManagerAtivities\n"+
//                "Please choose an action:\n"+
//                "1. Place an order from a supplier\n"+
//                "2. Re-order for the last order made from the supplier\n"+
//                "3. bye\n"+
//                "4. return\n");//TODO
//        while (!stop&&loginUser) {
//            System.out.println("enter Action");
//            try {
//                String action = scanner.nextLine();
//                PurchasingManagerAction(action);
//            } catch (IllegalArgumentException | ParseException e) {
//                System.err.println(e.getMessage());
//            }
//        }
//
//    }
//
//
//    private void PurchasingManagerAction(String action) throws ParseException {//TODO
//        switch (action.toLowerCase()) {
//            case "bye":
//                stop = true;
//                loginUser = false;
//                break;
//            case "return":
//                return;//TODO check
//        }}

    public void printError(IllegalArgumentException e) {
        System.out.println("Action Failed: " + e.getMessage());
    }
}


