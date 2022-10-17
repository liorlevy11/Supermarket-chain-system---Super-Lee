package InterfaceLayer;


import ServiceLayer.ModulesServices.IntegratedService;
import ServiceLayer.ServiceObjects.EmployeeS;
import ServiceLayer.ServiceObjects.SiteS;
import ServiceLayer.ServiceObjects.TransportS;
import ServiceLayer.ServiceObjects.TruckS;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TransportCLI {
    private final IntegratedService integratedService;
    //private final EmploTransportService integratedService;
    private final Scanner scanner;
    private static boolean stop;
    private final DateTimeFormatter formatter_2;
    private static boolean loginUser;
    private static boolean mainMenu;
    private static boolean returnmain;

    public TransportCLI() {

        integratedService = IntegratedService.getInstance();
        this.scanner = new Scanner(System.in);
        formatter_2 = DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm");
    }

    public int runProgram(String jobType) throws ParseException {
        stop = false;
        mainMenu= false;
        loginUser=true;
        returnmain =false;
        integratedService.startingProgram();
        while (!stop && !mainMenu&&loginUser) {
            if(jobType.equals("TransportManager"))
                TransportMenu();
            if(jobType.equals("StoreManager"))
                reportMenu();
        }
        if(stop)//bye
            return 0;
        if (!loginUser)//logout
            return 1;
        return 2;//return main menu
    }
    private void reportMenu() {
        returnmain=false;
        System.out.println("welcome to Report menu!\n" +
                "Please choose an action:\n1. transport report\n2. truck report\n3. site report\n" +
                "4. Return main menu");
        try {
            String action = scanner.nextLine();
            int actionNum = parseIntBetween(action, 5);
            switch (actionNum) {
                case 1:
                    displayTransports();
                    break;
                case 2:
                    displayTrucks();
                    break;
                case 3:
                    displaySites();
                    break;
                case 4:
                    mainMenu = true;
            }
        }
        catch (IllegalArgumentException e) {
            printError(e);}
    }

    private void TransportMenu() {
        returnmain=false;
        System.out.println("welcome to Transport menu!\n" +
                "Please choose an action:\n" +
                "1. Management trucking sites operations\n" +
                "2. Truck Management operations\n"+
                "3. Transport management operations\n"+
                "4. Display drivers\n"+
                "5. Return main menu");

        try {
            String action = scanner.nextLine();
            int actionNum = parseIntBetween(action, 5);
            switch (actionNum) {
                case 1:
                    ManagementTruckingSitesOperations();
                    break;
                case 2:
                    TruckManagementoperations();
                    break;
                case 3:
                    TransportManagementOperations();
                    break;
                case 4:
                    Displaydrivers();
                    break;
                case 5:
                    mainMenu=true;
            }
        }
        catch (IllegalArgumentException e) {
            printError(e);}
    }


    private void ManagementTruckingSitesOperations() {
        System.out.println("Transport menu: Management trucking sites operations\n"+
                "Please choose an action:\n"+
//                "1. add site\n"+
                "1. display sites\n"+
                "2. update site contact person\n"+
                "3. update site phone number\n"+
                "4. update site address\n"+
                "5. update site shipping area\n"+
                "6. logout\n"+
                "7. bye\n"+
                "8. return\n");
        while (!stop&&loginUser&&!returnmain) {
            System.out.println("enter Action");
            try {
                String action = scanner.nextLine();
                SitesAction(action);
            } catch (IllegalArgumentException | ParseException e) {
                System.err.println(e.getMessage());
            }
        }
    }


    private void TruckManagementoperations() {
        System.out.println("Transport menu: TruckManagementoperations\n"+
                "Please choose an action:\n"+
                "1. add truck\n"+
                "2. display trucks\n"+
                "3. update truck license number\n"+
                "4.update truck model\n"+
                "5. update truck weight\n"+
                "6. update truck max weight\n"+
                "7. update truck license type\n"+
                "8. logout\n"+
                "9. bye\n"+
                "10. return\n");
        while (!stop&&loginUser&&!returnmain) {
            System.out.println("enter Action");
            try {
                String action = scanner.nextLine();
                TruckAction(action);
            } catch (IllegalArgumentException | ParseException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private void TransportManagementOperations() {
        System.out.println("Transport menu: Transport Management Operations\n"+
                "Please choose an action:\n"+
                "1. Transport Operations \n"+
                "2. Update transport\n"+
                "3. return\n");

        try {
            String action = scanner.nextLine();
            int actionNum = parseIntBetween(action, 3);
            switch (actionNum) {
                case 1:
                    System.out.println("Transport menu: Transport Management Operations\n" +
                            "Please choose an action: \n"+
                            "1. add transport\n" +
//                            "2. del transport\n" +
                            "2. display transports\n" +
                            "3. display old transports\n" +
                            "4. display future transports\n" +
                            "5. logout\n" +
                            "6. bye\n" +
                            "7. return\n");
                    while (!stop && loginUser&!returnmain) {
                        System.out.println("enter Action");
                        try {
                            String actionT = scanner.nextLine();
                            TransportAction(actionT);
                        } catch (IllegalArgumentException | ParseException e) {
                            System.err.println(e.getMessage());
                        }
                    }
                    break;
                case 2:
                    System.out.println("Transport menu: Update transport\n"+
                            "Please choose an action: \n"+
                            "1. update transport date\n"+
                            "2. update transport ddn\n"+
                            "3. update transport source\n"+
                            "4. update transport truck\n"+
                            "5. update transport driver\n"+
                            "6. update transport total weight\n"+
                            "7. add transport des\n"+
                            "8. rem transport des\n"+
                            "9. logout\n"+
                            "10. bye\n"+
                            "11. return\n");
                    while (!stop&&loginUser&&!returnmain) {
                        System.out.println("enter Action");
                        try {
                            String managerAction = scanner.nextLine();
                            UpdateTransportAction(managerAction);
                        } catch (IllegalArgumentException | ParseException e) {
                            System.err.println(e.getMessage());
                        }
                    }
                    break;

                case 3:
                    return;
            }
        }
        catch (IllegalArgumentException e) {
            printError(e);}
    }


    private void Displaydrivers() {
        System.out.println("Transport menu: Management trucking sites operations\n"+
                "Please choose an action:\n"+
                "1. display available trucks\n"+
                "2. display available drivers\n"+
                "3. logout\n"+
                "4. bye\n"+
                "5. return\n");
        while (!stop&&loginUser&&!returnmain) {
            System.out.println("enter Action");
            try {
                String action = scanner.nextLine();
                DisplaydriversAction(action);
            } catch (IllegalArgumentException | ParseException e) {
                System.err.println(e.getMessage());
            }
        }
    }



    private void SitesAction(String action) throws ParseException {
        switch (action.toLowerCase()) {

//            case "1": {
//                AddSite();
//                break;
//            }
            case "1": {
                displaySites();
                break;
            }
            case "2": {
                UpdateSiteContactPerson();
                break;
            }
            case "3": {
                UpdateSitePhoneNumber();
                break;
            }
            case "4": {
                UpdateSiteAddress();
                break;
            }
            case "5": {
                UpdateSiteShippingArea();
                break;
            }
            case "6":
                integratedService.logout();
                loginUser=false;
                mainMenu=true;
                System.out.println("logout succeeded");
                break;

            case "8":
                stop = true;
                loginUser=false;
                break;

            case "9":
                returnmain=true;

            default:
                System.out.println("please enter a valid action");
                break;

        }
    }



    private void TruckAction(String action) throws ParseException {
        switch (action.toLowerCase()) {
            case "1": {
                AddTruck();
                break;
            }
            case "2": {
                displayTrucks();
                break;
            }
            case "3": {
                UpdateTruckLicenseNumber();
                break;
            }
            case "4": {
                UpdateTruckModel();
                break;
            }
            case "5": {
                UpdateTruckWeight();
                break;
            }
            case "6": {
                UpdateTruckMaxWeight();
                break;
            }

            case "7": {
                UpdateTruckLicense();
                break;
            }
            case "8":
                integratedService.logout();
                loginUser=false;
                mainMenu=true;
                System.out.println("logout succeeded");
                break;

            case "9":
                stop = true;
                loginUser=false;
                break;
            case "10":
                returnmain=true;
                break;
            default:
                System.out.println("please enter a valid action");
                break;


        }
    }



    private void TransportAction(String action) throws ParseException {
        switch (action.toLowerCase()) {
            case "1": {
                AddTransport();
                break;
            }
//            case "2": {
//                delTransport();
//                break;
//            }
            case "2": {
                displayTransports();
                break;
            }
            case "3": {
                DisplayOldTransport();
                break;
            }
            case "4": {
                DisplayFutureTransport();
                break;
            }
            case "5":
                integratedService.logout();
                loginUser=false;
                mainMenu=true;
                System.out.println("logout succeeded");
                break;
            case "6":
                stop = true;
                loginUser=false;
                break;

            case  "7":
                returnmain=true;
                break;
            default:
                System.out.println("please enter a valid action");
                break;

        }
    }

    private void UpdateTransportAction(String action) throws ParseException {
        switch (action.toLowerCase()) {

            case "1": {
                UpdateTransportDate();
                break;
            }

            case "2": {
                UpdateTransportDDN();
                break;
            }

            case "3": {
                updateTransportSource();
                break;
            }
            case "4": {
                updateTransportTruck();
                break;
            }

            case "5": {
                updateTransportDriver();
                break;
            }

            case "6": {
                UpdateTransportTotalWeight();
                break;
            }

            case "7": {
                addTransportDestination();
                break;
            }

            case "8": {
                removeTransportDestination();
                break;
            }
            case "9":
                integratedService.logout();
                loginUser=false;
                mainMenu=true;
                System.out.println("logout succeeded");
                break;
            case "10":
                stop = true;
                loginUser=false;
                break;
            case  "11":
                returnmain=true;
            default:
                System.out.println("please enter a valid action");
                break;


        }
    }


    private void DisplaydriversAction(String action) throws ParseException {
        switch (action.toLowerCase()) {

            case "1": {
                displayAvailableTrucks();
                break;
            }
            case "2": {
                displayAvailableDrives();
                break;
            }
            case "3":
                integratedService.logout();
                loginUser=false;
                mainMenu=true;
                System.out.println("logout succeeded");
                break;
            case "4":
                stop = true;
                loginUser=false;
                break;
            case  "5":
                returnmain=true;
            default:
                System.out.println("please enter a valid action");
                break;
        }
    }

    //general
    public void printError(IllegalArgumentException e) {
        System.out.println("Action Failed: " + e.getMessage());
    }


    //parsing
    private double parseDoubleWithPrint(String data) {
        System.out.println(data);
        String s = scanner.nextLine();
        boolean suc = false;
        double num = 0;
        while (!suc) {
            try {
                num = Double.parseDouble(s);
                suc = true;
            } catch (Exception e) {
                System.out.println("please enter a valid number");
                System.out.println(data);
                s = scanner.nextLine();
            }
        }
        return num;
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
    private int parseIntWithPrint(String data) {
        System.out.println(data);
        String s = scanner.nextLine();
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



    private LocalDateTime parseLocalDateTime(String data) {
        String s = data;
        boolean suc = false;
//        DateTimeFormatter formatter_2 = formatter_2=DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm");

        LocalDateTime d = LocalDateTime.parse("01/01/1000-08:00", formatter_2);
        while (!suc) {
            try {
                d = LocalDateTime.parse(s, formatter_2);
                suc = true;
            } catch (Exception e) {
                System.out.println("please enter a valid date time");
                s = scanner.nextLine();
            }
        }
        return d;
    }

    //site operations
    public void AddSite() {
        System.out.println("please enter idSite");
        String idSiteString = scanner.nextLine();
        Integer idSite = Integer.parseInt(idSiteString);
        System.out.println("please enter address");
        String address = scanner.nextLine();
        System.out.println("please enter name of contact person");
        String contactPerson = scanner.nextLine();
        System.out.println("please enter contact phone number");
        String phoneNumber = scanner.nextLine();
        System.out.println("please enter shipping area");
        String shippingArea = scanner.nextLine();
        try {
            integratedService.AddSite(idSite,contactPerson, phoneNumber, address, shippingArea);
            System.out.println("site added successfully");
        } catch (IllegalArgumentException e) {
            printError(e);
        }
    }

    public void displaySites() {
        HashMap<Integer, SiteS> sites = integratedService.DisplaySites();
        if (sites.size() == 0)
            System.out.println("There is no Sites in System");
        else {
            System.out.println("All System Sites:");
            for (SiteS s : sites.values()) {
                System.out.println(s);
            }
        }
    }

    public void UpdateSiteContactPerson() {
        int id = updateSite();
        System.out.println("please enter new contact person name for id site " + id);
        String contactPerson = scanner.nextLine();
        try {
            integratedService.UpdateSiteContactPerson(id, contactPerson);
            SuccessfulSiteUpdate(id);
        } catch (IllegalArgumentException e) {
            printError(e);
        }

    }

    public void UpdateSitePhoneNumber() {
        int id = updateSite();
        System.out.println("please enter new phone number for id site " + id);
        String phoneNumber = scanner.nextLine();
        try {
            integratedService.UpdateSitePhoneNumber(id, phoneNumber);
            SuccessfulSiteUpdate(id);
        } catch (IllegalArgumentException e) {
            printError(e);
        }
    }

    public void UpdateSiteAddress() {
        int id = updateSite();
        System.out.println("please enter new phone number for id site " + id);
        String address = scanner.nextLine();
        try {
            integratedService.UpdateSiteAddress(id, address);
            SuccessfulSiteUpdate(id);
        } catch (IllegalArgumentException e) {
            printError(e);
        }
    }

    public void UpdateSiteShippingArea() {
        int id = updateSite();
        System.out.println("please enter new phone number for id site " + id);
        String shippingArea = scanner.nextLine();
        try {
            integratedService.UpdateSiteShippingArea(id, shippingArea);
            SuccessfulSiteUpdate(id);
        } catch (IllegalArgumentException e) {
            printError(e);
        }
    }

    public int updateSite() {
        displaySites();
        System.out.println("please choose site id you would like to update");
        String id = scanner.nextLine();
        boolean suc = false;
        int Id = 0;
        while (!suc) {
            try {
                Id = Integer.parseInt(id);
                suc = true;
            } catch (Exception e) {
                System.out.println("please enter a valid Integer");
                System.out.println("please choose site id you would like to update");
                id = scanner.nextLine();
            }
        }
        return Id;
    }

    public void SuccessfulSiteUpdate(int idSite) {
        System.out.println("Site updated Successfully!");
        System.out.println(integratedService.getSite(idSite));
    }

    //truck operations
    public void AddTruck() {
        System.out.println("please enter truck license Number");
        String licenseNumber = scanner.nextLine();
        System.out.println("please enter truck model name");
        String model = scanner.nextLine();
//        System.out.println("please enter truck weight");
//        String TruckWeight = scanner.nextLine();
//        double myTruckWeight = parsedouble(TruckWeight);
//        System.out.println("please enter truck max capacity weight");
//        String maxTruckWeight = scanner.nextLine();
//        double mymaxWeight = parsedouble(maxTruckWeight);
        double myTruckWeight = parseDoubleWithPrint("please enter truck weight");
        double mymaxWeight = parseDoubleWithPrint("please enter truck max capacity weight");
        System.out.println("please enter Driver's license required for truck");
        String license = scanner.nextLine();
        try {
            integratedService.AddTruck(licenseNumber, model, myTruckWeight, mymaxWeight, license);
            System.out.println("truck added successfully");
        } catch (IllegalArgumentException e) {
            printError(e);
        }
    }

    public void displayTrucks() {
        HashMap<Integer, TruckS> trucks = integratedService.DisplayTrucks();
        if (trucks.size() == 0)
            System.out.println("There is no Trucks in System");
        else {
            System.out.println("All System Trucks:");
            for (TruckS s : trucks.values()) {
                System.out.println(s);
            }
        }
    }

    public void SuccessfulTruckUpdate(int idTruck) {
        System.out.println("Truck updated Successfully!");
        System.out.println(integratedService.getTruck(idTruck));
    }

    public int updateTruck() {
        displayTrucks();
        System.out.println("please choose truck id you would like to update");
        String id = scanner.nextLine();
        boolean suc = false;
        int Id = 0;
        while (!suc) {
            try {
                Id = Integer.parseInt(id);
                suc = true;
            } catch (Exception e) {
                System.out.println("please enter a valid Integer");
                System.out.println("please choose truck id you would like to update");
                id = scanner.nextLine();
            }
        }
        return Id;
    }

    public void UpdateTruckModel() {
        int id = updateTruck();
        System.out.println("please enter new model name for id truck " + id);
        String model = scanner.nextLine();
        try {
            integratedService.UpdateTruckModel(id, model);
            SuccessfulTruckUpdate(id);
        } catch (IllegalArgumentException e) {
            printError(e);
        }
    }

    public void UpdateTruckLicense() {
        int id = updateTruck();
        System.out.println("please enter new license for id truck " + id);
        String license = scanner.nextLine();
        try {
            integratedService.UpdateTruckLicenseType(id, license);
            SuccessfulTruckUpdate(id);
        } catch (IllegalArgumentException e) {
            printError(e);
        }
    }

    public void UpdateTruckWeight() {
        int id = updateTruck();
        double mytruckWeight = parseDoubleWithPrint("please enter new truck weight for id truck " + id);
        try {
            integratedService.UpdateTruckWeight(id, mytruckWeight);
            SuccessfulTruckUpdate(id);
        } catch (IllegalArgumentException e) {
            printError(e);
        }
    }

    public void UpdateTruckMaxWeight() {
        int id = updateTruck();
        double myTruckMaxWeight = parseDoubleWithPrint("please enter new truck max weight for id truck " + id);
        try {
            integratedService.UpdateTruckMaxWeight(id, myTruckMaxWeight);
            SuccessfulTruckUpdate(id);
        } catch (IllegalArgumentException e) {
            printError(e);
        }
    }

    public void UpdateTruckLicenseNumber() {
        int id = updateTruck();
        System.out.println("please enter new license number for id truck " + id);
        String licenseNumber = scanner.nextLine();
        try {
            integratedService.UpdateTruckLicenseNumber(id, licenseNumber);
            SuccessfulTruckUpdate(id);
        } catch (IllegalArgumentException e) {
            printError(e);
        }
    }

    public void displayAvailableTrucks() {
        System.out.println("enter date and time in format : dd/MM/yyyy-HH:mm");
        String date = scanner.nextLine();
        LocalDateTime date1 = parseLocalDateTime(date);
        HashMap<Integer, TruckS> trucks = integratedService.displayAvailableTrucks(date1);
        if (trucks.size() == 0)
            System.out.println("There is no available trucks in this date");
        else {
            System.out.println("All System available trucks at: " + date1);
            for (TruckS truckS : trucks.values())
                System.out.println(truckS);
        }
    }

    public void displayAvailableTrucks(LocalDateTime date) {
        HashMap<Integer, TruckS> trucks = integratedService.displayAvailableTrucks(date);
        if (trucks.size() == 0)
            System.out.println("There is no available trucks in this date");
        else {
            System.out.println("All System available trucks at: " + date.toString());
            for (TruckS truckS : trucks.values())
                System.out.println(truckS);
        }
    }

    //transport operations
    public void
    AddTransport() {
        System.out.println("enter date with time dd/MM/yyyy-HH:mm");
        String date = scanner.nextLine();
        LocalDateTime date1 = parseLocalDateTime(date);
        System.out.println("please enter Transport driver document number");
        String myDriverDocNum1 = scanner.nextLine();
        int myDriverDocNum = parseInt(myDriverDocNum1);
        displaySites();
        System.out.println("please choose id site for Transport source");
        String myIdSourceS = scanner.nextLine();
        int myIdSource = parseInt(myIdSourceS);
        displayTrucks();
        System.out.println("please choose id truck for Transport");
        String myIdTruckS = scanner.nextLine();
        int myIdTruck = parseInt(myIdTruckS);
        double myTotalWeight = parseDoubleWithPrint("please enter the total weight");
        try {
            integratedService.AddTransport(date1, myDriverDocNum, myIdSource, myIdTruck, myTotalWeight);
            System.out.println("Transport added successfully");
        } catch (IllegalArgumentException e) {
            printError(e);
        }
    }

    public void UpdateTransportDDN() {
        int id = updateTransport();
        int myDdn = parseIntWithPrint("please enter new Transport driver document number for id Transport " + id);
        try {
            integratedService.UpdateTransportDriverDocNum(id, myDdn);
            SuccessfulTransportUpdate(id);
        } catch (IllegalArgumentException e) {
            printError(e);
        }
    }

    public void UpdateTransportTotalWeight() {
        int id = updateTransport();
        double weight = parseDoubleWithPrint("please enter new Transport total Weight for id Transport " + id);
        try {
            integratedService.UpdateTransportTotalWeight(id, weight);
            SuccessfulTransportUpdate(id);
        } catch (IllegalArgumentException e) {
            printError(e);
        }
    }

    public void UpdateTransportDate() {
        int id = updateTransport();
        System.out.println("enter date and time dd/MM/yyyy-HH:mm");
        String date = scanner.nextLine();
        LocalDateTime date1 = parseLocalDateTime(date);
        try {
            integratedService.UpdateTransportDate(id, date1);
            SuccessfulTransportUpdate(id);
        } catch (IllegalArgumentException e) {
            printError(e);
            System.out.println("For your convenience, the drivers and truck available for this date are displayed");
            displayAvailableDrives(date1);
            displayAvailableTrucks(date1);

        }
    }

    public int updateTransport() {
        displayTransports();
        return parseInt("please choose Transport id you would like to update");
    }

    private void addTransportDestination() {
        int id = updateTransport();
        displaySites();
        int idDestination = parseIntWithPrint("please enter the id of the destination");
        try {
            integratedService.addTransportDestination(id, idDestination);
            SuccessfulTransportUpdate(id);
        } catch (IllegalArgumentException e) {
            printError(e);
        }
    }

    private void removeTransportDestination() {
        int id = updateTransport();
        displaySites();
        int idDestination = parseIntWithPrint("please enter the id of the destination");
        try {
            integratedService.RemoveTransportDestination(id, idDestination);
            SuccessfulTransportUpdate(id);
        } catch (IllegalArgumentException e) {
            printError(e);
        }
    }

    public void displayTransports() {
        HashMap<Integer, TransportS> transports = integratedService.DisplayTransports();
        if (transports.size() == 0)
            System.out.println("there is no transports in system");
        else {
            System.out.println("All System Transports:");
            for (TransportS t : transports.values()) {
                System.out.println(t);
            }
        }
    }

    private void DisplayFutureTransport() {
        int myMax = parseIntWithPrint("please enter the number of days to view the planned shipments");
        HashMap<Integer, TransportS> transports = integratedService.DisplayFutureTransport(myMax);
        if (transports.size() == 0)
            System.out.println("There is no future transports in the next: " + myMax + " days in system");
        else {
            System.out.println("All the future Transports for the next: " + myMax + " days");
            for (TransportS t : transports.values()) {
                System.out.println(t);
            }
        }
    }

    private void DisplayOldTransport() {
        HashMap<Integer, TransportS> transports = integratedService.DisplayOldTransport();
        if (transports.size() == 0)
            System.out.println("There is no old transports in system");
        else {
            System.out.println("All System old Transports:");
            for (TransportS t : transports.values()) {
                System.out.println(t);
            }
        }
    }

    public void SuccessfulTransportUpdate(int idTransport) {
        System.out.println("Transport updated Successfully!");
        System.out.println(integratedService.getTransport(idTransport));
    }

    public void delTransport() {
        displayTransports();
        int id = parseIntWithPrint("please choose Transport id you would like to delete");
        try {
            integratedService.deleteTransport(id);
            System.out.println("Transport deleted Successfully");
        } catch (IllegalArgumentException e) {
            printError(e);
        }
    }

    public void updateTransportSource() {
        int id = updateTransport();
        displaySites();
        int myIdSource = parseIntWithPrint("please choose id site for new Transport source");
        try {
            integratedService.UpdateTransportSource(id, myIdSource);
            SuccessfulTransportUpdate(id);
        } catch (IllegalArgumentException e) {
            printError(e);
        }
    }

    public void updateTransportDriver() {
        int id = updateTransport();
        System.out.println("The drivers that are available in the date of this transport");
        try {
            displayAvailableDrives(integratedService.getTransport(id).getDate());
            int myIdDriver = parseIntWithPrint("please choose the driver id");
            integratedService.UpdateTransportDriver(id, myIdDriver);
            SuccessfulTransportUpdate(id);
        } catch (IllegalArgumentException e) {
            printError(e);
        }
    }

    public void updateTransportTruck() {
        int id = updateTransport();
        System.out.println("The trucks that are available in the date of this transport");
        try {
            displayAvailableTrucks(integratedService.getTransport(id).getDate());
            int myIdTruck = parseIntWithPrint("please choose the truck id");
            integratedService.UpdateTransportTruck(id, myIdTruck);
            SuccessfulTransportUpdate(id);
        } catch (IllegalArgumentException e) {
            printError(e);
        }
    }


    //driver operations
    public void displayAvailableDrives() {
        System.out.println("enter date and time dd/MM/yyyy-HH:mm");
        String dateInString = scanner.nextLine();
        LocalDateTime date = parseLocalDateTime(dateInString);
        HashMap<Integer, EmployeeS> drivers = integratedService.displayAvailableDrivers(date);
        if (drivers.size() == 0)
            System.out.println("There is no available drivers at: " + date);
        else {
            System.out.println("All System available drivers at: " + date);
            for (EmployeeS driverS : drivers.values()) {
                System.out.println(driverS);
            }
        }
    }

    public void displayAvailableDrives(LocalDateTime date) {
        HashMap<Integer, EmployeeS> drivers = integratedService.displayAvailableDrivers(date);
        if (drivers.size() == 0)
            System.out.println("There is no available drivers at: " + date.toString());
        else {
            System.out.println("System available drivers at: " + date.toString());
            for (EmployeeS driverS : drivers.values()) {
                System.out.println(driverS);
            }
        }
    }







    private void logInSystemASTransportManager(){
        login("TransportManager");
        if(loginUser)
            return;
        System.out.println("please choose\n" +
                "for try again to login enter 1\n" +
                "to return Main Menu enter 2\n");
        String action = scanner.nextLine();
        int actionNum = parseIntBetween(action, 2);
        if(actionNum==1)
            login("PersonalManager");
        else
            mainMenu=true;
    }

    public void login(String jobType) {
        try{
            System.out.println("enter id");
            String id = scanner.nextLine();
            integratedService.login(parseInt(id),jobType);
            System.out.println("login succeeded");
            loginUser= true;

        }
        catch (IllegalArgumentException e) {
            printError(e);}
    }

    private int parseIntBetween(String data, int end) {
        String s = data;
        boolean suc = false;
        int num = 0;
        while (!suc) {
            try {
                num = Integer.parseInt(s);
                if(num>= 1 & num<=end){
                    suc = true;}
            } catch (Exception e) {
                System.out.println("please enter a valid number");
                s = scanner.nextLine();
            }
        }
        return num;
    }
}
