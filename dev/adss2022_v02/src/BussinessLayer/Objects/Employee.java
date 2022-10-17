package BussinessLayer.Objects;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Employee {




    public enum Training {
        TeamManagement,
        CancellationCard
    }

    protected final int id;
    public String name;
    protected int bankAccount;
    public int salary;
    public String hiringCondition;
    public LocalDate startOfEmployment;
    public Map<LocalDate,List<ShiftType>> constraint;
    public Map<LocalDate, List<ShiftType>> shifts;
    protected boolean finishWorking;
    public JobType jobType;
    protected final List<Training> training;
    protected List<Message> messages;

    public Employee(int id,String name, int bankAccount, int salary, String hiringCondition, String jobType){
        this.id=id;
        this.name=name;
        this.bankAccount=bankAccount;
        this.salary=salary;
        this.hiringCondition=hiringCondition;
        this.startOfEmployment= LocalDate.now();
        this.constraint=new HashMap<>();
        this.shifts=new HashMap<>();
        this.finishWorking=false;
        this.jobType=convertJobTypeToEnum(jobType);
        this.training=new LinkedList<>();
        messages = new LinkedList<>();
    }


    public Employee(int id,String name, int bankAccount, int salary, String hiringCondition,LocalDate startOfEmployment,Boolean finishWorking, String jobType){
        this.id=id;
        this.name=name;
        this.bankAccount=bankAccount;
        this.salary=salary;
        this.hiringCondition=hiringCondition;
        this.startOfEmployment= startOfEmployment;
        this.constraint=new HashMap<>();
        this.shifts=new HashMap<>();
        this.finishWorking=finishWorking;
        this.jobType=convertJobTypeToEnum(jobType);
        this.training=new LinkedList<>();
    }

    public List<Message> getMessages() {
        return messages;
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
            case "StoreManager":
                return JobType.StoreManager;
            case "Usher":
                return JobType.Usher;
            case "TransportManager":
                return JobType.TransportManager;
            default:
                throw new IllegalArgumentException("the job "+jobType+" does not exist");
        }
    }

    public LocalDate getStartOfEmployment() {
        return startOfEmployment;
    }

    public void addConstraint(LocalDate date,ShiftType shiftType){
        if(!((beforeFriday()&&afterSaturday(date,0))||(afterSaturday(date,1)))){
            throw new IllegalArgumentException("Constraints can be submitted for shift in next week");
        }
        if(constraint.containsKey(date)&&constraint.get(date).contains(shiftType))
            throw new IllegalArgumentException("Constraints already exist");
        if(shifts.containsKey(date)&&shifts.get(date).contains(shiftType))
            throw new IllegalArgumentException("you have shift in this date");
        if(!constraint.containsKey(date)) {
            List<ShiftType> l = new LinkedList<>();
            constraint.put(date, l);
        }
        constraint.get(date).add(shiftType);
    }
    public void deleteConstraint(LocalDate date,ShiftType shiftType){
        if(!(constraint.containsKey(date)&&constraint.get(date).contains(shiftType)))
            throw new IllegalArgumentException("Constraint does not exist");
        constraint.get(date).remove(shiftType);
        if (constraint.get(date).isEmpty())
            constraint.remove(date);
    }

    public void addShift(LocalDate date, ShiftType shiftType){
        if(shifts.containsKey(date))
            shifts.get(date).add(shiftType);
        else {
            List<ShiftType> l=new LinkedList<>();
            shifts.put(date,l);
            shifts.get(date).add(shiftType);
        }
    }

    public void deleteShift(LocalDate date, ShiftType shiftType){
        if(shifts.containsKey(date)) {
            shifts.get(date).remove(shiftType);
            if (shifts.get(date).isEmpty())
                shifts.remove(date);
        }
    }

    public boolean Isavailable(LocalDate date, ShiftType time){
        if(finishWorking)
            return false;
        if(constraint.containsKey(date))
            if (constraint.get(date).contains(time))
                return false;
        return !shifts.containsKey(date) || (!shifts.get(date).contains(time));
    }

//    public Map<Date,List<ShiftType>> shiftFrom(Date date){
//        Map<Date,List<ShiftType>> shiftFromDate=new HashMap<>();
//        for (Date d:shifts.keySet()) {
//            if (d.after(date))
//                shiftFromDate.put(d,shifts.get(d));
//        }
//        return shiftFromDate;
//    }

    public void setFinishWorking() {
        if(finishWorking)
            throw new IllegalArgumentException("the employee is finish to work already");
        this.finishWorking = true;
    }

    public int getId() {
        return id;
    }

    public JobType getJobType() {
        return jobType;
    }

    public Map<LocalDate,List<ShiftType>> getConstraint() { return constraint; }

    public Map<LocalDate, List<ShiftType>> getShiftsDate(){return shifts;}

    public String getShift() {
        String s="MY SHIFTS: \n";
        for (LocalDate d:shifts.keySet()) {
            for (ShiftType shiftType:shifts.get(d)) {
                s=s+d+" "+shiftType+"\n";
            }
        }
        return s;
    }

    public boolean inShift(LocalDate date, ShiftType shiftType){
        return (shifts.containsKey(date)&&shifts.get(date).contains(shiftType));
    }

    public void setHiringCondition(String hiringCondition) {
        if(finishWorking)
            throw new IllegalArgumentException("the employee is finish to work");
        this.hiringCondition = hiringCondition;
    }


    public void setBankAccount(int bankAccount) {
        if(finishWorking)
            throw new IllegalArgumentException("the employee is finish to work");
        this.bankAccount = bankAccount;
    }


    public void setSalary(int salary) {
        if (finishWorking)
            throw new IllegalArgumentException("the employee is finish to work");
        this.salary = salary;
    }

//    public int getBankAccount() {
//        return bankAccount;
//    }

//    public int getSalary() {
//        return salary;
//    }


    public void setJobType(JobType jobType) {
        if(finishWorking)
            throw new IllegalArgumentException("the employee is finish to work");
        this.jobType = jobType;
    }

    public void setTraining(String training1) {
        if(finishWorking)
            throw new IllegalArgumentException("the employee is finish to work");
        if(training1.equals("CancellationCard")) {
            if (training.contains(Training.CancellationCard))
                throw new IllegalArgumentException("Employee " + id + " already has Cancellation Card train");
            else
                training.add(Training.CancellationCard);
        }
        else if(training1.equals("TeamManagement")) {
            if (training.contains(Training.TeamManagement))
                throw new IllegalArgumentException("Employee " + id + " already has Team Management train");
            else
                training.add(Training.TeamManagement);
        }
        else
            throw new IllegalArgumentException("training should be TeamManagement|CancellationCard");
    }

    public void setTrainings(List<String> list){
        for(String s :list){
            if(s.equals("CancellationCard"))
                training.add(Training.CancellationCard);
            else if(s.equals("TeamManagement"))
                training.add(Training.TeamManagement);
        }
    }

    private boolean beforeFriday(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_YEAR, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        return (cal.getTime().after(new Date()));
    }

    private boolean afterSaturday(LocalDate date,int week){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_YEAR, week);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        return (cal.getTime().before(convertLocalDateToDate(date)));
    }

    public Date convertLocalDateToDate(LocalDate localDate){
        ZoneId defaultZoneId = ZoneId.systemDefault();
        return Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
    }

    public boolean shiftManager(){
        return training.contains(Training.TeamManagement)&&training.contains(Training.CancellationCard);
    }

    public String getDetails(){
        StringBuilder s= new StringBuilder("ID:" + id + " | NAME:" + name + "\n" + "BANK ACCOUNT:" + bankAccount + " | SALARY:" + salary + " | HIRING CONDITION:" + hiringCondition +
                " | START DATE:" + startOfEmployment);
        if(finishWorking)
            s.append("\n").append(" | FINISH TO WORK");
        else {
            s.append("\n").append("JOB TYPE:").append(jobType);
            if(!training.isEmpty()) {
                s.append(" | TRAINING:");
                for (Training t : training)
                    s.append(t).append(" ");
            }
        }
        return s.toString();
    }

    public String getName() {
        return name;
    }

    public int getSalary() {
        return salary;
    }

    public int getBankAccount() {
        return bankAccount;
    }

    public boolean getFinishWorking() {
        return finishWorking;
    }

    public String getHiringCondition() {
        return hiringCondition;
    }

    public List<Training> getTraining(){return training;}

    public String getStringTraining(){
        StringBuilder stringTrainig= new StringBuilder();
        for (Training t:training) {
            stringTrainig.append(t.toString()).append(" ");
        }
        return stringTrainig.toString();
    }

    public Map<LocalDate, List<String>> getMapConstraint() {
        Map<LocalDate, List<String>> copyConstraint = new HashMap<>();
        for (LocalDate date : constraint.keySet()) {
            copyConstraint.computeIfAbsent(date, k -> new LinkedList<>());
            for (ShiftType shifType : constraint.get(date)) {
                copyConstraint.get(date).add(shifType.toString());
            }

        }
        return copyConstraint;
    }

    public Map<LocalDate, List<String>> getStringShifts(){
        Map<LocalDate, List<String>> copyShifts =new HashMap<>();
        for(LocalDate date :shifts.keySet()){
            copyShifts.computeIfAbsent(date, k -> new LinkedList<>());
            for (ShiftType shifType:shifts.get(date)) {
                copyShifts.get(date).add(shifType.toString());
            }
        }
        return copyShifts;
    }

    public  List<String> getTrainingBystringList(){
        List< String> s= new LinkedList<>();
        if(!training.isEmpty()){
            for (Training t:training) {
                s.add(t.toString());
            }}
        return s;
    }

    public void SetEmployeeConstrainsFromDAO(Map<String,List<String>> map){
        DateTimeFormatter formatter_1= DateTimeFormatter.ofPattern("dd/MM/yyyy");
        ShiftType shiftTypeFromString= ShiftType.Morning;
        Map<LocalDate,List<ShiftType>> myConstrains= new HashMap<>();
        if(map!=null){
        for(String dateString:map.keySet()) {
            LocalDate date = LocalDate.parse(dateString, formatter_1);
            if (!myConstrains.containsKey(date))
                myConstrains.put(date, new LinkedList<>());
            for (String shiftTypeString : map.get(dateString)) {
                if (shiftTypeString.equals("Evening")) ;
                shiftTypeFromString = ShiftType.Evening;
                if (!myConstrains.get(date).contains(shiftTypeFromString))
                    myConstrains.get(date).add(shiftTypeFromString);
            }
        }
        }this.constraint=myConstrains;
    }

    public void SetEmployeeShiftsFromDAO(Map<String,List<String>> map){
        DateTimeFormatter formatter_1= DateTimeFormatter.ofPattern("dd/MM/yyyy");
        ShiftType shiftTypeFromString= ShiftType.Morning;
        Map<LocalDate,List<ShiftType>> myShifts= new HashMap<>();
        if(map!=null){
        for(String dateString:map.keySet()) {
            LocalDate date = LocalDate.parse(dateString, formatter_1);
            if (!myShifts.containsKey(date))
                myShifts.put(date, new LinkedList<>());
            for (String shiftTypeString : map.get(dateString)) {
                if (shiftTypeString.equals("Evening")) ;
                shiftTypeFromString = ShiftType.Evening;
                if (!myShifts.get(date).contains(shiftTypeFromString))
                    myShifts.get(date).add(shiftTypeFromString);
            }
        }
        }this.shifts=myShifts;
    }

    public void setConstraint(Map<LocalDate, List<String>> constraint) {
        for(LocalDate date:constraint.keySet()){
            this.constraint.put(date,convertListSToShiftType(constraint.get(date)));
        }
    }

    public List<ShiftType> convertListSToShiftType(List<String> l){
        List<ShiftType> list=new ArrayList<>();
        for(String s:l)
            list.add(shiftType(s));
        return list;
    }

    private ShiftType shiftType(String shift){
        if(shift.equals("Morning"))
            return ShiftType.Morning;
        if (shift.equals("Evening"))
            return ShiftType.Evening;
        throw new IllegalArgumentException("the option is Morning or Evening");
    }

    public void setShifts(Map<LocalDate, List<String>> shifts) {
        for(LocalDate date:shifts.keySet()){
            this.shifts.put(date,convertListSToShiftType(shifts.get(date)));
        }
    }

    @Override
    public String toString(){
        String s="ID:"+id+" NAME:"+name+" INFO:";
        if(finishWorking)
            s=s+" FINISH TO WORK";
        else {
            s = s + " JOB TYPE:" + jobType;
            if (shiftManager())
                s = s + " CAN BE SHIFT MANAGER";
        }
        return s;
    }
}
