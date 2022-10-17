package BussinessLayer.Managers;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


import BussinessLayer.Objects.JobType;
import BussinessLayer.Objects.Shift;
import BussinessLayer.Objects.ShiftType;
import DataAccessLayer.ShiftDAO;
import  DataAccessLayer.ShiftStructureDAO;
import DataAccessLayer.EmployeeShiftDAO;

public class ShiftsManager {
    private static ShiftsManager shiftsManager = null;
    private final ShiftStructureDAO shiftStructureDAO;
    private final EmployeeShiftDAO employeeShiftDAO;
    private ShiftDAO shiftDAO;

    DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static ShiftsManager getInstance() {
        if (shiftsManager == null){
            shiftsManager = new ShiftsManager();
        }
        return shiftsManager;
    }

    private ShiftsManager() {
        shiftStructureDAO= new ShiftStructureDAO();
        employeeShiftDAO =new EmployeeShiftDAO();
        shiftDAO = new ShiftDAO();
    }

    //add things
    public void addShift(LocalDate date, ShiftType shiftType, Map<JobType, Integer> shiftStructure) {
        existShift(date, shiftType);
        Shift shift = new Shift(date, shiftType, shiftStructure);
        shiftDAO.Insert(shift);
        for (JobType job : shiftStructure.keySet()) {
            shiftStructureDAO.Insert(shiftType.toString(), date.format(formatters), job.toString(), shiftStructure.get(job));
        }
    }

    //add shift with exist structure
    public void addShift(LocalDate date, LocalDate date1, ShiftType shiftType) {
        existShift(date, shiftType);
        Shift oldshift=getShift(date1, shiftType);
        if (oldshift==null)
            throw new IllegalArgumentException("the shift doesn't exist");
        Map<JobType, Integer> shiftStructureNew=oldshift.getshiftStructure();
        Shift shift=new Shift(date,shiftType,shiftStructureNew);
        shiftDAO.Insert(shift);
        for (JobType job:shiftStructureNew.keySet()) {
            shiftStructureDAO.Insert(shiftType.toString(),date.format(formatters),job.toString(),shiftStructureNew.get(job));
        }
    }

    //set and update func
    public void setShiftList(Integer e, Map<JobType, LinkedList<Integer>> employees, LocalDate date, ShiftType shiftType) {
        Shift shift= getShift(date,shiftType);
        notExistShift(shift);
        if(date.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("this date already pass");
        shift.setEmployeeList(e, employees);
        for (JobType j:employees.keySet()) {
            for (Integer i:employees.get(j)) {
                employeeShiftDAO.InsertOne(i ,shiftType.toString(), date.format(formatters));
            }
        }
        shiftDAO.SetIDmanager(e,shiftType.toString(),date.format(formatters));
    }

    public void updateJobInShift(LocalDate date, ShiftType shiftType, JobType jobType, int num) {
        Shift shift=getShift(date,shiftType);
        notExistShift(shift);
        if(date.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("this date already pass");
        shift.set1JobInShiftStructure(jobType,num);
        shiftStructureDAO.setNumOfEmployeeColumnName(shiftType.toString(),date.format(formatters),jobType.toString(),num);
    }

    public void deleteArrange(LocalDate date, ShiftType shiftType) {
        Shift shift = getShift(date, shiftType);
        notExistShift(shift);
        if(date.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("this date already pass");
        shift.deleteArrangment();
        shiftDAO.SetIDmanager(0,shiftType.toString(),date.format(formatters));
        employeeShiftDAO.DeleteShift(shiftType.toString(),date.format(formatters));
    }


    public List<Shift> shiftbeetween(LocalDate start, LocalDate end) {
        List<Shift> select = shiftDAO.SelectAllShifts();
        List<Shift> history =new LinkedList<>();
        for (Shift shift : select) {
            if (shift.getDate().isBefore(end) && shift.getDate().isAfter(start)) {
                Shift s=getShift(shift.getDate(), shift.getShiftType());
                history.add(s);
            }
        }
        return history;
    }

    public int getManager(LocalDate date, ShiftType shiftType) {
        notExistShift(getShift(date,shiftType));
        return shiftDAO.getShiftManagerIdByShift(shiftType.toString(),date.format(formatters));
    }

    public void updateshift(LocalDate date, ShiftType shiftType, Map<JobType, Integer> structure) {
        Shift shift=getShift(date, shiftType);
        notExistShift(shift);
        if(date.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("this date already pass");
        shift.setShiftStructure(structure);
        shiftStructureDAO.Delete(shiftType.toString(),date.format(formatters));//EXTRA
        for (JobType job:structure.keySet()) {
            shiftStructureDAO.Insert(shiftType.toString(),date.format(formatters),job.toString(),structure.get(job));
        }
    }

    public void switchEmployess(Integer employeeDelete, Integer employeeAdd, LocalDate date, ShiftType shiftType) {
        Shift shift= getShift(date, shiftType);
        notExistShift(shift);
        shift.switchEmployee(employeeDelete, employeeAdd);
        employeeShiftDAO.DeleteOne(employeeDelete, shiftType.toString(), date.format(formatters));
        employeeShiftDAO.InsertOne(employeeAdd, shiftType.toString(), date.format(formatters));
    }

    public void switchManager(Integer employeeAdd, LocalDate date, ShiftType shiftType) {
        Shift shift= getShift(date, shiftType);
        notExistShift(shift);
        shift.setManager(employeeAdd);
        shiftDAO.SetIDmanager(employeeAdd,shiftType.toString(),date.format(formatters));
    }

    public void deleteEmployee(int deleteEmployee, LocalDate date, ShiftType shiftType,JobType jobType) {
        Shift shift=getShift(date, shiftType);
        notExistShift(shift);
        shift.deleteEmployee(deleteEmployee,jobType);
        shiftStructureDAO.setNumOfEmployeeColumnName(shiftType.toString(),date.format(formatters),jobType.toString(),shift.getshiftStructure().get(jobType));
        employeeShiftDAO.DeleteOne(deleteEmployee, shiftType.toString(), date.format(formatters));
    }

    public void addEmployee(int addEmployee, JobType job, LocalDate date, ShiftType shiftType) {
        Shift shift=getShift(date, shiftType);
        notExistShift(shift);
        shift.AddEmployee(addEmployee, job);
        shiftStructureDAO.setNumOfEmployeeColumnName(shiftType.toString(),date.format(formatters),job.toString(),shift.getshiftStructure().get(job));
        employeeShiftDAO.InsertOne(addEmployee, shiftType.toString(), date.format(formatters));
    }

    //help func
    private void existShift(LocalDate date, ShiftType shiftType) {
        if (getShift(date,shiftType) != null)
            throw new IllegalArgumentException("the shift in date "+date+" in the "+shiftType+" already exist.");
    }
    private void notExistShift(Shift shift) {
        if (shift== null)
            throw new IllegalArgumentException("the shift not exist.");
    }


    private Shift getShift(LocalDate date, ShiftType shiftType) {
        Shift shift = shiftDAO.getShift(date, shiftType);
        if (shift != null) {
            shift.setShiftStructureString(shiftStructureDAO.getShiftStructure(date, shiftType));
            shift.setEmployeeList(employeeShiftDAO.getEmployeeIDInShift(shiftType.toString(),date.format(formatters)));
            shift.setManager(shiftDAO.getShiftManagerIdByShift(shiftType.toString(),date.format(formatters)));
            return shift;
        }
        return null;
    }


    public void loadData(){
        //date
        LocalDate today= LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        LocalDate date3 = today.plusDays(10);
        LocalDate date4 =today.plusDays(9);
        LocalDate date5 = today.plusDays(6);




        //put jobType
        Map<JobType, Integer> shiftStructure1 = new HashMap<>();
        shiftStructure1.put(JobType.Usher, 1);
        shiftStructure1.put(JobType.Cashier, 1);
        shiftStructure1.put(JobType.Storekeeper, 1);
        shiftStructure1.put(JobType.Driver, 3);

        //insert Shifts
        addShift(date4,ShiftType.Evening,shiftStructure1);
        addShift(date3,date4,ShiftType.Evening);
        addShift(date5,date4,ShiftType.Evening);
        addShift(today,date4,ShiftType.Evening);
        addShift(tomorrow,date4,ShiftType.Evening);

    }

    public void DelData() {
        shiftStructureDAO.resetCache();
        shiftStructureDAO.Delete();
        shiftDAO.resetCache();
        shiftDAO.Delete();
    }
}

