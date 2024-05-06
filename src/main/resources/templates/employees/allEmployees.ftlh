<#import "../layout.ftlh" as main>

<@main.layout>
    <h2>Employee List</h2>

    <div class="mt-3">
        <div class="btn-group">
            <button type="button" class="btn btn-outline-dark dropdown-toggle" data-bs-toggle="dropdown"
                    aria-haspopup="true" aria-expanded="false">
                Sort By
            </button>
            <ul class="dropdown-menu">
                <li><a class="dropdown-item" href="/employees/all?sort=fullName">Full Name</a></li>
                <li><a class="dropdown-item" href="/employees/all?sort=salary">Salary</a></li>
                <li><a class="dropdown-item" href="/employees/all?sort=address">Address</a></li>
                <!-- Add more sorting options as needed -->
            </ul>
        </div>
    </div>

    <table class="table mt-3">
        <thead>
        <tr>
            <th scope="col">Full Name</th>
            <th scope="col">Salary</th>
            <th scope="col">Address</th>
            <th scope="col">Phone Number</th>
            <th scope="col">Position</th>
            <th scope="col">Actions</th>
        </tr>
        </thead>
        <tbody>
        <#list employees.content as employee>
            <tr>
                <td>${employee.fullName}</td>
                <td>${employee.salary}</td>
                <td>${employee.address}</td>
                <td>${employee.phoneNumber}</td>
                <#list positions as position>
                    <#if employee.position.id == position.id>
                        <td>${position.name}</td>
                    </#if>
                </#list>
                <#if roles?seq_contains('ADMIN')>
                    <td>
                        <a href="/employees/${employee.id}/edit" class="btn btn-outline-warning mx-2">Edit</a>
                        <button class="btn btn-outline-danger" onclick="confirmDelete(${employee.id})">Delete</button>
                    </td>
<#--                <#elseif roles?seq_contains('DIRECTOR')>-->
<#--                    <td>-->
<#--                        <a href="/employees/${employee.id}" class="btn btn-outline-info mx-2">View</a>-->
<#--                    </td>-->
                </#if>
            </tr>
        </#list>
        </tbody>
    </table>

    <div class="mt-3">
        <#if roles?seq_contains('ADMIN')>
            <a href="/employees/create" class="btn btn-success">Add Employee</a>
        </#if>
    </div>
    <script>
        function confirmDelete(employeeId) {
            var confirmation = confirm("Are you sure you want to delete this employee?");
            if (confirmation) {
                window.location.href = '/employees/' + employeeId + '/delete/confirmed';
            }
        }
    </script>
</@main.layout>
