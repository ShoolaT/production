<!-- ingredients/createIngredient.html -->
<#import "../layout.ftlh" as main>

<@main.layout>
    <div class="container mt-5">
        <h2>Create Ingredient</h2>

        <form id="createIngredientForm" action="/ingredients/create" method="post" class="needs-validation">
            <div class="form-group">
                <div id="alertMessage" class="alert alert-warning" role="alert" style="display: none;">
                    Ingredient already exists for this product. Please choose a different combination.
                </div>
                <label for="productId">Product:</label>
                <select class="form-control" id="productId" name="product.id" required>
                    <#list products as product>
                        <option value="${product.id}"<#if selectedProduct?exists && product.id == selectedProduct.id> selected</#if>>${product.name}</option>
                    </#list>
                </select>
            </div>

            <div class="form-group">
                <label for="materialId">Raw Material:</label>
                <select class="form-control" id="materialId" name="rawMaterial.id" required>
                    <#list materials as material>
                        <option value="${material.id}">${material.name}</option>
                    </#list>
                </select>
            </div>

            <div class="form-group">
                <label for="quantity">Quantity:</label>
                <input type="text" class="form-control" id="quantity" name="quantity" required>
            </div>


            <input type="hidden" id="ingredientExists" name="ingredientExists"/>

            <div class="mt-2">
                <button type="button" class="btn btn-primary" onclick="confirmCreate()">Create</button>
                <a href="/ingredients/all" class="btn btn-secondary mx-3">Cancel</a>
            </div>
        </form>
    </div>
    <script>
        function confirmCreate() {
            var ingredientExists = document.getElementById('ingredientExists').value;

            if (ingredientExists === 'true') {
                window.location.href = '#exampleModal';
            } else {
                document.getElementById('createIngredientForm').submit();
            }
        }
    </script>


</@main.layout>
