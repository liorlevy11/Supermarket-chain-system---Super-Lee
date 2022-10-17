package BussinessLayer.Managers;

import BussinessLayer.Objects.*;
import DataAccessLayer.*;
import DataAccessLayer.MessageDAO;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class EmployeesManager {
    private EmployeeConstraintsDAO employeeConstraintsDAO;
    private EmployeeShiftDAO employeeShiftDAO;
    private EmployeeTrainingDAO employeeTrainingDAO;
    private EmployeeDAO employeeDAO;
    private DriverDAO driverDAO;
    private DriverLicenseDAO driverLicenseDAO;
    private MessageDAO messageDAO;

    DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static EmployeesManager employeesManager = null;

    private EmployeesManager() {
        employeeConstraintsDAO = new EmployeeConstraintsDAO();
        employeeShiftDAO = new EmployeeShiftDAO();
        employeeTrainingDAO = new EmployeeTrainingDAO();
        employeeDAO = new EmployeeDAO();
        driverDAO = new DriverDAO();
        driverLicenseDAO = new DriverLicenseDAO();
        messageDAO = new MessageDAO();
    }

    public static EmployeesManager getInstance() {
        if (employeesManager == null)
            employeesManager = new EmployeesManager();
        return employeesManager;
    }

    public void PersonnelManager(Employee login) {
        if (login == null || !(login.getJobType()).equals(JobType.PersonalManager))
            throw new IllegalArgumentException("Only the Personnel Manager can do this action.");
    }

   public void TransportManager(Employee login) {
        if (login == null || !(login.getJobType()).equals(JobType.TransportManager))
            throw new IllegalArgumentException("Only the transport manager can do this action.");
    }

    //all employee actions

    //add constraint
    public void addConstraint(LocalDate date, ShiftType shift, Employee login) {
        login.addConstraint(date, shift);
        employeeConstraintsDAO.InsertEmployeeConstraint(login.getId(), shift.toString(), date.format(formatters));
    }

    //remove constraint
    public void deleteConstraint(LocalDate date, ShiftType shift, Employee login) {
        login.deleteConstraint(date, shift);
        employeeConstraintsDAO.DeleteEmployeeConstraint(login.getId(), shift.toString(), date.format(formatters));
    }

    //watch my constraint
    public Map<LocalDate, List<ShiftType>> getConstraint(Employee login) {
        return login.getConstraint();
    }

    //watch my shifts
    public String getShift(Employee login) {
        return login.getShift();
    }

    public Map<LocalDate,List<ShiftType>> getShiftsDate(Employee login) {
        return login.getShiftsDate();
    }

    //personalManager
    //get all the employees
    //add employee
    public void addEmployee(int id, String name, int bankAccount, int salary, String hiringCondition, String jobType, String licence) {
        CheckIfEmployeeAlreadyExist(id);
        add(id, name, bankAccount, salary, hiringCondition, jobType, licence);
    }

    public void CheckIfEmployeeAlreadyExist(int id) {
        if (employeeDAO.ExistEmployee(id))
            throw new IllegalArgumentException("Employee "+id+" already exist");
    }

    private void add(int id, String name, int bankAccount, int salary, String hiringCondition, String jobType, String licence) {
        if (jobType.equals("Driver")) {
            Driver driver = new Driver(id, name, bankAccount, salary, hiringCondition, jobType, licence);
            employeeDAO.Insert(driver);
            driverLicenseDAO.Insert(driver);
            driverDAO.Insert(driver);
        } else {
            Employee employee = new Employee(id, name, bankAccount, salary, hiringCondition, jobType);
            employeeDAO.Insert(employee);
        }
    }

    public Employee getEmployee(int id) {
        boolean fromdb=true;
        if(employeeDAO.inCache(id))
            fromdb=false;
        Employee employee = employeeDAO.getEmployee(id);
        if(employee.getJobType().toString().equals("Driver"))
            return getDriver(id);
        if(fromdb)
            loadEmployee(employee);
        return employee;
    }
    public String getJobTypeByID(int id) {
        return getEmployee(id).getJobType().toString();
    }
    public void loadEmployee(Employee employee) {
        employee.setTrainings(employeeTrainingDAO.getEmployeeTraining(employee.getId()));
        employee.setConstraint(employeeConstraintsDAO.getEmployeeConstraintsById(employee.getId()));
        employee.setShifts(employeeShiftDAO.getEmployeeShiftsById(employee.getId()));
    }

    public Driver getDriver(int id) {
        Employee employee = employeeDAO.getEmployee(id);
        Driver driver = new Driver(employee.getId(), employee.getName(), employee.getBankAccount(), employee.getSalary(), employee.getHiringCondition(), employee.getStartOfEmployment(), employee.getFinishWorking(), employee.getJobType().toString());
        loadEmployee(driver);
        //driver loading
        driver.setLicenses(driverLicenseDAO.selectDriverLicense(id));
        driver.setDistance(driverDAO.selectDriverDistance(id));
        driver.setTransports(TransportManager.getInstance().getDriverTransports(id));
        return driver;
    }

    public List<Employee> getEmployess() {
        List<Employee> employeesAndDriversNotLoaded = employeeDAO.SelectAllEmployees();
        List<Employee> employeeList = new LinkedList<>();
        for (Employee employee : employeesAndDriversNotLoaded) {
            if (employee.getJobType().equals("Driver")) {
                employeeList.add(getDriver(employee.getId()));
            } else {
                loadEmployee(employee);
                employeeList.add(employee);
            }
        }
        return employeeList;
    }

    public List<Driver> getDrivers() {
        List<Employee> employeesAndDriversNotLoaded = employeeDAO.SelectAllEmployees();
        List<Driver> DriversList = new LinkedList<>();
        for (Employee employee : employeesAndDriversNotLoaded) {
            if (employee.getJobType().toString().equals("Driver")) {
                DriversList.add(getDriver(employee.getId()));
            }
        }
        return DriversList;
    }

    public String getDetails(int id) {
        Employee e = getEmployee(id);
        return e.getDetails();
    }

    //update employee status to finish work
    public void finish(int id) {
        Employee e = getEmployee(id);
        e.setFinishWorking();
        employeeDAO.setFinishWorking(id, true);
    }

    public void updatebank(int id, int bank) {
        Employee e = getEmployee(id);
        e.setBankAccount(bank);
        employeeDAO.setBankAccount(id, bank);
    }

    public void updateSalary(int id, int salary) {
        Employee e = getEmployee(id);
        e.setSalary(salary);
        employeeDAO.setSalary(id, salary);
    }

    public void updateJob(int id, String jobType) {
        Employee e = getEmployee(id);
        e.setJobType(jobType(jobType));
        employeeDAO.setJobType(id, jobType);
    }

    public void updateHiring(int id, String hiring) {
        Employee e = getEmployee(id);
        e.setHiringCondition(hiring);
        employeeDAO.setHiringConditions(id, hiring);
    }

    public void addTrain(int id, String training) {
        Employee e = getEmployee(id);
        e.setTraining(training);
        employeeTrainingDAO.InsertEmployeeTraining(id, training);
    }

    public void updatedriveravgdistance(Driver d) {
        driverDAO.setDriverDistance(d.getId(), d.getDistance());
    }

    public List<Driver> getAvailableDriver(LocalDateTime dateTime) {
        List<Driver> available = new ArrayList<>();
        List<Driver> drivers = getDrivers();
        for (Driver driver : drivers) {
              if (driver.checkIfTheDriverIsAvailable(dateTime)) {
                    available.add(driver);
              }
        }
        return available;
    }
    public List<Driver> getAvailableDriverForTheMorningAndTheEvening(LocalDateTime dateTime1 , LocalDateTime dateTime2) {
        List<Driver> available = new ArrayList<>();
        List<Driver> drivers = getDrivers();
        for (Driver driver : drivers) {
            if (driver.checkIfTheDriverIsAvailable(dateTime1) || driver.checkIfTheDriverIsAvailable(dateTime2)) {
                available.add(driver);
            }
        }
        return available;
    }


    //watch all the employees that available for the shift
    public Map<JobType, List<Employee>> getAvailableEmployees(LocalDate date, ShiftType shiftType) {
        List<Employee> employeeList = getEmployess();
        Map<JobType, List<Employee>> available = new HashMap<>();
        for (Employee e : employeeList) {
            if ((!e.getConstraint().containsKey(date))||!e.getConstraint().get(date).contains(shiftType))
            {
                if (!available.containsKey(e.getJobType()))
                    available.put(e.getJobType(), new LinkedList<>());
                available.get(e.getJobType()).add(e);
            }
        }
        return available;
    }



    public void switchManager(int iddelete, int idadd, LocalDate date, ShiftType shiftType) {
        Employee delete = getEmployee(iddelete);
        Employee add = getEmployee(idadd);
        if (!add.shiftManager())
            throw new IllegalArgumentException(add + "cant be shift Manager");
        delete.deleteShift(date, shiftType);
        add.addShift(date, shiftType);
    }

    private void legalSwitch(Employee delete, Employee add, LocalDate date, ShiftType shiftType) {
        if (date.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("this date already pass");
        if (add == null || delete == null)
            throw new IllegalArgumentException("employee not exist");
        if (!delete.inShift(date, shiftType))
            throw new IllegalArgumentException(delete + "employee is not exist in the shift");
        if (!add.Isavailable(date, shiftType))
            throw new IllegalArgumentException(add + "employee is not available for the shift");
    }

    public void switchEmployess(int iddelete, int idadd, LocalDate date, ShiftType shiftType) {
        Employee delete = getEmployee(iddelete);
        Employee add = getEmployee(idadd);
        legalSwitch(delete, add, date, shiftType);
        if (delete.getJobType() != add.getJobType())
            throw new IllegalArgumentException(add.getId() + " " + add.getName() + " cant be " + delete.getJobType());
        delete.deleteShift(date, shiftType);
        add.addShift(date, shiftType);

    }

    public JobType deleteShift(int iddelete, LocalDate date, ShiftType shiftType) {
        if (date.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("this date already pass");
        Employee delete = getEmployee(iddelete);
        if (delete == null)
            throw new IllegalArgumentException("employee is not exist");
        if (!delete.inShift(date, shiftType))
            throw new IllegalArgumentException(delete + "employee is not exist in the shift");
        delete.deleteShift(date, shiftType);
        return delete.jobType;
    }

    public JobType addEmployeetoShift(int idadd, LocalDate date, ShiftType shiftType) {
        LocalDate today = LocalDate.now();
        LocalDate yeasterday = today.minusDays(1);
        if (date.isBefore(yeasterday))
            throw new IllegalArgumentException("this date already pass");
        Employee add = getEmployee(idadd);
        if (add == null)
            throw new IllegalArgumentException("employee not exist");
        if (!add.Isavailable(date, shiftType))
            throw new IllegalArgumentException(add + " employee is not available for the shift");
        add.addShift(date, shiftType);
        return add.getJobType();
    }


    public void addShifts(LocalDate date, ShiftType shiftType, Map<JobType, List<Employee>> possibleShift) {
        for (JobType j : possibleShift.keySet()) {
            for (Employee e : possibleShift.get(j)) {
                e.addShift(date, shiftType);
            }
        }
    }

    public void deleteArrange(LocalDate date, ShiftType shiftType, Integer shiftManager, Map<JobType, List<Employee>> employees) {
        for (JobType j : employees.keySet()) {
            for (Employee e : employees.get(j)) {
                e.deleteShift(date, shiftType);
            }
        }
        getEmployee(shiftManager).deleteShift(date, shiftType);
    }

    public void shiftManager(LocalDate date, ShiftType shiftType, int id) {
        Employee e = getEmployee(id);
        if (!e.Isavailable(date, shiftType))
            throw new IllegalArgumentException(e + " is not available for this shift");
        if (!e.shiftManager())
            throw new IllegalArgumentException(e + " is not with quality of shift manager");
    }
    public List<Message> getUnreadMessages(String jobType) {
        List<Message> Messages =  messageDAO.SelectAllMessages();
        List<Message> unReadMessages = new LinkedList<>();
        for(Message mes : Messages) {
            if ((!mes.isRead())&&(mes.getJob().equals((jobType)))) {
                unReadMessages.add(mes);
                updateMessageIsRead(mes.getIdMessage());
            }
        }
        return unReadMessages;
    }

    public void addMessage(String jobType, String message, boolean read) {
        messageDAO.Insert(new Message(jobType,message,read));

    }

    public Map<JobType, List<Employee>> shiftLegal(LocalDate date, ShiftType shiftType, Map<JobType, LinkedList<Integer>> possibleShifts) {
        Map<JobType, List<Employee>> shift = new HashMap<>();
        for (JobType j : possibleShifts.keySet()) {
            shift.put(j, new LinkedList<>());
            for (Integer e : possibleShifts.get(j)) {
                Employee employee = getEmployee(e);
                if (employee == null)
                    throw new IllegalArgumentException(e + "is not exist employee");
                if (!employee.getJobType().equals(j))
                    throw new IllegalArgumentException("job " + j + " didn't fit for " + e);
                if (!employee.Isavailable(date, shiftType))
                    throw new IllegalArgumentException(e + " is not available for this shift");
                if (shift.get(j).contains(employee))
                    throw new IllegalArgumentException(e + " is Assigned twice to the shift");
                shift.get(j).add(employee);
            }
        }
        return shift;
    }

    public List<JobType> getJobTypeList(ShiftType shiftType) {
        if ((shiftType.equals(ShiftType.Evening)))
            return getJobslistWithOutManagers();
        return Arrays.asList(JobType.values());
    }

    private List<JobType> getJobslistWithOutManagers() {
        LinkedList<JobType> jobTypes = new LinkedList<>();
        for (JobType j : JobType.values()) {
            if (!j.toString().contains("Manager"))
                jobTypes.add(j);
        }
        return jobTypes;
    }
    public void updateMessageIsRead(int idMessage) {
        messageDAO.updateMessageIsRead(idMessage);
    }

    public boolean isDriver(String jobType) {
        return jobType(jobType).equals(JobType.Driver);
    }

    public JobType jobType(String jobType) {
        switch (jobType) {
            case "Driver":
                return JobType.Driver;
            case "Cashier":
                return JobType.Cashier;
            case "PersonalManager":
                return JobType.PersonalManager;
            case "PurchasingManager":
                return JobType.PurchasingManager;
            case "Storekeeper":
                return JobType.Storekeeper;
            case "Usher":
                return JobType.Usher;
            case "StoreManager":
                return JobType.StoreManager;
            case "TransportManager":
                return JobType.TransportManager;
            default:
                throw new IllegalArgumentException("the job does not exist");
        }
    }

    public void loadData() throws ParseException {        //addEmployee
        addEmployee(208687319, "lior levy", 123456789, 9999, "normal hiringCondition", "Usher", "");
        addEmployee(201202020,"Dina Apagov",12345677,9999,"normal hiring","StoreManager","");
        addEmployee(202876546, "pedro fridman", 113459, 9999, "normal hiring condition", "Cashier", "");
        addEmployee(209876546, "noam levy", 123459, 9999, "normal hiring condition", "Usher", "");
        addEmployee(207687849, "Noga Schwartz", 123458, 9999, "normal hiring condition", "PersonalManager", "");
        addEmployee(209876543, "ido doron", 123456, 9999, "normal hiring condition", "Cashier", "");
        addEmployee(209876544, "Tom Shainman", 123457, 9999, "normal hiring condition", "Storekeeper", "");
        addEmployee(209876545, "Shay Kresner", 127477, 9999, "normal hiring condition", "TransportManager", "");
        addEmployee(209879574, "aviv guy", 127477, 9999, "normal hiring condition", "Driver", "C1 C+E");
        addEmployee(209120278, "Mor Shuker", 127477, 9999, "normal hiring condition", "Driver", "C C+E");
        addEmployee(313882884, "Guy Barak", 127477, 9999, "normal hiring condition", "Driver", "C+E C1 B");
        addEmployee(208649319, "Ben Alvo", 127477, 9999, "normal hiring condition", "Driver", "C C+E B");
        addEmployee(206889543, "Nadav gigi", 127477, 9999, "normal hiring condition", "Driver", "C+E C");
        addEmployee(313883254, "Regev Alon", 127477, 9999, "normal hiring condition", "Driver", "B");
        addEmployee(206014839, "Alon Itzhaki", 127477, 9999, "normal hiring condition", "PurchasingManager", "");

        LocalDate date1 = LocalDate.of(2022, 5, 27);
        LocalDate date2 = LocalDate.of(2022, 5, 28);

//        addMessage("PersonalManager","arrange the drivers shifts 25/06/2022",false);
//        addMessage("PersonalManager","arrange the drivers shifts 30/06/2022",false);

    }


    public void DelData() {
        driverDAO.Delete();
        driverLicenseDAO.Delete();
        employeeTrainingDAO.resetCache();
        employeeTrainingDAO.Delete();
        employeeConstraintsDAO.resetCache();
        employeeConstraintsDAO.Delete();
        employeeShiftDAO.resetCache();
        employeeShiftDAO.Delete();
        employeeDAO.resetCache();
        employeeDAO.Delete();
        messageDAO.Delete();
    }



}

