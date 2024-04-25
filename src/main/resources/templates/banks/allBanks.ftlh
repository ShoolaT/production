<#import "../layout.ftlh" as main>

<@main.layout>
    <h2>Bank</h2>

    <p class="mt-3 fs-3 text-success">Budget: ${budget}</p>
    <div class="mt-3">
        <table class="table">
            <thead>
            <tr>
                <th scope="col">Sum</th>
                <th scope="col">Deadline (months)</th>
                <th scope="col">Annual percentage</th>
                <th scope="col">Fines</th>
                <th scope="col">Receipt Date</th>
                <th scope="col">Actions</th>
            </tr>
            </thead>
            <tbody>
            <#list banks as bank>
                <tr>
                    <td>${bank.sum}</td>
                    <td>${bank.month}</td>
                    <td>${bank.percent}</td>
                    <td>${bank.fine}</td>
                    <td>${bank.receiptDate?string("yyyy-MM-dd")}</td>
                    <td>
                        <a href="/credits/${bank.id}/history" class="btn btn-success mx-2">Payment history</a>
                        <#if bank.paid == false>
                        <a href="/credits/pay/${bank.id}" class="btn btn-outline-warning mx-2">Credit pay</a>
                        </#if>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>

    <div class="mt-3">
        <a href="/credits/create" class="btn btn-success">Add credit</a>
    </div>
</@main.layout>
