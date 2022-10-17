package ServiceLayer.ServiceObjects;

import ServiceLayer.ModulesServices.InventoryService;
import ServiceLayer.ModulesServices.ServiceModelEmployees;
import ServiceLayer.ModulesServices.ServiceModelTransform;
import ServiceLayer.ModulesServices.SuppliersService;

import java.text.ParseException;

public class DataService {

    public DataService() {

    }

//    public void LoadData(ServiceModelEmployees employeeService, ServiceModelTransform transform)  throws ParseException {
//        //load data to all services - will be display at "instructions"
//        employeeService.LoadData();
//        transform.LoadData();
//    }

    public void DelData(ServiceModelEmployees employeeService, ServiceModelTransform transform) throws ParseException {
        //load data to all services - will be display at "instructions"
        employeeService.DelData();
        transform.DelData();
    }

    public void startingProgram(ServiceModelEmployees employees, ServiceModelTransform transform) {
        transform.startingProgram();
    }

    public void LoadData(ServiceModelEmployees employees, ServiceModelTransform transform, InventoryService inventoryService, SuppliersService suppliersService) throws ParseException {
        employees.LoadData(); //good
        inventoryService.loadData(); //good
        transform.LoadData(); //good
        suppliersService.loadData(); //??
    }

    public void startingProgram(ServiceModelEmployees employees, ServiceModelTransform transform, InventoryService inventoryService, SuppliersService suppliersService) {
        transform.startingProgram();
    }
}
