package DataAccessLayer;

import BussinessLayer.Objects.Employee;
import BussinessLayer.Objects.Driver;
import DataAccessLayer.Dal.DBHandler;

import java.sql.*;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
//import DataAccessLayer.EmployeeShiftDAO;

public class EmployeeDAO extends DAO {

    private static String TableName = "Employee";
    public static final String IDColumnName = "ID";
    public static final String NameColumnName = "Name";
    public static final String BankAccountColumnName = "BankAccount";
    public static final String SalaryColumnName = "Salary";
    public static final String HiringConditionsColumnName = "HiringConditions";
    public static final String StartOfEmploymentColumnName = "StartOfEmployment";
    public static final String FinishWorkingColumnName = "FinishWorking";
    public static final String JobTypeColumnName = "JobType";
    private HashMap<Integer, Employee> employeesCache;
    private HashMap<Integer, Driver> driverCache;

    public EmployeeDAO() {
        super(TableName);
        employeesCache = new HashMap<>();
    }

    //table column names

    @Override
    public boolean Insert(Object employeeObj) {
        Employee employee = (Employee) employeeObj;
        boolean res = true;
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4}, {5}, {6} ,{7},{8}) VALUES(?, ?, ?, ?, ?, ?,?,?) "
                , _tableName, IDColumnName, NameColumnName, BankAccountColumnName, SalaryColumnName, HiringConditionsColumnName, StartOfEmploymentColumnName, FinishWorkingColumnName, JobTypeColumnName
        );
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            //Connection connection=open
            //pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, employee.getId());
            pstmt.setString(2, employee.getName());
            pstmt.setInt(3, employee.getBankAccount());
            pstmt.setInt(4, employee.getSalary());
            pstmt.setString(5, employee.getHiringCondition());
            pstmt.setString(6, employee.getStartOfEmployment().format(formatters));
            pstmt.setBoolean(7, employee.getFinishWorking());
            pstmt.setString(8, employee.getJobType().toString());
            pstmt.executeUpdate();
            //   EmployeeConstraintsDAO.Insert


        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public boolean Insert(Driver driver) {
        Employee employee = new Employee(driver.getId(), driver.getName(), driver.getBankAccount(), driver.getSalary(), driver.getHiringCondition(), driver.getJobType().toString());
        boolean res = true;
        String sql = MessageFormat.format("INSERT INTO {0} ({1}, {2}, {3}, {4}, {5}, {6} ,{7},{8}) VALUES(?, ?, ?, ?, ?, ?,?,?) "
                , _tableName, IDColumnName, NameColumnName, BankAccountColumnName, SalaryColumnName, HiringConditionsColumnName, StartOfEmploymentColumnName, FinishWorkingColumnName, JobTypeColumnName
        );
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            //Connection connection=open
            //pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, employee.getId());
            pstmt.setString(2, employee.getName());
            pstmt.setInt(3, employee.getBankAccount());
            pstmt.setInt(4, employee.getSalary());
            pstmt.setString(5, employee.getHiringCondition());
            pstmt.setString(6, employee.getStartOfEmployment().format(formatters));
            pstmt.setBoolean(7, employee.getFinishWorking());
            pstmt.setString(8, employee.getJobType().toString());
            pstmt.executeUpdate();
            //   EmployeeConstraintsDAO.Insert


        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }


    @Override
    public boolean Delete(Object employeeObj) {
        Employee employee = (Employee) employeeObj;
        boolean res = true;
        String sql = MessageFormat.format("DELETE FROM {0} WHERE {1} = ? "
                , _tableName, IDColumnName);

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
//            Connection connection=open
//            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, employee.getId());
            pstmt.executeUpdate();


        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
            res = false;
        }
        return res;
    }

    public boolean inCache(int idEmployee){
        return employeesCache.containsKey(idEmployee);
    }

    public List<Employee> SelectAllEmployees() {
        List<Employee> list = (List<Employee>) (List<?>) Select();
        return list;
    }

    public List<Employee> SelectAllEmployeesID() {
        List<Employee> list = (List<Employee>) (List<?>) Select();
        return list;
    }

    @Override
    public Employee convertReaderToObject(ResultSet rs) throws SQLException {
        if(employeesCache.containsKey(rs.getInt(1)))
            return employeesCache.get(rs.getInt(1));
        Employee employee = new Employee(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getString(5), parseLocalDate(rs.getString(6)), rs.getBoolean(7), rs.getString(8));
        employeesCache.put(employee.getId(), employee);
        return employee;
    }

    public void setBankAccount(int id, int bankAccount) {
        Update(BankAccountColumnName, bankAccount, makeList(IDColumnName), makeList(String.valueOf(id)));
    }

    public void setSalary(int id, int salary) {
        Update(SalaryColumnName, salary, makeList(IDColumnName), makeList(String.valueOf(id)));
    }

    public void setHiringConditions(int id, String hiringConditions) {
        Update(HiringConditionsColumnName, hiringConditions, makeList(IDColumnName), makeList(String.valueOf(id)));
    }

    public void setStartOfEmployment(int id, String startOfEmployment) {
        Update(StartOfEmploymentColumnName, startOfEmployment, makeList(IDColumnName), makeList(String.valueOf(id)));
    }

    public void setFinishWorking(int id, boolean finishWorking) {
        Update(FinishWorkingColumnName, finishWorking, makeList(IDColumnName), makeList(String.valueOf(id)));
    }

    public void setJobType(int id, String jobType) {
        Update(JobTypeColumnName, jobType, makeList(IDColumnName), makeList(String.valueOf(id)));
    }


    public Employee getEmployee(int id) {
        if (employeesCache.containsKey(id)) //Employee in cache
            return employeesCache.get(id);
        else {//Employee in db
            List<Employee> result = Select(makeList(IDColumnName), makeList(String.valueOf(id)));
            if (result.size() == 0)
                throw new IllegalArgumentException("Employee " + id + " does not exist");
            Employee employee = result.get(0);
            employeesCache.put(id, employee);
            return employee;
        }
    }


    public boolean ExistEmployee(int id){
        boolean exist =false;
        /// keys is for tables that have more that one key
        String sql = MessageFormat.format("SELECT {0} From {1} WHERE {2} = ?",
                IDColumnName,  _tableName, IDColumnName);
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                // Fetch each row from the result set
                exist=true;
            }

        } catch (SQLException e) {
            System.out.println("Got Exception:");
            System.out.println(e.getMessage());
            System.out.println(sql);
        }
        return exist;
    }

    public void resetCache() {
        employeesCache.clear();
    }

    public static int getmaxID(){
        int MaxId = 0;
        try {
            Connection c = DBHandler.getInstance().open();
            Statement stmt;
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + DBHandler.Employee + ";");
            while (rs.next()) {
                int cId = rs.getInt(IDColumnName);
                if (cId > MaxId) {
                    MaxId = cId;
                }
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MaxId;
    }

}


