<#import "../layout.ftlh" as main>
<@main.layout>
    <h2>All Product Productions</h2>
    <a href="/productProductions/all/report" class="mt-2 btn btn-outline-success">Report Product Production</a>
    <div class="mt-3">
        <table class="table">
            <thead>
            <tr>
                <th scope="col">Product</th>
                <th scope="col">Quantity</th>
                <th scope="col">Date</th>
                <th scope="col">Employee</th>
            </tr>
            </thead>
            <tbody>
            <#list productions as production>
                <tr>
                    <td>${production.product.name}</td>
                    <td>${production.quantity}</td>
                    <td>${production.date?string("yyyy-MM-dd")}</td>
                    <td>${production.employee.fullName}</td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>

    <div class="mt-3">
        <#if roles?seq_contains('ADMIN') || roles?seq_contains('TECHNOLOGIST')>
            <a href="/productProductions/create" class="btn btn-success">Add Product Production</a>
        </#if>
    </div>
</@main.layout>
