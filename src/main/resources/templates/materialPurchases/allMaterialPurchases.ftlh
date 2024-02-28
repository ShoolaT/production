<#import "../layout.ftlh" as main>

<@main.layout>
    <h2>Material Purchase List</h2>

    <div class="mt-3">
        <p class="fs-3 text-success">Budget: ${budget}</p>
        <table class="table">
            <thead>
            <tr>
                <th scope="col">Raw Material</th>
                <th scope="col">Quantity</th>
                <th scope="col">Amount (for 1)</th>
                <th scope="col">Employee</th>
                <th scope="col">Date</th>
                <th scope="col">Actions</th>
            </tr>
            </thead>
            <tbody>
            <#list materialPurchases as purchase>
                <tr>
                    <td>${purchase.rawMaterial.name}</td>
                    <td>${purchase.quantity}</td>
                    <td>${purchase.amount}</td>
                    <td>${purchase.employee.fullName}</td>
                    <td>${purchase.date?string("yyyy-MM-dd")}</td>
                    <td>
                        <a href="/materialPurchases/edit/${purchase.id}" class="btn btn-outline-warning mx-2">Edit</a>
                        <button class="btn btn-outline-danger" onclick="confirmDelete(${purchase.id})">Delete</button>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>

    <div class="mt-3">
        <a href="/materialPurchases/create" class="btn btn-success">Add Material Purchase</a>
    </div>

    <script>
        function confirmDelete(purchaseId) {
            var confirmation = confirm("Are you sure you want to delete this material purchase?");
            if (confirmation) {
                window.location.href = '/materialPurchases/'+ purchaseId+ '/delete/confirmed';
            }
        }
    </script>
</@main.layout>
