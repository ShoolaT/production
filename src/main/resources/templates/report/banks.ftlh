<#import "../layout.ftlh" as main>

<@main.layout>
    <h2>Bank Report</h2>

    <form action="/credits/all/report" method="get">
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
        <a href="/credits/all/print?startDate=${startD}&endDate=${endD}">Export PDF</a>
    </#if>

    <div class="mt-3">
        <table class="table">
            <thead>
            <tr>
                <th scope="col">Receipt Date</th>
                <th scope="col">Sum</th>
                <th scope="col">Deadline (months)</th>
                <th scope="col">Annual percentage</th>
                <th scope="col">Fines</th>
            </tr>
            </thead>
            <tbody>
            <#if banks??>
                <#list banks as bank>
                    <tr>
                        <td>${bank.receiptDate?string("yyyy-MM-dd")}</td>
                        <td>${bank.sum}</td>
                        <td>${bank.month}</td>
                        <td>${bank.percent}</td>
                        <td>${bank.fine}</td>
                    </tr>
                </#list>
                <tr>
                    <td>Total:</td>
                    <td>${total}</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
            </#if>
            </tbody>
        </table>
    </div>
    
</@main.layout>
