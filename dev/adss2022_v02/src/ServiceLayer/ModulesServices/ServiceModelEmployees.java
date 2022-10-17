package ServiceLayer.ModulesServices;

import BussinessLayer.Objects.Driver;
import BussinessLayer.Objects.ShiftType;
import ServiceLayer.ServiceObjects.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ServiceModelEmployees {
    private final ShiftService shiftService;
    private final EmployeeService employeeService;

    public ServiceModelEmployees() {
        shiftService = new ShiftService();
        employeeService = new EmployeeService();
    }

    public HashMap<Integer, EmployeeS> displayAvailableDrivers(LocalDateTime date)  {
        return employeeService.displayAvailableDrivers(date);
    }

    //shifts
    public void shiftArrange(LocalDate date, String shiftType, Integer shiftMannager, Map<String, LinkedList<Integer>> inputShift){
        shiftService.shiftArrange(date,shiftType,shiftMannager,inputShift);}
    public void addShift(LocalDate date, String shiftType, Map<String, Integer> shiftStructure){
        shiftService.addShift(date,shiftType, shiftStructure);}

    public void addShift(LocalDate date, LocalDate date1, String shiftType1){
        shiftService.addShift(date,date1,shiftType1); }

    public List<ShiftS> displayShift(LocalDate start, LocalDate end) {
        return shiftService.displayShift(start, end);
    }

    public void deleteArrange(LocalDate date, String shiftType){
        shiftService.deleteArrange(date,shiftType);
    }

    public void deleteEmployee(int id, LocalDate date1, String shiftTime) {
        shiftService.deleteEmployee(id,date1,shiftTime);
    }

    public void addEmployeeToShift(int id, LocalDate date1, String shiftTime) {
        shiftService.addEmployeeToShift(id,date1,shiftTime);
    }

    public void switchEmployee(int id1, int id2, LocalDate date1, String shiftTime) {
        shiftService.switchEmployee(id1,id2,date1,shiftTime);
    }
    public void switchManager(int id1, int id2, LocalDate date1, String shiftTime) {
        shiftService.switchManager(id1,id2,date1,shiftTime);
    }
    public void updateshift(LocalDate date1, String shiftTime, Map<String, Integer> structure) {
        shiftService.updateshift(date1,shiftTime,structure);
    }

    public void updateJobInShift(LocalDate date, String shiftTime, String job, int num) {
        shiftService.updateJobInShift(date,shiftTime,job,num);
    }

    public List<String> getJobType(String shiftTime) {
        return shiftService.getJobType(shiftTime);
    }

    //employee
    public void login(int id) { employeeService.login(id);}

    //TODO new func
    public void login(int id,String jobtype) { employeeService.login(id,jobtype);}

    public void logout() { employeeService.logout();}

    public void addConstraint(LocalDate date,String shift) { employeeService.addConstraint(date,shift);}

    public void deleteConstraint(LocalDate date,String shift) { employeeService.deleteConstraint(date,shift);}

    public Map<LocalDate,List<ShiftType>> displayConstraint(){return employeeService.displayConstraint();}

    public String myshift(){ return employeeService.myshift(); }

    //manager actions
    public void addEmployee(int id, String name, int bankAccount, int salary, String hiringCondition, String jobType,String licence)
    { employeeService.addEmployee(id,name,bankAccount,salary,hiringCondition,jobType,licence); }

    public List<EmployeeS> displayEmployees() {
        return employeeService.displayEmployees();
    }
    public String getDetails(int id){ return employeeService.getDetails(id);}

    public void finish(int id) {
        employeeService.finish(id);
    }

    public void updatebank(int id, int bank) {
        employeeService.updatebank(id,bank);
    }

    public void updateSalary(int id, int salary) {
        employeeService.updateSalary(id,salary);
    }

    public void updateJob(int id, String jobType) {
        employeeService.updateJob(id,jobType);
    }

    public void updateHiring(int id, String hiring) {
        employeeService.updateHiring(id,hiring);
    }

    public void addTrain(int id, String training) { employeeService.addTrain(id,training);}

    public Map<String, List<EmployeeS>>  displayAvailableEmployees(LocalDate date, String shiftType){
        return employeeService.displayAvailableEmployees(date,shiftType);
    }

    public boolean isDriver(String jobType){
        return employeeService.isDriver(jobType);
    }

    public void TransportManager() {
        employeeService.TransportManager();
    }

    public Driver getDriver(int idDriver) {
        return employeeService.getDriver(idDriver);
    }
    public List<MessageS> getUnreadMessages(String jobType) {
        return employeeService.getUnreadMessages(jobType);
    }

    public void LoadData() throws ParseException {
        employeeService.loadData();
    }
    public String getJobTypeByID(int ID) {
        return employeeService.getJobTypeByID(ID);
    }

    public void DelData() {
        employeeService.DelData();
        //shiftService.DelData();
    }

    public List<Driver> getAvailableDriver(LocalDateTime date) {
        return employeeService.getAvailableDriver(date);
    }



}

