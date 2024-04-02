<#import "../layout.ftlh" as main>

<@main.layout>
    <h2>Salary List</h2>

    <form id="salaryForm" action="/salaries/all" method="post">
        <label for="year">Year:</label>
        <select id="year" name="year">
            <#list years as year>
                <#if year == currentYear>
                    <option value="${year}" selected>${year}</option>
                <#else>
                    <option value="${year}">${year}</option>
                </#if>
            </#list>
        </select>

        <label for="month">Month:</label>
        <select id="month" name="month">
            <#list months as m>
                <#if m_index + 1 == currentMonth>
                    <option value="${m_index + 1}" selected>${m}</option>
                <#else>
                    <option value="${m_index + 1}">${m}</option>
                </#if>
            </#list>
        </select>
    </form>

    <script>
        // Обработка изменений в полях year и month
        document.getElementById('year').addEventListener('change', function () {
            document.getElementById('salaryForm').submit();
        });

        document.getElementById('month').addEventListener('change', function () {
            document.getElementById('salaryForm').submit();
        });
    </script>

    <p class="mt-3 fs-3 text-success">Budget: ${budget}</p>
    <p class="mt-3 fs-4 text-primary">Total amount: ${totalAmount}</p>
    <#if salaries[0].issued == false>
        <form id="issueForm" action="/salaries/issue" method="post">
            <input type="hidden" name="year" value="${currentYear}">
            <input type="hidden" name="month" value="${currentMonth}">
            <button id="issueButton" class="btn btn-outline-primary mx-2">Issue</button>
        </form>
    <#else>
        <p class="text-primary">This month's salary had already been issued</p>
    </#if>


    <#if salaries??>
        <table class="table mt-3">
            <thead>
            <tr>
                <th scope="col">Employees</th>
                <th scope="col">Quantity of purchases</th>
                <th scope="col">Quantity of production</th>
                <th scope="col">Quantity of sales</th>
                <th scope="col">Common</th>
                <th scope="col">Salary</th>
                <th scope="col">Bonus</th>
                <th scope="col">General</th>
                <th scope="col">Issued</th>
                <th scope="col">Actions</th>
            </tr>
            </thead>
            <tbody>
            <#list salaries as salary>
                <tr>
                    <td>${salary.employee.fullName}</td>
                    <td>${salary.numberOfPurchase}</td>
                    <td>${salary.numberOfProduction}</td>
                    <td>${salary.numberOfSale}</td>
                    <td>${salary.common}</td>
                    <td>${salary.salary}</td>
                    <td>${salary.bonus}</td>
                    <td>${salary.general}</td>
                    <td>${salary.issued?string('Yes', 'No')}</td>
                    <#if salary.issued == false>
                        <td>
                            <a href="/salaries/${salary.id}/edit" class="btn btn-outline-warning me-2">Edit</a>
                        </td>
                    <#else>
                        <td>No Actions</td>
                    </#if>


                </tr>
            </#list>
            </tbody>
        </table>
    <#else>
        <p>No salaries to display.</p>
    </#if>

    <script>
        document.addEventListener('DOMContentLoaded', function () {
            document.querySelector('#issueButton').addEventListener('click', function (event) {
                event.preventDefault();

                // Получаем значение бюджета и общую сумму
                var budget = parseFloat('${budget}');
                var totalAmount = parseFloat('${totalAmount}');

                console.log("Budget "+ budget);
                console.log(totalAmount);

                if (budget >= totalAmount) {
                    document.querySelector('#issueForm').submit();
                } else {
                    alert('There is not enough budget for issuing');
                }
            });
        });
    </script>

</@main.layout>
