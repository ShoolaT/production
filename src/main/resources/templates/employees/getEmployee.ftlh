<#import "../layout.ftlh" as main>
<@main.layout>
    <div class="container mt-5">
        <h2>Employee Details</h2>

        <div class="card">
            <div class="card-body">
                <h5 class="card-title">${employee.fullName}</h5>
                <p class="card-text">
                    <strong>Position:</strong> ${employee.position.name}<br>
                    <strong>Salary:</strong> ${employee.salary}<br>
                    <strong>Address:</strong> ${employee.address}<br>
                    <strong>Phone number:</strong> ${employee.phoneNumber}<br>
                    <strong>Salary:</strong> ${employee.salary}<br>
                </p>
            </div>
        </div>

        <div class="mt-3">
            <a href="/employees/all" class="btn btn-primary">Back to Employees List</a>
        </div>
    </div>
</@main.layout>
