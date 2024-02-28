<#import "../layout.ftlh" as main>

<@main.layout>
    <h2>Raw material List</h2>

    <table class="table mt-3">
        <thead>
        <tr>
            <th scope="col">Name</th>
            <th scope="col">Unit of Measurement</th>
            <th scope="col">Quantity</th>
            <th scope="col">Amount</th>
            <th scope="col">Actions</th>
        </tr>
        </thead>
        <tbody>
        <#list materials.content as material>
            <tr>
                <td>${material.name}</td>
                <td>${material.unitsOfMeasurement.name}</td>
                <td>${material.quantity}</td>
                <td>${material.amount}</td>
                <td>
                    <a href="/materials/${material.id}/edit" class="btn btn-outline-warning mx-2">Edit</a>
                    <button class="btn btn-outline-danger" onclick="confirmDelete(${material.id})">Delete</button>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>

    <div class="mt-3">
        <a href="/materials/create" class="btn btn-success">Add Raw material</a>
    </div>
    <script>
        function confirmDelete(materialId) {
            var confirmation = confirm("Are you sure you want to delete this raw material?");
            if (confirmation) {
                window.location.href = '/materials/' + materialId + '/delete/confirmed';
            }
        }
    </script>
</@main.layout>
