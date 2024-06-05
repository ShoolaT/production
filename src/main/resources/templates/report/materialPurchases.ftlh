<#import "../layout.ftlh" as main>

<@main.layout>
    <h2>Material Purchase Report</h2>

    <form action="/materialPurchases/all/report" method="get">
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
        <a href="/materialPurchases/all/print?startDate=${startD}&endDate=${endD}">Export PDF</a>
    </#if>

    <div class="mt-3">
        <table class="table">
            <thead>
            <tr>
                <th scope="col">Raw Material</th>
                <th scope="col">Quantity</th>
                <th scope="col">Amount (for 1)</th>
                <th scope="col">Employee</th>
                <th scope="col">Date</th>
            </tr>
            </thead>
            <tbody>
            <#if materialPurchases??>
                <#list materialPurchases as purchase>
                    <tr>
                        <td>${purchase.rawMaterial.name}</td>
                        <td>${purchase.quantity}</td>
                        <td>${purchase.amount}</td>
                        <td>${purchase.employee.fullName}</td>
                        <td>${purchase.date?string("yyyy-MM-dd")}</td>
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
