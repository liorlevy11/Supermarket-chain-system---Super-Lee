package ServiceLayer.ServiceObjects;

import BussinessLayer.Managers.Facade;
import BussinessLayer.Objects.JobType;
import BussinessLayer.Objects.Shift;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ShiftService {

    private final Facade shiftsManager;

    public ShiftService(){
        shiftsManager= Facade.getInstance();
    }
    public void shiftArrange(LocalDate date, String shiftType, Integer shiftMannager, Map<String, LinkedList<Integer>> inputShift ){
        shiftsManager.shiftArrange(date,shiftType,shiftMannager,inputShift);}
    public void addShift(LocalDate date, String shiftType, Map<String, Integer> shiftStructure){
        shiftsManager.addShift(date,shiftType, shiftStructure);}

    public void addShift(LocalDate date, LocalDate date1, String shiftType1){
        shiftsManager.addShift(date,date1,shiftType1); }

    public  List<ShiftS> displayShift(LocalDate start, LocalDate end) {
        List<Shift> shifts = shiftsManager.displayShift(start, end);
        List<ShiftS> shiftS = new LinkedList<>();
        for (Shift shift : shifts) {
            shiftS.add(new ShiftS(shift));
        }
        return shiftS;
    }
/*
>>>>>>> 3dd01771a6b5a43743ab18ab96b2ef74c48b75cc
        StringBuilder s = new StringBuilder(" ");
        for (Object shi : shifts) {
            s.append(shi);
            s.append("\n");
        }
        return s.toString();
    }*/



    public void deleteArrange(LocalDate date, String shiftType){
        shiftsManager.deleteArrange(date,shiftType);
    }

    public void deleteEmployee(int id, LocalDate date1, String shiftTime) {
        shiftsManager.deleteEmployee(id,date1,shiftTime);
    }

    public void addEmployeeToShift(int id, LocalDate date1, String shiftTime) {
        shiftsManager.addEmployee(id,date1,shiftTime);
    }

    public void switchEmployee(int id1, int id2, LocalDate date1, String shiftTime) {
        shiftsManager.switchEmployees(id1,id2,date1,shiftTime);
    }
    public void switchManager(int id1, int id2, LocalDate date1, String shiftTime ) {
        shiftsManager.switchManager(id1,id2,date1,shiftTime);
    }
    public void updateshift(LocalDate date1, String shiftTime, Map<String, Integer> structure) {
        shiftsManager.updateshift(date1,shiftTime,structure);
    }

    public void updateJobInShift(LocalDate date, String shiftTime, String job, int num) {
        shiftsManager.updateJobInShift(date,shiftTime,job,num);
    }

    //job action
    public List<String> getJobType(String shiftTime) {
        List<String> StringjobTypes = new LinkedList<>();
        List<JobType> jobTypes = shiftsManager.jobTypes(shiftTime);
        for (JobType j : jobTypes)
            StringjobTypes.add(j.toString());
        return StringjobTypes;
    }

    public void DelData() {
       // shiftsManager.DelData();
    }
}
