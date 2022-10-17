package InterfaceLayer;

import DataAccessLayer.Dal.DBHandler;
import ServiceLayer.ModulesServices.IntegratedService;

import java.text.ParseException;
import java.util.Scanner;


public class Main {
    private static IntegratedService service = IntegratedService.getInstance();

    private static Scanner scanner = new Scanner(System.in);
    private static boolean stop;
    private static String[] job_id;
    private static boolean isLogin;


    public static void main(String[] args) throws ParseException {

       //to delete all - for testing the code
//        DBHandler.getInstance().deleteRecordsFromTables();
//        service.DelData();
        System.out.println("Welcome to Super-Lee System !\n");
        System.out.println("load data ? y/n");
        String yesOrNo = scanner.nextLine();
        if(yesOrNo.equals("y")){
            service.loadData();
            System.out.println("data load \n");
        }
        service.startingProgram();

        stop = false;
        isLogin = false;
        start();
    }


    private static void start() throws ParseException {

        EmployeeCLI employeeCLI = new EmployeeCLI();
        SupplierCLI supplierCLI = new SupplierCLI();
        InventoryCli inventoryCLI = new InventoryCli(supplierCLI);
        TransportCLI transportCLI = new TransportCLI();

        while (!stop) {


            System.out.println("\nThe appropriate options for each actor in Super-Lee System:\n" +
                    "PersonalManager: Employee Menu\n" +
                    "Storekeeper: Inventory Menu and Suppliers Menu\n" +
                    "LogisticsManager: Transports Menu\n" +
                    "all employees: Employee Menu - Information and personal update\n");

            while (job_id == null) {
                System.out.println("please login to the system\n");
                job_id = employeeCLI.login();
            }
            while (job_id != null && !stop) {

                System.out.println("\nPlease choose an action:\n" + "E - Enter Employee Menu");

                if (job_id[0].equals("Storekeeper") | job_id[0].equals("StoreManager")) {
                    System.out.println("S - Enter Suppliers Menu");
                }
                if (job_id[0].equals("PersonalManager") | job_id[0].equals("TransportManager") | job_id[0].equals("Storekeeper")|job_id[0].equals("StoreManager")) {
                    System.out.println("I - Enter Inventory Menu");
                }
                if (job_id[0].equals("TransportManager")|job_id[0].equals("StoreManager")) {
                    System.out.println("T - Enter Transports Menu");
                }
                System.out.println("Q - Quit \n" +
                        "L - logout");


                String ActionNum = scanner.nextLine();
                try {
                    switch (ActionNum) {
                        case "E":
                            returnValue(employeeCLI.runProgram(job_id[0]));
                            break;
                        case "S":
                            if (job_id[0].equals("Storekeeper") | job_id[0].equals("StoreManager")) {
                                returnValue(supplierCLI.runProgram());
                            }
                            break;
                        case "T":
                                returnValue(transportCLI.runProgram(job_id[0]));
                            break;
                        case "I":
                            if (job_id[0].equals("Storekeeper") | job_id[0].equals("TransportManager") | job_id[0].equals("PersonalManager")|job_id[0].equals("StoreManager")) {
                                returnValue(inventoryCLI.runProgram(job_id[0]));
                            }
                            break;
                        case "Q":
                            stop = true;
                        case "L":
                            employeeCLI.logout();
                            job_id = null;
                    }
                } catch (IllegalArgumentException e) {
                    employeeCLI.printError(e);
                }
            }
        }
        System.out.println("thanks you use our system");

    }

    private static int parseIntBetween(String data, int end) {
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
    private static void returnValue(int i) {
        if (i == 0)//stop
            stop = true;
        if (i == 1)//logout -> else main menu
            job_id = null;
    }

}