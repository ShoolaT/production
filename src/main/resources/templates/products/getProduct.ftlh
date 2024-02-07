<!-- products/getProduct.html -->

<#import "../layout.ftlh" as main>
<@main.layout>
    <div class="container mt-5">
        <h2>Product Details</h2>

        <div class="card">
            <div class="card-body">
                <h5 class="card-title">${product.name}</h5>
                <p class="card-text">
                    <strong>Unit of Measurement:</strong> ${product.unitsOfMeasurement.name}<br>
                    <strong>Quantity:</strong> ${product.quantity}<br>
                    <strong>Amount:</strong> ${product.amount}<br>
                </p>
            </div>
        </div>

        <div class="mt-3">
            <a href="/products/all" class="btn btn-primary">Back to Product List</a>

            <!-- Add Edit and Delete buttons -->
            <a href="/products/${product.id}/edit" class="btn btn-warning mx-2">Edit</a>
            <a href="/products/${product.id}/delete" class="btn btn-danger mt-auto">Delete</a>
        </div>
    </div>
</@main.layout>
