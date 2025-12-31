package lk.acpt.demoee.service;

import lk.acpt.demoee.dto.EmployeeDto;

import java.util.List;

public interface EmployeeService {
    EmployeeDto saveEmployee(EmployeeDto employeeDto);
    boolean deleteEmployee(String nic);
    List<EmployeeDto> getAllEmployee();
    EmployeeDto searchEmployee(String nic);
    boolean updateEmployee(EmployeeDto employeeDto);


}
