package lk.acpt.demoee.service.ipml;

import lk.acpt.demoee.db.DBConnection;
import lk.acpt.demoee.dto.EmployeeDto;
import lk.acpt.demoee.service.EmployeeService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeServiceImpl implements EmployeeService {
    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {
        try {
            Connection connection = DBConnection.getDbConnection().getConnection();

            // create the dynamic quary
            PreparedStatement stm = connection.prepareStatement("insert into employee(empNic,empName,empAge,empSalary) values(? , ? , ? ,?)");

            stm.setObject(1, employeeDto.getNic());
            stm.setObject(2, employeeDto.getName());
            stm.setObject(3, employeeDto.getAge());
            stm.setObject(4, employeeDto.getSalary());


            int i = stm.executeUpdate();

            if (i > 0) {
                return employeeDto;

            } else {
                return null;
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean deleteEmployee(String nic) {
        return false;
    }

    @Override
    public List<EmployeeDto> getAllEmployee() {
        List<EmployeeDto> list = new ArrayList<>();
        try {
//            // load Driver class to ram
//            Class.forName("com.mysql.cj.jdbc.Driver");
//
//            //create a connaction with selected database
//            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/acpt_1", "root", "123456");

            Connection connection = DBConnection.getDbConnection().getConnection();

            // create the dynamic quary
            PreparedStatement stm = connection.prepareStatement("select * from employee ");


            ResultSet rst = stm.executeQuery();


            while (rst.next()) {
                EmployeeDto dto = new EmployeeDto(rst.getNString("empNic"), rst.getNString("empName"), rst.getInt("empAge"), rst.getDouble("empSalary"));
                list.add(dto);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return list;
    }

    @Override
    public EmployeeDto searchEmployee(String nic) {
        try {
//            // load Driver class to ram
//            Class.forName("com.mysql.cj.jdbc.Driver");
//
//            //create a connaction with selected database
//            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/acpt_1", "root", "123456");
            Connection connection = DBConnection.getDbConnection().getConnection();

            // create the dynamic quary
            PreparedStatement stm = connection.prepareStatement("select * from employee where empNic=?");

            stm.setString(1, nic);


            ResultSet rst = stm.executeQuery();


            if (rst.next()) {
                return new EmployeeDto(rst.getNString("empNic"), rst.getNString("empName"), rst.getInt("empAge"), rst.getDouble("empSalary")

                );

            } else {
                return null;
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean updateEmployee(EmployeeDto employeeDto) {
        try {
            Connection connection = DBConnection.getDbConnection().getConnection();

            PreparedStatement stm = connection.prepareStatement(
                    "UPDATE employee SET empName=?, empAge=?, empSalary=? WHERE empNic=?"
            );

            stm.setString(1, employeeDto.getName());
            stm.setInt(2, employeeDto.getAge());
            stm.setDouble(3, employeeDto.getSalary());
            stm.setString(4, employeeDto.getNic());

            return stm.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    }


//
//    @Override
//    public EmployeeDto updateEmployee(EmployeeDto employeeDto) {
//        try {
////            // load Driver class to ram
////            Class.forName("com.mysql.cj.jdbc.Driver");
//////create a connaction with selected database
////            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/acpt_1", "root", "123456");
//
//            Connection connection = DBConnection.getDbConnection().getConnection();
//            // create the dynamic quary
//            PreparedStatement stm = connection.prepareStatement("UPDATE employee SET empName=?, empAge=?, empSalary=? WHERE empNic=?");
//
//
//            stm.setObject(1, employeeDto.getName());
//            stm.setObject(2, employeeDto.getAge());
//            stm.setObject(3, employeeDto.getSalary());
//            stm.setObject(4, employeeDto.getNic());
////            System.out.println(employeeDto.getNic());
//
//
//            int i = stm.executeUpdate();
//
//            if (i > 0) {
//                return employeeDto;
//
//            } else {
//                return null;
//            }
//        } catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//    }


