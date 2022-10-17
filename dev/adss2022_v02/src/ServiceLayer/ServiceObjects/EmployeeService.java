package ServiceLayer.ServiceObjects;

import BussinessLayer.Managers.Facade;
import BussinessLayer.Objects.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;

public class EmployeeService {

    public final Facade facade;

    public EmployeeService() {
        facade = Facade.getInstance();
    }

    //employee action
    public void login(int id) {
        facade.Login(id);
    }

    //TOdo new fun
    public void login(int id,String jobType) {
        facade.Login(id,jobType);
    }

    public void logout() {
        facade.Logout();
    }

    public void addConstraint(LocalDate date, String shift) {
        facade.addConstraint(date, shift);
    }

    public void deleteConstraint(LocalDate date, String shift) {
        facade.deleteConstraint(date, shift);
    }

    public Map<LocalDate, List<ShiftType>> displayConstraint() {
        return facade.getConstraint();
    }

    public String myshift() {
        return facade.myshift();
    }

    //manager actions
    public void addEmployee(int id, String name, int bankAccount, int salary, String hiringCondition, String jobType, String licence) {
        facade.addEmployee(id, name, bankAccount, salary, hiringCondition, jobType, licence);
    }

    public List<EmployeeS> displayEmployees() {
        List<EmployeeS> employeesS = new LinkedList<>();
        List<Employee> employees = facade.employees();
        for (Employee employee : employees) {
            employeesS.add(new EmployeeS(employee));
        }
        return employeesS;
    }

    public String getDetails(int id) {
        return facade.getDetails(id);
    }

    public void finish(int id) {
        facade.finish(id);
    }

    public void updatebank(int id, int bank) {
        facade.updatebank(id, bank);
    }

    public void updateSalary(int id, int salary) {
        facade.updateSalary(id, salary);
    }

    public void updateJob(int id, String jobType) {
        facade.updateJob(id, jobType);
    }

    public void updateHiring(int id, String hiring) {
        facade.updateHiring(id, hiring);
    }

    public void addTrain(int id, String training) {
        facade.addTrain(id, training);
    }

    public Map<String, List<EmployeeS>> displayAvailableEmployees(LocalDate date, String shiftType) {
        Map<String, List<EmployeeS>> availS = new HashMap<>();
        Map<JobType, List<Employee>> avail = facade.getAvailableEmployees(date, shiftType);
        for (JobType j : avail.keySet()) {
            availS.put(j.toString(), new LinkedList<>());
            for (Employee e : avail.get(j)) {
                availS.get(j.toString()).add(new EmployeeS(e));
            }
        }
        return availS;
    }

    public HashMap<Integer, EmployeeS> displayAvailableDrivers(LocalDateTime date) {
        HashMap<Integer, EmployeeS> driverS = new HashMap<>();
        List<Driver> drivers = facade.getAvailableDriver(date);
        for (Driver d : drivers) {
            driverS.put(d.getId(), new EmployeeS(d));
        }
        return driverS;
    }
    public List<MessageS> getUnreadMessages(String jobType) {
        List<Message> messages =  facade.getUnreadMessages(jobType);
        List<MessageS> messagesS = new LinkedList<>();
        for(Message message : messages){
            messagesS.add(new MessageS(message.getIdMessage(),message.getJob(),message.getMessage(),message.isRead()));
        }
        return messagesS;
    }

    public void TransportManager() {
        facade.TransportManager();
    }
    public String getJobTypeByID(int id) {
        return facade.getJobTypeByID(id);
    }
    public Driver getDriver(int id) {
        return facade.getDriver(id);
    }

    public boolean isDriver(String jobType) {
        return facade.isDriver(jobType);
    }


    public void loadData() throws ParseException {
        facade.loadData();
    }

    public void DelData() {
        facade.DelData();
    }

    public List<Driver> getAvailableDriver(LocalDateTime date) {
       return facade.getAvailableDriver(date);
    }


}

