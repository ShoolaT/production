<#import "../layout.ftlh" as main>
<@main.layout>
    <div class="container mt-2">
        <h2>Edit Salary</h2>

        <form action="/salaries/${salary1.id}/edit" method="post" class="needs-validation" novalidate>
            <div class="form-group">
                <label for="employee">Employee:</label>
                <input type="text" class="form-control" id="employee" name="employee.fullName" value="${salary1.employee.fullName}" readonly>
                <input type="hidden" name="employee.id" value="${salary1.employee.id}">
            </div>
            <div class="form-group">
                <label for="year">Year:</label>
                <input type="text" class="form-control" id="year" name="year" value="${year}" readonly>
            </div>

            <div class="form-group">
                <label for="month">Month:</label>
                <input type="text" class="form-control" id="month" name="month" value="${salary1.month}" readonly>
            </div>

            <div class="form-group">
                <label for="numberOfPurchase">Quantity of Purchases:</label>
                <input type="text" class="form-control" id="numberOfPurchase" name="numberOfPurchase" value="${salary1.numberOfPurchase}" readonly>
            </div>

            <div class="form-group">
                <label for="numberOfProduction">Quantity of Production:</label>
                <input type="text" class="form-control" id="numberOfProduction" name="numberOfProduction" value="${salary1.numberOfProduction}" readonly>
            </div>

            <div class="form-group">
                <label for="numberOfSale">Quantity of Sales:</label>
                <input type="text" class="form-control" id="numberOfSale" name="numberOfSale" value="${salary1.numberOfSale}" readonly>
            </div>

            <div class="form-group">
                <label for="common">Common:</label>
                <input type="text" class="form-control" id="common" name="common" value="${salary1.common}" readonly>
            </div>

            <div class="form-group">
                <label for="salary">Salary:</label>
                <input type="text" class="form-control" id="salary" name="salaryAmount" value="${salaryAmount}" readonly>
            </div>

            <div class="form-group">
                <label for="bonus">Bonus:</label>
                <input type="text" class="form-control" id="bonus" name="bonus" value="${bonus}" readonly>
            </div>

            <div class="form-group">
                <label for="general">Total Amount:</label>
                <input type="text" class="form-control" id="general" name="general" value="${general}" required>
            </div>
            <div class="form-group">
                <label for="issued">Issued:</label>
                <input type="text" class="form-control" id="issued" name="issued" value="${salary1.issued?string('Yes', 'No')}" readonly>
            </div>
            <div class="mt-2">
                <button type="submit" class="btn btn-primary">Update</button>
                <a href="/salaries/all" class="btn btn-secondary mx-3">Cancel</a>
            </div>
        </form>
    </div>
</@main.layout>
