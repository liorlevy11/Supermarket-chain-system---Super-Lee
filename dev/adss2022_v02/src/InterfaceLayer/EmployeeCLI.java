package InterfaceLayer;

import BussinessLayer.Objects.JobType;
import BussinessLayer.Objects.ShiftType;
import ServiceLayer.ModulesServices.IntegratedService;
import ServiceLayer.ServiceObjects.EmployeeS;
import ServiceLayer.ServiceObjects.MessageS;
import ServiceLayer.ServiceObjects.ShiftS;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
public class EmployeeCLI {
    private final IntegratedService integratedService;
    private final Scanner scanner;
    private static boolean stop;
    private final DateTimeFormatter formatter_1;
    private static boolean loginUser;
    private static boolean mainMenu;
    private static boolean is_manager;
    private static boolean is_store;
    private static boolean returnmain;
    // Declaring ANSI_RESET so that we can reset the color
    public static final String ANSI_RESET = "\u001B[0m";

    // Declaring the color
    // Custom declaration
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_RED = "\u001B[31m";




    public EmployeeCLI() {
        integratedService = IntegratedService.getInstance();
        this.scanner = new Scanner(System.in);
        formatter_1 = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    }

    public int runProgram(String jobType) throws ParseException {
        stop = false;
        mainMenu= false;
        loginUser=true;
        returnmain =false;
        integratedService.startingProgram();
        is_manager=(jobType.equals("PersonalManager"));
        is_store = (jobType.equals("StoreManager"));
        while (!stop&&!mainMenu&&loginUser) {
            employeeMenu();
        }
        if(stop)//bye
            return 0;
        if (!loginUser)//logout
            return 1;
        return 2;//return main menu
    }

    private void employeeMenu() {
        returnmain=false;

        System.out.println("welcome to employee menu!\n" +
                "Please choose an action:\n" +
                "I - Information and personal update");
        if(is_manager)
            System.out.println("P - Personnel manager activities");
        if(is_store)
            System.out.println("RE - Report's employees an shift");
        System.out.println( "R - Return main menu");
        try {
            String actionNum = scanner.nextLine();
            //  int actionNum = parseIntBetween(action, 3);
            switch (actionNum) {
                case "I":
                    informationAndPersonalUpdate();
                    break;
                case "P":
                    employeeManagerActivities();
                    break;
                case "RE":
                    report();
                case "R":
                    mainMenu=true;
            }
        }
        catch (IllegalArgumentException e) {
            printError(e);}
    }

    private void informationAndPersonalUpdate() {
        System.out.println("Employee menu: \n"+
                "Please choose an action:\n"+
                "1. add constraint\n"+
                "2. delete constraint\n"+
                "3. display constraint\n"+
                "4. my shifts\n"+
                "5. logout\n"+
                "6. bye\n"+
                "7. return\n");//TODO
        while (!stop&&loginUser&&!returnmain) {
            System.out.println("enter Action");
            try {
                String action = scanner.nextLine();
                int actionNum = parseIntBetween(action, 7);
                employeeAction(actionNum);
            } catch (IllegalArgumentException | ParseException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private void report(){
        System.out.println("Report menu: \n" +
                "1. employees report\n2. employee details\n3. shift history\n4. return\n");
        try {
            String action = scanner.nextLine();
            int actionNum = parseIntBetween(action, 4);
            switch (actionNum) {
                case 1:
                    displayEmployees();
                    break;
                case 2:
                    System.out.println("enter id");
                    String id = scanner.nextLine();
                    System.out.println(integratedService.getDetails(parseInt(id)));
                    break;
                case 3:
                    shiftHistory();
                    break;
                case 4:
                    returnmain=true;
            }
        }
        catch (IllegalArgumentException e) {
            printError(e);
        }
    }

    private void employeeManagerActivities() {
        System.out.println("Employee Manager menu: \n" +
                "Please choose an action: \n" +
                "1. Employee management operations\n" +
                "2. Shift management operations\n" +
                "3. return\n");

        try {
            String action = scanner.nextLine();
            int actionNum = parseIntBetween(action, 3);
            switch (actionNum) {
                case 1:
                    System.out.println("Employee Manager menu: Employee management operations\n" +
                            "Please choose an action : \n" +
                            "1. add employee\n" +
                            "2. display employees\n" +
                            "3. employee details\n" +
                            "4. finish\n" +
                            "5. update bank\n" +
                            "6. update salary\n" +
                            "7. update job\n" +
                            "8. update hiring\n" +
                            "9. add train\n" +
                            "10. logout\n" +
                            "11. bye\n" +
                            "12. return\n");//TODO
                    while (!stop && loginUser&!returnmain) {
                        System.out.println("enter Action");
                        try {
                            String action1 = scanner.nextLine();
                            int managerAction = parseIntBetween(action1, 12);
                            EmployeeManagerEmployeesAction(managerAction);
                        } catch (IllegalArgumentException | ParseException e) {
                            System.err.println(e.getMessage());
                        }
                    }
                    break;
                case 2:
                    System.out.println("Employee Manager menu: Shift management operations\n" +
                            "Please choose an action: \n" +
                            "1. add shift\n" +
                            "2. add duplicate shift\n" +
                            "3. display available employees\n" +
                            "4. arrange\n" +
                            "5. history shift\n" +
                            "6. delete arrange\n" +
                            "7. delete employee\n" +
                            "8. add employee to shift\n" +
                            "9. switch employee\n" +
                            "10. switch manager\n" +
                            "11. update shift\n" +
                            "12. update job in shift\n" +
                            "13. logout\n" +
                            "14. bye\n" +
                            "15. return\n");

                    while (!stop && loginUser&&!returnmain) {
                        System.out.println("enter Action");
                        try {
                            String manager2 = scanner.nextLine();
                            int managerAction2 = parseIntBetween(manager2, 15);
                            EmployeeManagerShiftAction(managerAction2);
                        } catch (IllegalArgumentException | ParseException e) {
                            System.err.println(e.getMessage());
                        }
                    }
                    break;
            }
        } catch (IllegalArgumentException e) {
            printError(e);
        }
    }

    private void employeeAction(int action) throws ParseException {
        switch (action) {
            case 1:
                addConstraint();
                break;
            case 2:
                deleteConstraint();
                break;
            case 3:
                displayConstraint();
                break;
            case 4:
                myshifts();
                break;
            case 5:
                logout();
                break;
            case 6:
                stop = true;
                loginUser=false;
                break;
            case 7:
                //service.logout();
                returnmain=true;
        }}



    private void EmployeeManagerEmployeesAction(int action) throws ParseException {
        switch (action) {
            case 1:
                addEmployment();
                break;
            case 2:
                displayEmployees();
                break;
            case 3:
                System.out.println("enter id");
                String id = scanner.nextLine();
                System.out.println(integratedService.getDetails(parseInt(id)));
                break;
            case 4:
                finish();
                break;
            case 5:
                updateBank();
                break;
            case 6:
                updateSalary();
                break;
            case 7:
                updateJob();
                break;
            case 8:
                updatehiring();
                break;
            case 9:
                addTrain();
                break;
            case 10:
                logout();
                break;
            case 11:
                bye();
                break;
            case 12:
                returnmain=true;
            default:
                System.out.println("please put valid action\n");

        }
    }


    private void EmployeeManagerShiftAction(int action) throws ParseException {
        switch (action) {
            case 1:
                addShift();
                break;
            case 2:
                addDupShift();
                break;
            case 3:
                availableEmployee();
                break;
            case 4:
                arrange();
                break;
            case 5:
                shiftHistory();
                break;
            case 6:
                deleteArrange();
                break;
            case 7:
                deleteEmployee();
                break;
            case 8:
                addEmployeeToShift();
                break;
            case 9:
                switchEmployee();
                break;
            case 10:
                switchManager();
                break;
            case 11:
                updateshift();
                break;
            case 12:
                updateJobInShift();
                break;
            case 13:
                logout();
                break;
            case 14:
                bye();
                break;
            case 15:
                returnmain=true;

        }
    }


    //general
    public void printError(IllegalArgumentException e) {
        System.out.println("Action Failed: " + e.getMessage());
    }

    public static void printError(ParseException e) {
        System.out.println("Action Failed: " + e.getMessage());
    }


    //parsing


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


    private LocalDate parseLocalDate(String data) {
        String s = data;
        boolean suc = false;
        LocalDate d = LocalDate.now();
        while (!suc) {
            try {
                d = LocalDate.parse(s, formatter_1);
                suc = true;
            } catch (Exception e) {
                System.out.println("please enter a valid date");
                s = scanner.nextLine();
            }
        }
        return d;
    }


    private LocalDate localDate(String str) {
        System.out.println(str);
        String date = scanner.nextLine();
        return parseLocalDate(date);
    }

    private String shiftTime() {
        System.out.println("enter shift time (Morning or Evening) of constraint");
        return scanner.nextLine();
    }



    public String[] login() {
        try{
            System.out.println("enter id");
            String id = scanner.nextLine();
            integratedService.login(parseInt(id));
            String jobType = integratedService.getJobTypeByID(parseInt(id));
            System.out.println("login succeeded");
            loginUser= true;
            getUnreadMessages(jobType);
            String[] returnArray= {jobType,"s"};
            return returnArray;

        }
        catch (IllegalArgumentException e) {
            printError(e);}
        return  null;
    }

    //TODO new function
    public void login(String jobType) {
        try{
            System.out.println("enter id");
            String id = scanner.nextLine();
            int idInteger = parseInt(id);
            integratedService.login(idInteger,jobType);
            System.out.println("login succeeded");
            loginUser= true;
            getUnreadMessages(jobType);
        }

        catch (IllegalArgumentException e) {
            printError(e);}
    }


    private void getUnreadMessages(String jobType){
        int i = 1;
        List<MessageS> unreadMessages = integratedService.getUnreadMessages(jobType);
        if (unreadMessages.size() != 0) {
            System.out.println(ANSI_RED+ "Hello , you have " + unreadMessages.size() + " new messages"+ANSI_RESET);
            for (MessageS m : unreadMessages) {
                System.out.println(ANSI_PURPLE + i+")"+ m.getMessage() + ANSI_RESET);
                i++;
            }
        }
        else
            System.out.println("Hello , You do not have new messages");
    }



    private void addConstraint() {
        try{
            integratedService.addConstraint(localDate("enter date dd/MM/yyyy"), shiftTime());
            System.out.println("adding succeeded");
        } catch (IllegalArgumentException e) {
            printError(e);}}

    private void deleteConstraint() {
        try{
            integratedService.deleteConstraint(localDate("enter date dd/MM/yyyy"), shiftTime());
            System.out.println("deleting succeeded");
        } catch (IllegalArgumentException e) {
            printError(e);}}

    private void addEmployment() {

        System.out.println("enter id");
        String Id = scanner.nextLine();
        int id = parseInt(Id);
        System.out.println("enter name");
        String name = scanner.nextLine();
        System.out.println("enter bank account");
        String Bank = scanner.nextLine();
        int bank = parseInt(Bank);
        System.out.println("enter salary");
        String Salary = scanner.nextLine();
        int salary = parseInt(Salary);
        System.out.println("enter hiring condition");
        String hir = scanner.nextLine();
        System.out.println("enter job type");
        String job = scanner.nextLine();
        String licence = "";
        if (integratedService.isDriver(job)) {
            System.out.println("enter licence type in one line (B, C, C1, CE)");
            licence = scanner.nextLine();
        }
        integratedService.addEmployee(id, name, bank, salary, hir, job, licence);
        System.out.println("adding succeeded");
    }

    private void finish() {
        System.out.println("enter id");
        String Id = scanner.nextLine();
        int id = parseInt(Id);
        integratedService.finish(id);
        System.out.println(id + " is finish to work");
    }

    private void updateBank() {
        System.out.println("enter id");
        String Id = scanner.nextLine();
        int id = parseInt(Id);
        System.out.println("enter new bank account");
        String Bank = scanner.nextLine();
        int bank = parseInt(Bank);
        integratedService.updatebank(id, bank);
        System.out.println("update succeed");
    }

    private void updateSalary() {
        System.out.println("enter id");
        String Id = scanner.nextLine();
        int id = parseInt(Id);
        System.out.println("enter new salary");
        String Salary = scanner.nextLine();
        int salary = parseInt(Salary);
        integratedService.updateSalary(id, salary);
        System.out.println("update succeed");
    }

    private void updateJob() {
        System.out.println("enter id");
        String Id = scanner.nextLine();
        int id = parseInt(Id);
        System.out.println("enter new job");
        String jobType = scanner.nextLine();
        integratedService.updateJob(id, jobType);
        System.out.println("update succeed");
    }

    private void updatehiring() {
        System.out.println("enter id");
        String Id = scanner.nextLine();
        int id = parseInt(Id);
        System.out.println("enter new hiring condition");
        String hiring = scanner.nextLine();
        integratedService.updateHiring(id, hiring);
        System.out.println("update succeed");
    }

    private void addTrain() {
        System.out.println("enter id");
        String Id = scanner.nextLine();
        int id = parseInt(Id);
        System.out.println("enter training: TeamManagement/CancellationCard");
        String training = scanner.nextLine();
        integratedService.addTrain(id, training);
        System.out.println("adding succeeded");
    }

    private void addShift() {
        LocalDate date1 = localDate("enter date dd/MM/yyyy");
        String shiftTime = shiftTime();
        Map<String, Integer> structure = new HashMap<>();
        List<String> jobTypes = integratedService.getJobType(shiftTime);
        for (String j : jobTypes) {
            System.out.println("enter num of " + j);
            String Num = scanner.nextLine();
            int num = parseInt(Num);
            structure.put(j, num);
        }
        integratedService.addShift(date1, shiftTime, structure);
        System.out.println("adding succeeded");
    }

    private void addDupShift() {
        try {
            integratedService.addShift(localDate("enter date dd/MM/yyyy"), localDate("enter date (dd/MM/yyyy) of the shift with the structure yo want"), shiftTime());
            System.out.println("adding succeeded");
        } catch (IllegalArgumentException e) {
            printError(e);
        }
    }

    private void availableEmployee() {
        Map<String, List<EmployeeS>> employees = integratedService.getAvailableEmployees(localDate("enter date dd/MM/yyyy"), shiftTime());
        if (employees.size() == 0)
            System.out.println("There is no available employee");
        else {
            System.out.println("All System employee available:");
            for (String s : employees.keySet()) {
                System.out.println(s + ": ");
                for (EmployeeS e : employees.get(s)) {
                    System.out.println(e);
                }
            }
        }
        System.out.println();
    }

    private void arrange() {
        LocalDate date1 = localDate("enter date dd/MM/yyyy");
        String shiftTime = shiftTime();
        System.out.println("enter shift manager");
        String ShiftManager = scanner.nextLine();
        int shiftManager = parseInt(ShiftManager);
        Map<String, LinkedList<Integer>> arrange = new HashMap<>();
        List<String> jobTypes = integratedService.getJobType(shiftTime);
        for (String j : jobTypes) {
            LinkedList<Integer> l = new LinkedList<>();
            int id = 1;
            while (id != 0) {
                System.out.println("enter id for " + j + " or 0 to move to next job type");
                String Id = scanner.nextLine();
                id = parseInt(Id);
                if (id != 0)
                    l.add(id);
            }
            arrange.put(j, l);
        }
        integratedService.shiftArrange(date1, shiftTime, shiftManager, arrange);
        System.out.println("arrangement succeed");
    }

    private void shiftHistory() {
        List<ShiftS> shiftS = integratedService.displayShift(localDate("enter start date dd/MM/yyyy"), localDate("enter end date dd/MM/yyyy"));
        if (shiftS.size() == 0)
            System.out.println("There is no shifts in System");
        else {
            System.out.println("SHIFT HISTORY: ");
            for (ShiftS shiftS1 : shiftS) {
                System.out.println(shiftS1.toString());
            }
        }
    }

    private void deleteArrange() {
        integratedService.deleteArrange(localDate("enter date dd/MM/yyyy"), shiftTime());
        System.out.println("employment arrange delete successfully");
    }

    private void deleteEmployee() {
        LocalDate date1 = localDate("enter date dd/MM/yyyy");
        String shiftTime = shiftTime();
        System.out.println("enter id");
        String Id = scanner.nextLine();
        int id = parseInt(Id);
        integratedService.deleteEmployee(id, date1, shiftTime);
        System.out.println("deleting succeeded");
    }

    private void addEmployeeToShift() {
        LocalDate date1 = localDate("enter date dd/MM/yyyy");
        String shiftTime = shiftTime();
        System.out.println("enter id");
        String Id = scanner.nextLine();
        int id = parseInt(Id);
        integratedService.addEmployeeToShift(id, date1, shiftTime);
        System.out.println("adding succeeded");

    }

    private void switchEmployee() {
        LocalDate date1 = localDate("enter date dd/MM/yyyy");
        String shiftTime = shiftTime();
        System.out.println("enter id to remove from shift");
        String Id1 = scanner.nextLine();
        int id1 = parseInt(Id1);
        System.out.println("enter id to add to shift");
        String Id2 = scanner.nextLine();
        int id2 = parseInt(Id2);
        integratedService.switchEmployee(id1, id2, date1, shiftTime);
        System.out.println("switching succeeded");
    }

    private void switchManager() {
        LocalDate date1 = localDate("enter date dd/MM/yyyy");
        String shiftTime = shiftTime();
        System.out.println("enter id to remove from shift");
        String Id1 = scanner.nextLine();
        int id1 = parseInt(Id1);
        System.out.println("enter id to add to shift");
        String Id2 = scanner.nextLine();
        int id2 = parseInt(Id2);
        integratedService.switchManager(id1, id2, date1, shiftTime);
        System.out.println("switching succeeded");
    }

    private void updateshift() {
        LocalDate date1 = localDate("enter date dd/MM/yyyy");
        String shiftTime = shiftTime();
        Map<String, Integer> structure = new HashMap<>();
        List<String> jobTypes = integratedService.getJobType(shiftTime);
        for (String j : jobTypes) {
            System.out.println("enter num of " + j);
            String Num = scanner.nextLine();
            int num = parseInt(Num);
            structure.put(j, num);
        }
        integratedService.updateshift(date1, shiftTime, structure);
        System.out.println("updating succeeded");
    }

    private void updateJobInShift() {
        LocalDate date1 = localDate("enter date dd/MM/yyyy");
        String shiftTime = shiftTime();
        System.out.println("enter job");
        String job = scanner.nextLine();
        System.out.println("enter num of " + job);
        String Num = scanner.nextLine();
        int num = parseInt(Num);
        integratedService.updateJobInShift(date1, shiftTime, job, num);
        System.out.println("updating succeeded");
    }

    public void displayEmployees() {
        List<EmployeeS> employeeS = integratedService.displayEmployees();
        if (employeeS.size() == 0)
            System.out.println("There is no Employees in System");
        else {
            System.out.println("EMPLOYEES: ");
            for (EmployeeS employee : employeeS) {
                System.out.println(employee.toString());
            }
        }
    }

    private void displayConstraint() {
        Map<LocalDate, List<ShiftType>> con = integratedService.displayConstraint();
        if (con.size() == 0)
            System.out.println("There is no constraint in System");
        for (LocalDate d : con.keySet()) {
            for (ShiftType shiftType : con.get(d)) {
                System.out.println(d + " " + shiftType);
            }
        }
    }

    private void myshifts() {
        String myShift = integratedService.myshift();
        if (myShift.equals("MY SHIFTS: \n"))
            myShift = "you do not have any Shift";
        System.out.println(myShift);
    }

    public void loadData() throws ParseException {
        integratedService.loadData();
        System.out.println("Data load Successfully");
    }

    public void DelData() throws ParseException {
        integratedService.DelData();
        integratedService.startingProgram();
        System.out.println("Data deleted Successfully");
    }
    //TODO new
    public void logout(){
        integratedService.logout();
        System.out.println("logout succeeded");
        loginUser=false;
    }

    //TODO new

    private void bye(){
        stop = true;
        logout();
    }

    private void logInSystemASEmplyeeManager(){
        login("PersonalManager");
        if(loginUser)
            return;
        System.out.println("please choose\n" +
                "for try again to login enter 1\n" +
                "to return employee Menu enter 2\n");
        String action = scanner.nextLine();
        int actionNum = parseIntBetween(action, 2);
        if(actionNum==1)
            login("PersonalManager");
        else
            employeeMenu();
    }

    private void logInSystem(){
        login();
        if(loginUser)
            return;
        System.out.println("please choose\n" +
                "for try again to login enter 1\n" +
                "to return employee Menu enter 2\n");
        String action = scanner.nextLine();
        int actionNum = parseIntBetween(action, 2);
        if(actionNum==1)
            login();
        else
            employeeMenu();
    }



}


