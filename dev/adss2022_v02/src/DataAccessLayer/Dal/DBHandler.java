package DataAccessLayer.Dal;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBHandler {
    public final static String SUPPLIERS = "SUPPLIERS";
    public final static String ITEM_ORDERS = "ITEM_ORDERS";
    public final static String CONTACTS = "CONTACTS";
    public final static String PRODUCTS = "PRODUCTS";
    public final static String CATEGORIES = "CATEGORIES";
    public final static String SALES = "SALES";
    public final static String SUPERLEE_ITEMS = "SUPERLEE_ITEMS";
    public final static String ORDERS = "ORDERS";
    public final static String SUPPLIER_PRODUCT = "SUPPLIER_PRODUCTS";

    //Emplotrans
    public final static String DriverLicense = "DriverLicense";
    public final static String Driver = "Driver";
    public final static String Transport = "Transport";
    public final static String TransportDes = "TransportDes";
    public final static String ShiftStructure = "ShiftStructure";
    public final static String EmployeeConstraints = "EmployeeConstraints";
    public final static String Employee = "Employee";
    public final static String EmployeeShift = "EmployeeShift";
    public final static String Shift = "Shift";
    public final static String EmployeeTraining = "EmployeeTraining";
    public final static String Site = "Site";
    public final static String Truck = "Truck";
    public final static String Messages = "Messages";

    private static DBHandler dbHandler = null;

    public static Connection c = null;
    public int Category_ID = 1;
    public int Product_ID = 1;
    //until here, the objects are being used
    int item_id = 1;
    int supplier_id = 1;
    int agreement_id = 1;

    private DBHandler() {

    }

    public static DBHandler getInstance() {
        if (dbHandler == null) {
            return createDBHandler();
        }
        return dbHandler;
    }

    private static DBHandler createDBHandler() {
        dbHandler = new DBHandler();
        try {
            dbHandler.open();
            dbHandler.init();
            dbHandler.Emplotrans_init();
        } catch (Exception e) {
        }
        return dbHandler;
    }

    public void initIds() {
        try {
            Product_ID = ProductDAO.getInstance().getMaxID() + 1;
            Category_ID = CategoryDAO.getInstance().getMaxID() + 1;

        } catch (Exception e) {
        }
    }

    public int getCategory_ID() {
        Category_ID = CategoryDAO.getInstance().getMaxID() + 1;
        return Category_ID;
    }

    public Connection open() throws Exception {
        if (c != null && !c.isClosed())
            return c;
        Class.forName("org.sqlite.JDBC");
        c = DriverManager.getConnection("jdbc:sqlite:SuperLee.db");
        return c;
    }

    public void close() throws Exception {
        if (c != null)
            c.close();
    }

    public void init() throws Exception {
        try {
            createSupplierTable();
        } catch (Exception e) {
        }
        try {
            createItemOrderTable();
        } catch (Exception e) {
        }
        try {
            createContactsTable();
        } catch (Exception e) {
        }
        try {
            createSaleTable();
        } catch (Exception e) {
        }
        try {
            createCategoryTable();
        } catch (Exception e) {
        }
        try {
            createProductTable();
        } catch (Exception e) {
        }
        try {
            createSuperLeeItemTable();
        } catch (Exception e) {
        }
        try {
            createOrderTable();
        } catch (Exception e) {
        }
        try {
            createSupplierProductTable();
        } catch (Exception e) {
        }
    }

    public void Emplotrans_init() throws Exception {
        try {
            createTransportDesTable();
        } catch (Exception e) {
        }
        try {
            createEmployeeTable();
        } catch (Exception e) {
        }
        try {
            createSiteTable();
        } catch (Exception e) {
        }
        try {
            createTruckTable();
        } catch (Exception e) {
        }
        try {
            createDriverLicenseTable();
        } catch (Exception e) {
        }
        try {
            createDriverTable();
        } catch (Exception e) {
        }
        try {
            createTransportTable();
        } catch (Exception e) {
        }
        try {
            createShiftTable();
        } catch (Exception e) {
        }
        try {
            createEmployeeTrainingTable();
        } catch (Exception e) {
        }
        try {
            createShiftStructureTable();
        } catch (Exception e) {
        }
        try {
            createEmployeeConstraintsTable();
        } catch (Exception e) {
        }
        try {
            createEmployeeShiftTable();
        } catch (Exception e) {
        }
        try {
            createMessagesTable();
        } catch (Exception e) {
        }
    }

    public void deleteRecordsFromTables() {
        CategoryDAO.getInstance().Delete();
        CategoryDAO.getInstance().resetCache();
        ContactDAO.getInstance().Delete();
        ContactDAO.getInstance().resetCache();
        ItemOrderDAO.getInstance().Delete();
        ItemOrderDAO.getInstance().resetCache();
        OrderDAO.getInstance().Delete();
        OrderDAO.getInstance().resetCache();
        ProductDAO.getInstance().Delete();
        ProductDAO.getInstance().resetCache();
        SaleDAO.getInstance().Delete();
        SaleDAO.getInstance().resetCache();
        SuperItemDAO.getInstance().Delete();
        SuperItemDAO.getInstance().resetCache();
        SupplierDAO.getInstance().Delete();
        SupplierDAO.getInstance().resetCache();
        SupplierProductDAO.getInstance().Delete();
        SupplierProductDAO.getInstance().resetCache();

        initIds();
    }

    private void createOrderTable() throws Exception {
        Statement stmt = null;
        stmt = c.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS ORDERS " +
                "(ORDER_ID INT NOT NULL," +
                " ORDER_DATE VARCHAR(30) NOT NULL, " +
                " PERIODIC_ORDER BOOLEAN NOT NULL DEFAULT 'FALSE', " +
                " WEEK_DAYS VARCHAR(30) NOT NULL DEFAULT '-', " +
                " IS_PLACED BOOLEAN NOT NULL DEFAULT 'FALSE', " +
                " TOTAL_PRICE_BEFORE_DISCOUNT INT DEFAULT '-1', " +
                " TOTAL_PRICE_AFTER_DISCOUNT INT DEFAULT '-1', " +
                " PRIMARY KEY (ORDER_ID) )";
        stmt.executeUpdate(sql);
        c.commit();
        stmt.close();
    }

    private void createSupplierProductTable() throws Exception {
        Statement stmt = null;
        stmt = c.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS SUPPLIER_PRODUCTS " +
                "(PRODUCT_ID INT  NOT NULL," +
                " SUPPLIER_ID INT   NOT NULL, " +
                " NAME VARCHAR(30) NOT NULL, " +
                " CATALOG_NUMBER VARCHAR(30) NOT NULL, " +
                " PRICE INT NOT NULL, " +
                " DISCOUNT_BY_AMOUNT VARCHAR(30) NOT NULL DEFAULT '-'," +
                " PRIMARY KEY (PRODUCT_ID, SUPPLIER_ID) )";
        stmt.executeUpdate(sql);
        c.commit();
        stmt.close();
    }

    private void createSupplierTable() throws Exception {
        Statement stmt = null;
        stmt = c.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS SUPPLIERS " +
                "(ID INT   NOT NULL," +
                " NAME VARCHAR(30)    NOT NULL, " +
                " ADDRESS VARCHAR(30)    NOT NULL, " +
                " PAYMENT_METHOD VARCHAR(30)    NOT NULL DEFAULT 'CREDIT', " +
                " BANK_ACCOUNT VARCHAR(30)    NOT NULL, " +
                " SUPPLY_METHOD VARCHAR(30)  NOT NULL DEFAULT 'byOrder', " +
                " DELIVERY_DAYS VARCHAR(30)," +
                " PRIMARY KEY (ID)) ";
        stmt.executeUpdate(sql);
        c.commit();
        stmt.close();
    }

    private void createItemOrderTable() throws Exception {
        Statement stmt = null;
        stmt = c.createStatement();
        String sql =
                "CREATE TABLE IF NOT EXISTS ITEM_ORDERS " +
                        "(PRODUCT_ID INT  NOT NULL, " +
                        " SUPPLIER_ID INT  NOT NULL, " +
                        " ORDER_ID INT  NOT NULL, " +
                        " ORDER_DATE VARCHAR(30) NOT NULL, " +
                        " BUY_PRICE DOUBLE NOT NULL, " +
                        " AMOUNT INT NOT NULL, " +
                        " PRIMARY KEY (PRODUCT_ID, ORDER_ID) )";
        stmt.executeUpdate(sql);
        c.commit();
        stmt.close();
    }

    private void createContactsTable() throws Exception {
        Statement stmt = null;
        stmt = c.createStatement();
        String sql =
                "CREATE TABLE IF NOT EXISTS CONTACTS " +
                        "(SUPPLIER_ID INT  NOT NULL, " +
                        " NAME VARCHAR(30) NOT NULL, " +
                        " EMAIL DOUBLE NOT NULL, " +
                        " PHONE_NUMBER VARCHAR(30) NOT NULL, " +
                        " FOREIGN KEY (SUPPLIER_ID) REFERENCES SUPPLIER(ID)," +
                        " PRIMARY KEY (SUPPLIER_ID, NAME,EMAIL, PHONE_NUMBER) )";
        stmt.executeUpdate(sql);
        c.commit();
        stmt.close();
    }

    private void createProductTable() throws Exception {
        Statement stmt = null;
        stmt = c.createStatement();
        String sql =
                "CREATE TABLE IF NOT EXISTS PRODUCTS " +
                        "(PRODUCT_ID INT  NOT NULL, " +
                        " NAME VARCHAR(30) NOT NULL, " +
                        " MANUFACTURER VARCHAR(30) NOT NULL, " +
                        " SALE_ID INT  NOT NULL, " +
                        " CATEGORY_ID INT  NOT NULL, " +
                        " SUB_CATEGORY_ID INT  NOT NULL, " +
                        " SUB_SUB_CATEGORY_ID INT  NOT NULL, " +
                        " SELL_PRICE DOUBLE NOT NULL, " +
                        " MIN_QUANTITY INT NOT NULL, " +
                        " Weight DOUBLE NOT NULL, " +
                        " FOREIGN KEY (SUB_SUB_CATEGORY_ID) REFERENCES CATEGORY(ID)," +
                        " FOREIGN KEY (SUB_CATEGORY_ID) REFERENCES CATEGORY(ID)," +
                        " FOREIGN KEY (CATEGORY_ID) REFERENCES CATEGORY(ID)," +
                        " FOREIGN KEY (SALE_ID) REFERENCES SALE(ID)," +
                        " PRIMARY KEY (PRODUCT_ID) )";
        stmt.executeUpdate(sql);
        c.commit();
        stmt.close();
    }

    private void createCategoryTable() throws Exception {
        Statement stmt = null;
        stmt = c.createStatement();
        String sql =
                "CREATE TABLE IF NOT EXISTS  CATEGORIES " +
                        "(ID INT    NOT NULL, " +
                        " NAME VARCHAR(30) NOT NULL, " +
                        " CATEGORY_TYPE VARCHAR(30) NOT NULL, " +
                        " SALE_ID INT  NOT NULL, " +
                        " FOREIGN KEY (SALE_ID) REFERENCES SALE(ID)," +
                        " PRIMARY KEY (ID) )";
        stmt.executeUpdate(sql);
        c.commit();
        stmt.close();
    }

    private void createSaleTable() throws Exception {
        Statement stmt = null;
        stmt = c.createStatement();
        String sql =
                "CREATE TABLE IF NOT EXISTS SALES " +
                        "(ID INT NOT NULL, " +
                        " START_DATE VARCHAR(30) NOT NULL, " +
                        " END_DATE VARCHAR(30) NOT NULL, " +
                        " DISCOUNT DOUBLE NOT NULL, " +
                        " PRIMARY KEY (ID) )";
        stmt.executeUpdate(sql);
        c.commit();
        stmt.close();
    }

    private void createSuperLeeItemTable() throws Exception {
        Statement stmt = null;
        stmt = c.createStatement();
        String sql =
                "CREATE TABLE IF NOT EXISTS SUPERLEE_ITEMS " +
                        "(ID INT  NOT NULL, " +
                        " PRODUCT_ID INT  NOT NULL, " +
                        " ORDER_ID INT  NOT NULL, " +
                        " LOCATION VARCHAR(30) , " +
                        " BUY_PRICE DOUBLE  NOT NULL, " +
                        " STORAGE_OR_SHELF VARCHAR(30) NOT NULL DEFAULT 'SHELF', " +
                        " EXP_DATE VARCHAR(30) NOT NULL, " +
                        " DEFECTIVE BOOLEAN NOT NULL DEFAULT '0', " +
                        " FOREIGN KEY (ORDER_ID) REFERENCES ITEM_ORDER(ID)," +
                        " FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCTS(ID)," +
                        " PRIMARY KEY (ID) )";
        stmt.executeUpdate(sql);
        c.commit();
        stmt.close();
    }

    //emplotrans

    private void createDriverLicenseTable() throws Exception {
        Statement stmt = null;
        stmt = c.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS DriverLicense " +
                "(ID INT NOT NULL," +
                " License VARCHAR(30) NOT NULL, " +
                " FOREIGN KEY(ID) REFERENCES Employee(ID)," +
                " PRIMARY KEY (ID,License) )";
        stmt.executeUpdate(sql);
        c.commit();
        stmt.close();
    }

    private void createDriverTable() throws Exception {
        Statement stmt = null;
        stmt = c.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS Driver " +
                "(ID INT NOT NULL," +
                " AvgDistance DOUBLE NOT NULL," +
                " FOREIGN KEY(ID) REFERENCES Employee(ID)," +
                " PRIMARY KEY (ID) )";
        stmt.executeUpdate(sql);
        c.commit();
        stmt.close();
    }

    private void createTransportTable() throws Exception {
        Statement stmt = null;
        stmt = c.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS Transport " +
                "(ID INT NOT NULL," +
                " DateAndTime VARCHAR(30) NOT NULL," +
                " Ddn INT NOT NULL," +
                " SourceID INT NOT NULL," +
                " TruckID INT NOT NULL," +
                " DriverID INT NOT NULL," +
                " TotalWeight DOUBLE NOT NULL," +
                " orderID INT NOT NULL," +
                " IsReceived BOOLEAN NOT NULL," +
                " FOREIGN KEY(TruckID) REFERENCES Truck(ID)," +
                " FOREIGN KEY(DriverID) REFERENCES Employee(ID)," +
                " PRIMARY KEY (ID) )";
        stmt.executeUpdate(sql);
        c.commit();
        stmt.close();
    }

    private void createTransportDesTable() throws Exception {
        Statement stmt = null;
        stmt = c.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS TransportDes " +
                "(IdTransport INT NOT NULL," +
                " IdSite INT NOT NULL," +
                " PRIMARY KEY (IdTransport,IdSite) )";
        stmt.executeUpdate(sql);
        c.commit();
        stmt.close();
    }


    private void createShiftStructureTable() throws Exception {
        Statement stmt = null;
        stmt = c.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS ShiftStructure " +
                "(ShiftType VARCHAR(30) NOT NULL," +
                " ShiftDate VARCHAR(30) NOT NULL," +
                " JobType VARCHAR(30) NOT NULL," +
                " NumOfEmployee INT NOT NULL," +
                " FOREIGN KEY(ShiftType) REFERENCES Shift(Type)," +
                " FOREIGN KEY(ShiftDate) REFERENCES Shift(Date)," +
                " PRIMARY KEY (ShiftType, ShiftDate, JobType) )";
        stmt.executeUpdate(sql);
        c.commit();
        stmt.close();
    }

    private void createEmployeeConstraintsTable() throws Exception {
        Statement stmt = null;
        stmt = c.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS EmployeeConstraints " +
                "(EmployeeID INT NOT NULL," +
                " ShiftType VARCHAR(30) NOT NULL," +
                " Date VARCHAR(30) NOT NULL," +
                " FOREIGN KEY(ShiftType) REFERENCES Shift(Type)," +
                " FOREIGN KEY(Date) REFERENCES Shift(Date)," +
                " FOREIGN KEY(EmployeeID) REFERENCES Employee(ID)," +
                " PRIMARY KEY ( EmployeeID, Date, ShiftType) )";
        stmt.executeUpdate(sql);
        c.commit();
        stmt.close();
    }

    private void createEmployeeTable() throws Exception {
        Statement stmt = null;
        stmt = c.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS Employee " +
                "(ID INT NOT NULL," +
                " Name VARCHAR(30) NOT NULL," +
                " BankAccount INT NOT NULL," +
                " Salary INT NOT NULL," +
                " HiringConditions VARCHAR(30) NOT NULL," +
                " StartOfEmployment VARCHAR(30) NOT NULL," +
                " FinishWorking VARCHAR(30) NOT NULL," +
                " JobType VARCHAR(30) NOT NULL," +
                " PRIMARY KEY (ID) )";
        stmt.executeUpdate(sql);
        c.commit();
        stmt.close();
    }

    private void createEmployeeShiftTable() throws Exception {
        Statement stmt = null;
        stmt = c.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS EmployeeShift " +
                "(ShiftType VARCHAR(30) NOT NULL," +
                " ShiftDate VARCHAR(30) NOT NULL," +
                " EmployeeID INT NOT NULL," +
                " FOREIGN KEY(EmployeeID) REFERENCES Employee(ID)," +
                " FOREIGN KEY(ShiftDate) REFERENCES Shift(Date)," +
                " FOREIGN KEY(ShiftType) REFERENCES Shift(Type)," +
                " PRIMARY KEY (ShiftType ,ShiftDate ,EmployeeID) )";
        stmt.executeUpdate(sql);
        c.commit();
        stmt.close();
    }

    private void createShiftTable() throws Exception {
        Statement stmt = null;
        stmt = c.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS Shift " +
                "(Type VARCHAR(30) NOT NULL," +
                " Date VARCHAR(30) NOT NULL," +
                " ShiftManagerID INT NOT NULL," +
                " PRIMARY KEY (Type, Date) )";
        stmt.executeUpdate(sql);
        c.commit();
        stmt.close();
    }

    private void createEmployeeTrainingTable() throws Exception {
        Statement stmt = null;
        stmt = c.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS EmployeeTraining " +
                "(ID INT NOT NULL," +
                " TrainingName VARCHAR(30) NOT NULL," +
                " FOREIGN KEY(ID) REFERENCES Employee(ID)," +
                " PRIMARY KEY (ID, TrainingName) )";
        stmt.executeUpdate(sql);
        c.commit();
        stmt.close();
    }

    private void createSiteTable() throws Exception {
        Statement stmt = null;
        stmt = c.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS Site " +
                "(ID INT NOT NULL," +
                " ContactPerson VARCHAR(30) NOT NULL," +
                " ShippingArea VARCHAR(30) NOT NULL," +
                " PhoneNumber VARCHAR(30) NOT NULL," +
                " address VARCHAR(30) NOT NULL," +
                " PRIMARY KEY (ID) )";
        stmt.executeUpdate(sql);
        c.commit();
        stmt.close();
    }

    private void createTruckTable() throws Exception {
        Statement stmt = null;
        stmt = c.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS Truck " +
                "(ID INT NOT NULL," +
                " LicenseNumber VARCHAR(30) NOT NULL," +
                " Model VARCHAR(30) NOT NULL," +
                " TruckWeight DOUBLE NOT NULL," +
                " MaxWeight DOUBLE NOT NULL," +
                " TypeOfLicense VARCHAR(30) NOT NULL," +
                " PRIMARY KEY (ID) )";
        stmt.executeUpdate(sql);
        c.commit();
        stmt.close();
    }

    private void createMessagesTable() throws Exception {
        Statement stmt = null;
        stmt = c.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS Messages " +
                "(ID INT NOT NULL," +
                " jobType VARCHAR(30) NOT NULL," +
                " message VARCHAR(30) NOT NULL," +
                " read BOOLEAN NOT NULL DEFAULT '0'," +
                " FOREIGN KEY(jobType) REFERENCES ShiftStructure(JobType)," +
                " PRIMARY KEY (ID) )";
        stmt.executeUpdate(sql);
        c.commit();
        stmt.close();
    }
}
