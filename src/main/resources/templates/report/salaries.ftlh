<#import "../layout.ftlh" as main>

<@main.layout>
    <h2>Salary Report</h2>

    <form action="/salaries/all/report" method="get">
        <label for="startDate">Start Date:</label>
        <#if startD??>
            <input type="date" id="startDate" name="startDate" value="${startD}">
        <#else>
            <input type="date" id="startDate" name="startDate">
        </#if>
        <label for="endDate">End Date:</label>
        <#if endD??>
            <input type="date" id="endDate" name="endDate" value="${endD}">
        <#else>
            <input type="date" id="endDate" name="endDate" value="${.now?string('yyyy-MM-dd')}">
        </#if>
        <button type="submit">Get Report</button>
    </form>
    <#if startD??>
        <a href="/salaries/all/print?startDate=${startD}&endDate=${endD}">Export PDF</a>
    </#if>

    <table class="table mt-3">
        <thead>
        <tr>
            <th scope="col">Employees</th>
            <th scope="col">Date</th>
            <th scope="col">Common</th>
            <th scope="col">Salary</th>
            <th scope="col">Bonus</th>
            <th scope="col">General</th>
            <th scope="col">Issued</th>
        </tr>
        </thead>
        <tbody>
        <#if salaries??>
            <#list salaries as salary>
                <tr>
                    <td>${salary.employee.fullName}</td>
                    <td>${salary.year}-${salary.month}</td>
                    <td>${salary.common}</td>
                    <td>${salary.salary}</td>
                    <td>${salary.bonus}</td>
                    <td>${salary.general}</td>
                    <td>${salary.issued?string('Yes', 'No')}</td>
                </tr>
            </#list>
            <tr>
                <td colspan="2">Total:</td>
                <td></td>
                <td></td>
                <td></td>
                <td>${total}</td>
                <td></td>
            </tr>
        </#if>
        </tbody>
    </table>


</@main.layout>
