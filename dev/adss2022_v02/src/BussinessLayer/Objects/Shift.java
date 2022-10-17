package BussinessLayer.Objects;


import java.time.LocalDate;
import java.util.*;

public class Shift {

    public LocalDate date;
    public ShiftType shiftType;
    public Map<JobType, Integer> shiftStructure;
    //int=employee ids
    private List<Integer> employeesID;
    private int shiftManagerID;


    public Shift(LocalDate date, ShiftType shiftType, Map<JobType, Integer> shiftStructure){
        if(LocalDate.now().isAfter(date))
            throw new IllegalArgumentException("this date already pass");
        LegalStructure(shiftStructure);
        this.date=date;
        this.shiftType=shiftType;
        this.shiftStructure=new HashMap<>();
        for (JobType jobType:shiftStructure.keySet())
            this.shiftStructure.put(jobType,shiftStructure.get(jobType));
        employeesID=new LinkedList<>();
//        for (JobType j:shiftStructure.keySet()) {
//            employeesID.put(j,new LinkedList<>());
//        }
        shiftManagerID=0;
    }

    //to db
    public Shift(LocalDate date, String shiftType,int shiftManagerID){
        this.date=date;
        if(shiftType.equals("Morning"))
            this.shiftType=ShiftType.Morning;
        else
            this.shiftType=ShiftType.Evening;
        this.shiftStructure=new HashMap<>();
        this.employeesID=new LinkedList<>();
        this.shiftManagerID=shiftManagerID;
    }

    public ShiftType getShiftType() {
        return shiftType;
    }

    public int getManager() {
        return shiftManagerID;
    }

    private void LegalStructure(Map<JobType, Integer> shiftStructure1){
        for (JobType j:shiftStructure1.keySet()) {
            if(shiftStructure1.get(j)<1)
                throw new IllegalArgumentException(j.toString()+" with under 1 employess");
        }
    }

    public void legalArrangment(Map<JobType, Integer> arrangment){
        for (JobType j:shiftStructure.keySet()) {
            if(!shiftStructure.get(j).equals(arrangment.get(j)))
                throw new IllegalArgumentException("You have "+arrangment.get(j)+" employees in "+j+" but you need"+shiftStructure.get(j));
        }
    }

    public void deleteArrangment(){
        if(LocalDate.now().isAfter(date))
            throw new IllegalArgumentException("this date already pass");
        if(shiftManagerID==0)
            throw new IllegalArgumentException("you don't have arrangement of employees");
        this.shiftManagerID=0;
        employeesID=new LinkedList<>();
    }

    public void setShiftStructure(Map<JobType, Integer> shiftStructure1){
        LegalStructure(shiftStructure1);
        emptyEmployee();
        shiftStructure=shiftStructure1;
    }

    //for db
    public void setShiftStructureString(Map<String, Integer> shiftStructure1){
        for (String s:shiftStructure1.keySet()) {
            shiftStructure.put(convertJobTypeToEnum(s),shiftStructure1.get(s));
        }
    }

    private void emptyEmployee(){
        if(!employeesID.isEmpty())
            throw new IllegalArgumentException("you have to erase employee arrangement before you change shift structure");
    }

    public void set1JobInShiftStructure(JobType j,int quantity){
        emptyEmployee();
        set1Job(j,quantity);
    }

    private void set1Job(JobType j,int quantity){
        if(!shiftStructure.containsKey(j))
            throw new IllegalArgumentException(j+" not exist in this shift");
        if(quantity<=0)
            throw new IllegalArgumentException("you cant change quantity to less than 1 employee");
        shiftStructure.put(j,quantity);
    }
    public void setManager(Integer employeeAdd){
        shiftManagerID=employeeAdd;
    }

    public void switchEmployee(Integer employeeDelete,Integer employeeAdd) {
        if (employeesID.contains(employeeDelete)) {
            employeesID.remove(employeeDelete);
            employeesID.add(employeeAdd);
        }
    }

    public Map<JobType, Integer> getshiftStructure() {
        return shiftStructure;
    }

    public void setEmployeeList(Integer shiftMannager,Map<JobType, LinkedList<Integer>> arrangment) {
        if(shiftManagerID!=0)
            throw new IllegalArgumentException("you have already arrangement of the employees");
        if(shiftMannager==0)
            throw new IllegalArgumentException("you have to put shift manager");
        Map<JobType,Integer> listSize=new HashMap<>();
        for (JobType j:arrangment.keySet()) {
            listSize.put(j,arrangment.get(j).size());
        }
        legalArrangment(listSize);
        this.shiftManagerID=shiftMannager;
        for (JobType j:arrangment.keySet()) {
            this.employeesID.addAll(arrangment.get(j));
        }
    }
    public void setEmployeeList(List<Integer> arrangment) {
        this.employeesID= arrangment;
    }


    public void deleteEmployee(Integer deleteEmployee,JobType jobType) {
        if (shiftManagerID == 0)
            throw new IllegalArgumentException("you don't have arrangement of employees");
        set1Job(jobType,shiftStructure.get(jobType)-1);
        employeesID.remove(deleteEmployee);
    }

    public void AddEmployee(Integer addEmployee,JobType jobType) {
        if(shiftManagerID==0)
            throw new IllegalArgumentException("you don't have arrangement of employees");
        set1Job(jobType,shiftStructure.get(jobType)+1);
        employeesID.add(addEmployee);

    }

    @Override
    public String toString(){
        StringBuilder s;
        s = new StringBuilder(date + " " + shiftType + "\n" + "shift Structure: " + "\n");
        for (JobType j:shiftStructure.keySet()) {
            s.append(j).append(": ").append(shiftStructure.get(j)).append("\n");
        }
        if(employeesID.size()==0)
            s.append("haven't assigned the employees yet");
        else {
            if(shiftManagerID!=0) {
                s.append("\nShift Manager:").append(shiftManagerID).append("\n");
                s.append("employees:");
                for (int i=0;i<employeesID.size();i++) {
                    s.append(employeesID.get(i)).append("\n");
                }
            }
        }
        return s.toString();
    }


    public String getStringShiftType(){
        return shiftType.toString();
    }

    public Map<String, Integer> getMapShiftStructure(){
        Map<String, Integer> copyShiftStructure = new HashMap<>();
        for (JobType jobType : shiftStructure.keySet()) {
            copyShiftStructure.put(jobType.toString(),shiftStructure.get(jobType));
        }

        return copyShiftStructure;
    }


    public List<Integer> getEmployees(){
        return employeesID;
    }

    public LocalDate getDate() {
        return date;
    }

    public JobType convertJobTypeToEnum(String jobType) {
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
            case "TransportManager":
                return JobType.TransportManager;
            default:
                throw new IllegalArgumentException("the job does not exist");
        }
    }
}
