<#import "../layout.ftlh" as main>

<@main.layout>
    <h2>Product List</h2>

    <table class="table mt-3">
        <thead>
        <tr>
            <th scope="col">Product Name</th>
            <th scope="col">Unit of Measurement</th>
            <th scope="col">Quantity</th>
            <th scope="col">Amount</th>
            <th scope="col">Actions</th>
        </tr>
        </thead>
        <tbody>
        <#list products as product>
            <tr>
                <td>${product.name}</td>
                <td>${product.unitsOfMeasurement.name}</td>
                <td>${product.quantity}</td>
                <td>${product.amount}</td>
                <td>
                    <#if roles?seq_contains('ADMIN') || roles?seq_contains('MANAGER')>
                        <a href="/products/${product.id}/edit" class="btn btn-outline-warning mx-2">Edit</a>
                    </#if>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>

    <div class="mt-3">
        <#if roles?seq_contains('ADMIN')|| roles?seq_contains('MANAGER')>
        <a href="/products/create" class="btn btn-success">Add Product</a>
        </#if>
    </div>
    <script>
        function confirmDelete(productId) {
            var confirmation = confirm("Are you sure you want to delete this product?");
            if (confirmation) {
                window.location.href = '/products/' + productId + '/delete/confirmed';
            }
        }
    </script>
</@main.layout>
