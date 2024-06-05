<#import "../layout.ftlh" as main>

<@main.layout>
    <h2>All Product Sales</h2>
    <a href="/productSales/all/report" class="mt-2 btn btn-outline-success">Report Product Sales</a>
    <div class="mt-3">
        <p class="fs-3 text-success">Budget: ${budget} Percent: ${budgetPercent}%</p>
        <table class="table">
            <thead>
            <tr>
                <th scope="col">Product</th>
                <th scope="col">Quantity</th>
                <th scope="col">Amount</th>
                <th scope="col">Date</th>
                <th scope="col">Employee</th>
                <#--                <th scope="col">Actions</th>-->
            </tr>
            </thead>
            <tbody>
            <#list productSales as sale>
                <tr>
                    <td>${sale.product.name}</td>
                    <td>${sale.quantity}</td>
                    <td>${sale.amount}</td>
                    <td>${sale.date?string("yyyy-MM-dd")}</td>
                    <td>${sale.employee.fullName}</td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>

    <div class="mt-3">
        <#if roles?seq_contains('ADMIN') || roles?seq_contains('MANAGER')>
            <a href="/productSales/create" class="btn btn-success">Add Product Sale</a>
        </#if>
    </div>

    <script>
        function confirmDelete(saleId) {
            var confirmation = confirm("Are you sure you want to delete this product sale?");
            if (confirmation) {
                window.location.href = '/productSales/' + saleId + '/delete/confirmed';
            }
        }
    </script>
</@main.layout>
