package lk.acpt.demoee.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.acpt.demoee.dto.EmployeeDto;
import lk.acpt.demoee.service.EmployeeService;
import lk.acpt.demoee.service.ipml.EmployeeServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/employee")
public class EmployeeServlet extends HttpServlet {
    private final EmployeeService employeeService = new EmployeeServiceImpl();
    private final Gson gson = new Gson();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
String nic = req.getParameter("nic");

if(nic==null|| nic.isEmpty()){
    List<EmployeeDto> allEmployee = employeeService.getAllEmployee();
    String json = gson.toJson(allEmployee);
    resp.getWriter().write(json);
}
else {
    EmployeeDto employeeDto = employeeService.searchEmployee(nic);
    if(employeeDto!=null){
        String json = gson.toJson(employeeDto);
        resp.getWriter().write(json);
    }
    else {
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
     resp.getWriter().write("Employee not found");
    }
}
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EmployeeDto employeeDto = gson.fromJson(req.getReader().readLine(), EmployeeDto.class);
        EmployeeDto savedEmployee = employeeService.saveEmployee(employeeDto);

        if (employeeDto != null) {
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(gson.toJson(savedEmployee));
        }
        else  {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Convert JSON → Java object
        Gson gson = new Gson();
        EmployeeDto employeeDto =
                gson.fromJson(req.getReader(), EmployeeDto.class);

        // Call service
        boolean updated = employeeService.updateEmployee(employeeDto);

        // Send response status
        if (updated) {
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}
