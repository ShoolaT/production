<#import "../layout.ftlh" as main>

<@main.layout>
    <h2>All Product Sales</h2>

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
                    <td>${sale.cost}</td>
                    <td>${sale.date?string("yyyy-MM-dd")}</td>
                    <td>${sale.employee.fullName}</td>
<#--                    <td>-->
<#--&lt;#&ndash;                        <a href="/productSales/edit/${sale.id}" class="btn btn-outline-warning mx-2">Edit</a>&ndash;&gt;-->
<#--                        <button class="btn btn-outline-danger" onclick="confirmDelete(${sale.id})">Delete</button>-->
<#--                    </td>-->
                </tr>
            </#list>
            </tbody>
        </table>
    </div>

    <div class="mt-3">
        <a href="/productSales/create" class="btn btn-success">Add Product Sale</a>
    </div>

    <script>
        function confirmDelete(saleId) {
            var confirmation = confirm("Are you sure you want to delete this product sale?");
            if (confirmation) {
                window.location.href = '/productSales/'+ saleId+ '/delete/confirmed';
            }
        }
    </script>
</@main.layout>
