<#import "../layout.ftlh" as main>

<@main.layout>
    <h2>Product Sales Report</h2>

    <form action="/productSales/all/report" method="get">
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
        <a href="/productSales/all/print?startDate=${startD}&endDate=${endD}">Export PDF</a>
    </#if>
    <div class="mt-3">
        <table class="table">
            <thead>
            <tr>
                <th scope="col">Product</th>
                <th scope="col">Quantity</th>
                <th scope="col">Amount</th>
                <th scope="col">Date</th>
                <th scope="col">Employee</th>
            </tr>
            </thead>
            <tbody>
            <#if productSales??>
                <#list productSales as sale>
                    <tr>
                        <td>${sale.product.name}</td>
                        <td>${sale.quantity}</td>
                        <td>${sale.amount}</td>
                        <td>${sale.date?string("yyyy-MM-dd")}</td>
                        <td>${sale.employee.fullName}</td>
                    </tr>
                </#list>
                <tr>
                    <td>Total:</td>
                    <td>${totalQuantity}</td>
                    <td>${totalAmount}</td>
                    <td></td>
                    <td></td>
                </tr>
            </#if>
            </tbody>
        </table>
    </div>
</@main.layout>
