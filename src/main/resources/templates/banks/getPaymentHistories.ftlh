<#import "../layout.ftlh" as main>

<@main.layout>
    <h2>Payment History</h2>

    <div class="mt-3">
        <table class="table">
            <thead>
            <tr>
                <th scope="col">Payment Date</th>
                <th scope="col">Base Amount</th>
                <th scope="col">Percent Amount</th>
                <th scope="col">All Amount</th>
                <th scope="col">Overdue</th>
                <th scope="col">Fine Amount</th>
                <th scope="col">Total Amount</th>
                <th scope="col">Residue</th>
            </tr>
            </thead>
            <tbody>
            <#if paymentHistories??>
            <#list paymentHistories as paymentHistory>
                <tr>
                    <td>${paymentHistory.paymentDate?string("yyyy-MM-dd")}</td>
                    <td>${paymentHistory.baseAmount}</td>
                    <td>${paymentHistory.percentAmount}</td>
                    <td>${paymentHistory.allAmount}</td>
                    <td>${paymentHistory.overdue}</td>
                    <td>${paymentHistory.fineAmount}</td>
                    <td>${paymentHistory.totalAmount}</td>
                    <td>${paymentHistory.residue}</td>
                </tr>
            </#list>
            <#else>
            </#if>
            </tbody>
        </table>
        <#if totalAmount??>
            <h3 class="text-success">Total amount: ${totalAmount}</h3>
        </#if>



        <div class="mt-3">
            <a href="/credits/all" class="btn btn-success">All Credits Page</a>
        </div>
    </div>
</@main.layout>
