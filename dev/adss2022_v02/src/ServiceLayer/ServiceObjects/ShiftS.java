package ServiceLayer.ServiceObjects;
import BussinessLayer.Objects.Shift;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ShiftS {
    public LocalDate date;
    public String shiftType;
    public Map<String, Integer> shiftStructure;
    private final List<Integer> employeesID;
    private int shiftManagerID;


    public ShiftS(LocalDate date, String shiftType){
        if(LocalDate.now().isAfter(date))
            throw new IllegalArgumentException("this date already pass");
        this.date=date;
        this.shiftType=shiftType;
        this.shiftStructure=new HashMap<>();
        employeesID=new LinkedList<>();
        shiftManagerID=0;
    }


    public ShiftS(Shift shift){
        this.date= shift.getDate();
        this.shiftType=shift.getStringShiftType();
        this.shiftStructure= shift.getMapShiftStructure();
        this.employeesID=shift.getEmployees();
        this.shiftManagerID= shift.getManager();
    }


    @Override
    public String toString(){
        StringBuilder s;
        s = new StringBuilder("DATE: "+ date + " " + " | SHIFT TYPE: "+shiftType + "\n" + "SHIFT STRUCTURE: " + "\n");
        for (String j:shiftStructure.keySet()) {
            s.append(j).append(": ").append(shiftStructure.get(j)).append("\n");
        }
        if(employeesID.size()==0)
            s.append("haven't assigned the employees yet");
        else {
            s.append("employees:");
            if (shiftManagerID != 0) {
                s.append("\nShift Manager:").append(shiftManagerID).append("\n");
                for (Integer e : employeesID) {
                    s.append(e).append("  ");
                }
                s.append("\n");
            }
        }
        return s.toString();
    }


}
