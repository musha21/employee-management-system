<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Employee Management</title>


    <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
            rel="stylesheet">

    <style>
        body {
            background-color: #f4f6f9;
        }

        .card {
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
        }
    </style>
</head>

<body>

<div class="container mt-5">

    <h2 class="text-center mb-4">Employee Management</h2>


    <div class="card p-4 mb-4">
        <h5>Add Employee</h5>

        <div class="row g-3 mt-2">
            <div class="col-md-3">
                <input type="text" id="saveNic" class="form-control" placeholder="NIC">
            </div>
            <div class="col-md-3">
                <input type="text" id="saveName" class="form-control" placeholder="Name">
            </div>
            <div class="col-md-2">
                <input type="number" id="saveAge" class="form-control" placeholder="Age">
            </div>
            <div class="col-md-2">
                <input type="number" id="saveSalary" class="form-control" placeholder="Salary">
            </div>
            <div class="col-md-2 d-grid">
                <button type="button" class="btn btn-primary" onclick="saveEmploee()">
                    Save
                </button>

            </div>
        </div>
    </div>


    <div class="row mb-3">
        <div class="col-md-10">
            <input type="text" id="searchNic" class="form-control"
                   placeholder="Search by NIC">
        </div>
        <div class="col-md-2 d-grid">
            <button type="button" class="btn btn-secondary"
                    onclick="searchEmployee()">
                Search
            </button>
        </div>
    </div>


    <div class="card p-3">
        <table class="table table-bordered table-hover text-center">
            <thead class="table-dark">
            <tr>
                <th>NIC</th>
                <th>Name</th>
                <th>Age</th>
                <th>Salary</th>
                <th>Actions</th>
            </tr>
            </thead>
            <tbody id="employeeTable">
            </tbody>
        </table>
    </div>

</div>


<div class="modal fade" id="updateModal" tabindex="-1">
    <div class="modal-dialog">
        <div class="modal-content">

            <div class="modal-header">
                <h5 class="modal-title">Update Employee</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>

            <div class="modal-body">
                <input type="hidden" id="updateIndex">

                <div class="mb-2">
                    <label>NIC</label>
                    <input type="text" id="updateNic" class="form-control" disabled>
                </div>

                <div class="mb-2">
                    <label>Name</label>
                    <input type="text" id="updateName" class="form-control">
                </div>

                <div class="mb-2">
                    <label>Age</label>
                    <input type="number" id="updateAge" class="form-control">
                </div>

                <div class="mb-2">
                    <label>Salary</label>
                    <input type="number" id="updateSalary" class="form-control">
                </div>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-secondary"
                        data-bs-dismiss="modal">Cancel
                </button>
                <button type="button" class="btn btn-success"
                        onclick="updateEmployee()">Update
                </button>
            </div>

        </div>
    </div>
</div>


<script
        src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js">
</script>

<script>


    function saveEmploee() {
        const nic = saveNic.value.trim();
        const name = saveName.value.trim();
        const age = saveAge.value.trim();
        const salary = saveSalary.value.trim();

        // 1. Check empty fields
        if (!nic || !name || !age || !salary) {
            alert("Please fill all fields");
            return;
        }

        // 2. Check duplicate NIC in current table
        if (window.displayedEmployees?.some(emp => emp.nic === nic)) {
            alert("NIC already exists! Please use a different NIC.");
            return;
        }

        const emp = {
            nic: nic,
            name: name,
            age: parseInt(age),
            salary: parseFloat(salary)
        };

        fetch('http://localhost:8080/demoEE_war_exploded/employee', {
            method: 'POST',
            body: JSON.stringify(emp),
            headers: {
                'Content-type': 'application/json; charset=UTF-8',
            },
        })
            .then(res => {
                if (res.status === 409) throw new Error("NIC already exists");
                if (!res.ok) throw new Error("Save failed");
                return res.json();
            })
            .then(() => {
                loadAllEmployees();
            clearForm();
                alert("Employee saved successfully");
            })
            .catch(err => alert(err.message));
    }


    let employees = [];


    document.addEventListener("DOMContentLoaded", loadAllEmployees);


    function loadAllEmployees() {
        fetch("http://localhost:8080/demoEE_war_exploded/employee")
            .then(res => res.json())
            .then(data => renderTable(data));
    }


    function renderTable(data) {
        const table = document.getElementById("employeeTable");
        table.innerHTML = "";

        data.forEach((emp, index) => {
            table.innerHTML += `
        <tr>
            <td>\${emp.nic}</td>
            <td>\${emp.name}</td>
            <td>\${emp.age}</td>
            <td>\${emp.salary}</td>
            <td>
                <button class="btn btn-warning btn-sm"
                        onclick="openUpdate(\${index})">
                    Update
                </button>
                <button class="btn btn-danger btn-sm"
                        onclick="deleteEmployee('\${emp.nic}')">
                    Delete
                   </button>
            </td>
        </tr>`;
        });

        // store current displayed employees for modal reference
        window.displayedEmployees = data;


    }


    function searchEmployee() {
        const nic = document.getElementById("searchNic").value.trim();

        if (nic === "") {
            loadAllEmployees();
            return;
        }

        fetch("http://localhost:8080/demoEE_war_exploded/employee?nic=" + nic)
            .then(res => {
                if (!res.ok) throw new Error("Employee not found");
                return res.json();
            })
            .then(emp => renderTable([emp]))
            .catch(() => renderTable([]));
    }

    function openUpdate(index) {
        selectedEmployee = window.displayedEmployees[index]; // get employee from table

        if (!selectedEmployee) {
            alert("Employee not found");
            return;
        }

        document.getElementById("updateNic").value = selectedEmployee.nic;
        document.getElementById("updateName").value = selectedEmployee.name;
        document.getElementById("updateAge").value = selectedEmployee.age;
        document.getElementById("updateSalary").value = selectedEmployee.salary;

        new bootstrap.Modal(
            document.getElementById("updateModal")
        ).show();
    }


    let selectedEmployee = null; // global variable

    function updateEmployee() {
        if (!selectedEmployee) return;

        const emp = {
            nic: selectedEmployee.nic, // ensure backend can identify the employee
            name: document.getElementById("updateName").value,
            age: parseInt(document.getElementById("updateAge").value),
            salary: parseFloat(document.getElementById("updateSalary").value)
        };
        fetch("http://localhost:8080/demoEE_war_exploded/employee", {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(emp)
        })
            .then(res => {
                if (!res.ok) throw new Error("Update failed");
            })
            .then(() => loadAllEmployees())
            .finally(() => {
                bootstrap.Modal.getInstance(
                    document.getElementById("updateModal")
                ).hide();
            });
    }
    function deleteEmployee(nic) {
        console.log("Deleting NIC:", nic);

        if (!confirm("Are you sure you want to delete this employee?")) return;

        fetch("http://localhost:8080/demoEE_war_exploded/employee?nic=" + nic, {
            method: "DELETE"
        })
            .then(res => {
                if (!res.ok) throw new Error("Delete failed");
            })
            .then(() => loadAllEmployees());
    }


    function clearForm() {
        document.getElementById("saveNic").value = "";
        document.getElementById("saveName").value = "";
        document.getElementById("saveAge").value = "";
        document.getElementById("saveSalary").value = "";
    }

</script>

</body>
</html>