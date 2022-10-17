package BussinessLayer.Managers;
import BussinessLayer.Objects.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Facade {

    private  static Facade facade= null;
    private final EmployeesManager employeesManager;
    private final ShiftsManager shiftsManager;
    private Employee login;

    private Facade(){
        shiftsManager=ShiftsManager.getInstance();
        employeesManager=EmployeesManager.getInstance();
    }

    public List<Message> getUnreadMessages(String jobType) {
     return employeesManager.getUnreadMessages(jobType);
    }

    public static Facade getInstance() {
        if (facade == null)
            facade = new Facade();
        return facade;
    }

    private ShiftType shiftType(String time){
        if(time.equals("Morning"))
            return ShiftType.Morning;
        if (time.equals("Evening"))
            return ShiftType.Evening;
        throw new IllegalArgumentException("the option is Morning or Evening");
    }

    public void Login(int id){
        Employee e=employeesManager.getEmployee(id);
        if(e==null)
            throw new IllegalArgumentException("id doesn't exist");
        login=e;
    }
//TODO new fun
    public void Login(int id,String jobType){
        Employee e=employeesManager.getEmployee(id);
        if(e==null)
            throw new IllegalArgumentException("id doesn't exist");
        if(!e.getJobType().toString().equals(jobType))
            throw new IllegalArgumentException("you are not "+jobType+ " you "+ e.getJobType() );
        login=e;
    }

    public void Logout(){
        if(login==null)
            throw new IllegalArgumentException("nobody login");
        login=null;
    }

    //employment
    //add constraint
    public void addConstraint(LocalDate date, String shift) {
        if(login==null)
            throw new IllegalArgumentException("You have to logIn for this action");
        employeesManager.addConstraint(date,shiftType(shift),login);
    }

    //remove constraint
    public void deleteConstraint(LocalDate date, String shift) {
        if(login==null)
            throw new IllegalArgumentException("You have to logIn for this action");
        employeesManager.deleteConstraint(date,shiftType(shift),login);
    }

    //watch my constraint
    public Map<LocalDate,List<ShiftType>> getConstraint(){
        if(login==null)
            throw new IllegalArgumentException("You have to logIn for this action");
        return employeesManager.getConstraint(login);
    }

    //watch my shifts
    public String myshift(){
        if(login==null)
            throw new IllegalArgumentException("You have to logIn for this action");
        return employeesManager.getShift(login);
    }

    //get all the employees
    public List<Employee> employees(){
        return employeesManager.getEmployess();
    }

    public String getDetails(int id){
        return employeesManager.getDetails(id);
    }

    //shift
    //get list of ids and check if this arrange ok(the worker can work in the same day,there are enough workers...)
    //add employee
    public void addEmployee(int id, String name, int bankAccount, int salary, String hiringCondition, String jobType,String licence) {
        PersonalMannager();
        employeesManager.addEmployee(id,name,bankAccount,salary,hiringCondition,jobType,licence);
    }

    //set in employee fields
    //update employee status to finish work
    public void finish(int id) {
        PersonalMannager();
        employeesManager.finish(id);
    }
    //update bank account
    public void updatebank(int id, int bank) {
        PersonalMannager();
        employeesManager.updatebank(id,bank);
    }
    //update bank salary
    public void updateSalary(int id, int salary) {
        PersonalMannager();
        employeesManager.updateSalary(id,salary);
    }
    //update job
    public void updateJob(int id, String jobType) {
        PersonalMannager();
        employeesManager.updateJob(id,jobType);
    }
    //update hiring condition
    public void updateHiring(int id, String hiring) {
        PersonalMannager();
        employeesManager.updateHiring(id,hiring);
    }
    public void addTrain(int id, String training) {
        PersonalMannager();
        employeesManager.addTrain(id,training);
    }
    public void shiftArrange(LocalDate date,String shiftType,Integer shiftManager,Map<String,LinkedList<Integer>> inputShift){
        PersonalMannager();
        Map<JobType,LinkedList<Integer>> possibleShift=new HashMap<>();
        for (String s:inputShift.keySet()) {
            possibleShift.put(employeesManager.jobType(s), inputShift.get(s));
            if(inputShift.get(s).contains(shiftManager))
                throw new IllegalArgumentException(shiftManager+" assigned as both manager and employee");
        }
        //check employees relevant for the shift
        Map<JobType,List<Employee>> employees=employeesManager.shiftLegal(date,shiftType(shiftType),possibleShift);
        //check shift manager
        try {
            employeesManager.shiftManager(date, shiftType(shiftType), shiftManager);
            employeesManager.addShifts(date, shiftType(shiftType), employees);
            employeesManager.addEmployeetoShift(shiftManager, date, shiftType(shiftType));
            shiftsManager.setShiftList(shiftManager, possibleShift, date, shiftType(shiftType));
        }
        catch (Exception e){
            employeesManager.deleteArrange(date, shiftType(shiftType), shiftManager,employees);
            throw new IllegalArgumentException(e.getMessage());
        }
    }
    //watch all the employees that available for the shift
    public Map<JobType, List<Employee>> getAvailableEmployees(LocalDate date, String shiftType) {
        PersonalMannager();
        return employeesManager.getAvailableEmployees(date,shiftType(shiftType));
    }

    //add shift
    public void addShift(LocalDate date, String shiftType, Map<String, Integer> inputStructure) {
        PersonalMannager();
        Map<JobType,Integer> shiftStructure=new HashMap<>();
        for (String s:inputStructure.keySet()) {
            shiftStructure.put(employeesManager.jobType(s), inputStructure.get(s));
        }
        shiftsManager.addShift(date,shiftType(shiftType), shiftStructure);
    }

    //add shift with structure that exist
    public void addShift(LocalDate date, LocalDate date1, String shiftType1) {
        PersonalMannager();
        shiftsManager.addShift(date,date1,shiftType(shiftType1));
    }

    //watch shift history - between 2 dates
    public List<Shift> displayShift(LocalDate start, LocalDate end) {
        return shiftsManager.shiftbeetween(start,end);
    }

    //delete the employee's arrangement that exist for the shift
    public void deleteArrange(LocalDate date,String shiftType) {
        PersonalMannager();
        shiftsManager.deleteArrange(date,shiftType(shiftType));
    }

    //switch between to employees in the shift
    public void switchEmployees(int iddelete, int idadd, LocalDate date,String shiftType){
        PersonalMannager();
        try {
            employeesManager.switchEmployess(iddelete,idadd,date,shiftType(shiftType));
            shiftsManager.switchEmployess(iddelete,idadd,date,shiftType(shiftType));
        }
        catch (Exception e){
            employeesManager.getEmployee(idadd).deleteShift(date,shiftType(shiftType));
            employeesManager.getEmployee(iddelete).addShift(date,shiftType(shiftType));
        }
    }

    public void switchManager(int iddelete, int idadd, LocalDate date,String shiftType){
        PersonalMannager();
        if(shiftsManager.getManager(date,shiftType(shiftType))!=iddelete)
            throw new IllegalArgumentException(iddelete+" is not the shift manager of the shift");
        employeesManager.switchManager(iddelete,idadd,date,shiftType(shiftType));
        shiftsManager.switchManager(idadd,date,shiftType(shiftType));
    }

    //remove employee from shift- this update the structure of shift
    public void deleteEmployee(int iddelete, LocalDate date,String shiftType) {
        PersonalMannager();
        if (shiftsManager.getManager(date, shiftType(shiftType)) == iddelete)
            throw new IllegalArgumentException("You can't erase the shift manager- only to replace him");
        try {
            shiftsManager.deleteEmployee(iddelete, date, shiftType(shiftType), employeesManager.deleteShift(iddelete, date, shiftType(shiftType)));
        }
        catch (Exception e){
            employeesManager.getEmployee(iddelete).addShift(date,shiftType(shiftType));
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    //add employee from shift- this update the structure of shift
    public void addEmployee(int idadd, LocalDate date,String shiftType) {
        PersonalMannager();
        try {
            shiftsManager.addEmployee(idadd,employeesManager.addEmployeetoShift(idadd,date,shiftType(shiftType)),date,shiftType(shiftType));
        }
        catch (Exception e){
            employeesManager.getEmployee(idadd).deleteShift(date,shiftType(shiftType));
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    //update shift structure
    public void updateshift(LocalDate date, String shiftType, Map<String, Integer> inputStructure) {
        PersonalMannager();
        Map<JobType,Integer> shiftStructure=new HashMap<>();
        for (String s:inputStructure.keySet()) {
            shiftStructure.put(employeesManager.jobType(s), inputStructure.get(s));
        }
        shiftsManager.updateshift(date,shiftType(shiftType),shiftStructure);
    }

    //update 1 job in shift structure
    public void updateJobInShift(LocalDate date, String shiftType, String job, int num) {
        PersonalMannager();
        shiftsManager.updateJobInShift(date,shiftType(shiftType),employeesManager.jobType(job),num);
    }
    //jobs
    //get all the jobs
    public List<JobType> jobTypes(String shiftTime){
        PersonalMannager();
        return employeesManager.getJobTypeList(shiftType(shiftTime));
    }

    //help functions
    private void PersonalMannager(){
        employeesManager.PersonnelManager(login);
    }
    //func to transform model
    public void TransportManager(){
        employeesManager.TransportManager(login);
    }

    public List<Driver> getAvailableDriver(LocalDateTime date) {
        return employeesManager.getAvailableDriver(date);
    }

    public Driver getDriver(int id){
       return employeesManager.getDriver(id);
    }

    public boolean isDriver(String jobType){
        return employeesManager.isDriver(jobType);
    }
    public String getJobTypeByID(int id) {
        return employeesManager.getJobTypeByID(id);
    }

    public void loadData() throws ParseException {
        employeesManager.loadData();
        shiftsManager.loadData();
        LocalDate today= LocalDate.now();
        LocalDate date3 = today.plusDays(10);
        LocalDate date4 =today.plusDays(9);
        Login(207687849);
        addTrain(208687319, "CancellationCard");
        addTrain(208687319, "TeamManagement");
        addTrain(202876546, "CancellationCard");
        addTrain(202876546, "TeamManagement");
        Login(202876546);
        addConstraint(date3,"Morning");
        addConstraint(date3,"Evening");
        addConstraint(date4,"Morning");
        addConstraint(date4,"Evening");
        Logout();
        //insert Shift with employees
        Map<String, LinkedList<Integer>> employeesID = new HashMap<>();
        LinkedList<Integer> l1 = new LinkedList<>();
        LinkedList<Integer> l2 = new LinkedList<>();
        LinkedList<Integer> l3 = new LinkedList<>();
        LinkedList<Integer> l4 = new LinkedList<>();
        l1.add(209876543);
        l2.add(313883254);
        l2.add(206889543);
        l2.add(208649319);
        l3.add(209876544);
        l4.add(209876546);
        employeesID.put("Cashier", l1);
        employeesID.put("Driver", l2);
        employeesID.put("Storekeeper", l3);
        employeesID.put("Usher", l4);
        Login(207687849);


        LocalDate tomorrow = today.plusDays(1);


        shiftArrange(date3,"Evening",208687319,employeesID);
        shiftArrange(date4,"Evening",208687319,employeesID);
        shiftArrange(today,"Evening",208687319,employeesID);
        shiftArrange(tomorrow,"Evening",208687319,employeesID);
        Logout();
    }


    public void DelData() {
        employeesManager.DelData();
        shiftsManager.DelData();

    }



}
