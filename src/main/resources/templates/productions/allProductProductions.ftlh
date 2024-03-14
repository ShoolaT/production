<#import "../layout.ftlh" as main>

<@main.layout>
    <h2>All Product Productions</h2>

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
        <a href="/productProductions/create" class="btn btn-success">Add Product Production</a>
    </div>

<#--    <script>-->
<#--        function confirmDelete(saleId) {-->
<#--            var confirmation = confirm("Are you sure you want to delete this product sale?");-->
<#--            if (confirmation) {-->
<#--                window.location.href = '/productSales/'+ saleId+ '/delete/confirmed';-->
<#--            }-->
<#--        }-->
<#--    </script>-->
</@main.layout>
