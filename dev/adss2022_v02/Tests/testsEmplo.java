import BussinessLayer.Managers.EmployeesManager;
import BussinessLayer.Managers.ShiftsManager;
import BussinessLayer.Objects.Employee;
import BussinessLayer.Objects.JobType;
import BussinessLayer.Objects.ShiftType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class testsEmplo {
    public EmployeesManager employeesManager;
    public ShiftsManager shiftsManager;
    Map<JobType, Integer> shiftStructureA;
    DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    LocalDate date1;
    LocalDate date2;
    LocalDate date3;
    LocalDate date4;
    LocalDate date5;

    public testsEmplo() {
        employeesManager=EmployeesManager.getInstance();
        shiftsManager=ShiftsManager.getInstance();
        date1 = LocalDate.parse("30/06/2022", formatters);
        date2 = LocalDate.parse("10/07/2022", formatters);
        date3 = LocalDate.parse("02/06/2022", formatters);
        date4 = LocalDate.parse("01/01/2022", formatters);
        date5 = LocalDate.parse("01/01/2023", formatters);
        shiftStructureA=new HashMap<>();
        shiftStructureA.put(JobType.Usher, 1);
        shiftStructureA.put(JobType.Cashier, 1);
        shiftStructureA.put(JobType.Storekeeper, 1);
        shiftStructureA.put(JobType.Driver, 3);
        employeesManager.DelData();
        shiftsManager.DelData();
    }

    @Before
    public void setUp() throws ParseException {
        employeesManager.loadData();
        shiftsManager.loadData();
    }

    @After
    public void tearDown()  {
        employeesManager.DelData();
        shiftsManager.DelData();
    }


    @Test
    public void addExistEmplyeeFail() {
        try {
            employeesManager.addEmployee(208687319, "lior levy", 123456789, 9999, "normal hiringCondition","Usher","");
            fail("need to throw error");
        } catch (Exception  e) {

        }
    }

    @Test
    public void addEmplyeeSuccessfully() {
        try {
            int sizeOfEmployeeList=employeesManager.getEmployess().size();
           employeesManager.addEmployee(208687399, "eli cohenn", 123411111, 9999, "normal hiringCondition", "Usher"," ");
            assertEquals(sizeOfEmployeeList + 1,employeesManager.getEmployess().size());
        } catch (Exception  e) {
            assertEquals( null,e.getMessage());

        }
    }

    @Test
    public void getEmployeeByIdThatNotExistSuccessfully() {
        try {
            Employee employee= employeesManager.getEmployee(208687350);
            fail("need to throw error");
        } catch (Exception  e) {
        }
    }


    @Test
    public void updateBankByIdSuccessfully() {
        try {
            employeesManager.updatebank(202876546, 111111);
            assertEquals(employeesManager.getEmployee(202876546).getBankAccount(), 111111);
        } catch (Exception e) {
            assertEquals( null,e.getMessage());
        }
    }



    @Test
    public void updateSalaryByIdSuccessfully() {
        try {
            employeesManager.updateSalary(202876546,111111);
            assertEquals(employeesManager.getEmployee(202876546).getSalary(),111111);
        } catch (Exception  e) {
            assertEquals( null,e.getMessage());
        }}



    @Test
    public void addTrainSuccessfully() {
        employeesManager.addTrain(202876546, "CancellationCard");
        assertTrue(employeesManager.getEmployee(202876546).getTraining().contains(Employee.Training.CancellationCard));
    }



    @Test
    public void AvailableSuccessfully() {
        int sizeWithout =employeesManager.getAvailableEmployees(date1, ShiftType.Morning).size();
        Employee employeeWithConstraint = employeesManager.getEmployee(207687849);
        employeesManager.addConstraint(date1, ShiftType.Morning, employeeWithConstraint);
        int size =employeesManager.getAvailableEmployees(date1, ShiftType.Morning).size();
        assertEquals(sizeWithout-1,size);
    }


    @Test
    //employee not exist
    public void switchEmployessFail() {
        try {
            employeesManager.switchEmployess(11111,111112,date1,ShiftType.Morning);
            fail("need to throw error");
        } catch (Exception  e) {

        }
    }


    @Test
    public void addShiftFail(){
        try {
            shiftsManager.addShift(date2,ShiftType.Morning,shiftStructureA);
        } catch (Exception  e) {
            assertEquals("this date already pass",e.getMessage());
        }
    }

    @Test
    public void addShiftSuccefully() {
        int sizeOfShift;
        sizeOfShift=shiftsManager.shiftbeetween(date4,date5).size();
        shiftsManager.addShift(date2, ShiftType.Morning, shiftStructureA);
        assertEquals(sizeOfShift + 1, shiftsManager.shiftbeetween(date4,date5).size());
    }


    @Test
    public  void deleteArrangeFail() {
        try {
            shiftsManager.deleteArrange(date3,ShiftType.Evening);
            fail("need to throw error");
        } catch (Exception e) {
        }
    }


    @Test
    public  void deleteArrangeSuccefuly() {
        try {
            shiftsManager.addShift(date3, ShiftType.Morning, shiftStructureA);
            shiftsManager.deleteArrange(date3,ShiftType.Morning);
            fail("need to throw error");
        } catch (Exception e) {
        }
    }

}
