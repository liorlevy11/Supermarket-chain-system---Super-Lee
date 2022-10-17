package ServiceLayer.ServiceObjects;
import BussinessLayer.Objects.Employee;

import java.time.LocalDate;
import java.util.*;


public class EmployeeS {
    private int id;
    private  String name;
    private int bankAccount;
    private int salary;
    private String hiringCondition;
    private LocalDate startOfEmployment;
    private Map<LocalDate, List<String>> constraint;
    private Map<LocalDate, List<String>> shifts;
    private boolean finishWorking;
    private String jobType;
    private  String training;



    public EmployeeS(int id,String name, int bankAccount, int salary, String hiringCondition, String jobType){
        this.id=id;
        this.name=name;
        this.bankAccount=bankAccount;
        this.salary=salary;
        this.hiringCondition=hiringCondition;
        this.startOfEmployment= LocalDate.now();
        this.constraint=new HashMap<>();
        this.shifts=new HashMap<>();
        this.finishWorking=false;
        this.jobType=jobType;
        this.training="";
    }

    public EmployeeS(Employee employee) {
        this.id = employee.getId();
        this.name = employee.getName();
        this.bankAccount = employee.getBankAccount();
        this.salary = employee.getSalary();
        this.hiringCondition = employee.getHiringCondition();
        this.startOfEmployment = employee.startOfEmployment;
        this.constraint = employee.getMapConstraint();
        this.shifts = employee.getStringShifts();
        this.finishWorking = employee.getFinishWorking();
        this.jobType = employee.getJobType().toString();
        this.training = employee.getStringTraining();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    //private String licenses;

//    public DriverS(int idDriver, String name, String licenses) {
//        this.idDriver = idDriver;
//        this.name = name;
//        this.licenses = licenses;
//    }


    // private  List<Employee.Training> training;
//
//    public List<Employee.Training> copyTraining(List<Employee.Training> training){
//        LinkedList<Employee.Training> copyTraining=new LinkedList<>();
//        for(Employee.Training train :training){
//            copyTraining.add(train);
//        }
//        return copyTraining;
//    }
    @Override
    public String toString() {
        String s="ID: "+id+" | NAME: "+name+" | INFO: ";
        if(finishWorking)
            s=s+" | FINISH TO WORK";
        else {
            s = s + " | JOB TYPE: " + jobType;
            if (training.contains("CancellationCard")&&training.contains("TeamManagement"))
                s = s + " | CAN BE SHIFT MANAGER";
        }
        return s;
    }
}
