<#import "../layout.ftlh" as main>
<@main.layout>
    <div class="container mt-5">
        <h2>Update Employee</h2>

        <form action="/employees/${employeeDto.id}/edit" method="post" class="needs-validation" novalidate>
            <div class="form-group">
                <label for="fullName">Full Name:</label>
                <input type="text" class="form-control" id="fullName" name="fullName" value="${employeeDto.fullName}" required>
            </div>

            <div class="form-group">
                <label for="position">Position:</label>
                <select class="form-control" id="position" name="position.id" required>
                    <#list positions as position>
                        <option value="${position.id}"<#if position.id == employeeDto.position.id> selected</#if>>${position.name}</option>
                    </#list>
                </select>
            </div>

            <div class="form-group">
                <label for="salary">Salary:</label>
                <input type="text" class="form-control" id="salary" name="salary"
                       value="${employeeDto.salary?string['0.##']}" required>
            </div>

            <div class="form-group">
                <label for="address">Address:</label>
                <input type="text" class="form-control" id="address" name="address" value="${employeeDto.address}" required>
            </div>

            <div class="form-group">
                <label for="phoneNumber">Phone Number:</label>
                <input type="text" class="form-control" id="phoneNumber" name="phoneNumber" value="${employeeDto.phoneNumber}">
            </div>

            <div class="mt-2">
                <button type="submit" class="btn btn-primary">Update</button>
                <a href="/employees/${employeeDto.id}" class="btn btn-secondary mx-3">Cancel</a>
            </div>
        </form>
    </div>
</@main.layout>
