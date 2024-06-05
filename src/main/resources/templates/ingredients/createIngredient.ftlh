<#import "../layout.ftlh" as main>

<@main.layout>
    <div class="container mt-5">
        <h2>Create Ingredient</h2>

        <#if message?exists && message != "">
            <div id="message" class="alert alert-danger" role="alert">
                ${message}
            </div>
            <script>
                setTimeout(function(){
                    var messageDiv = document.getElementById("message");
                    if (messageDiv) {
                        messageDiv.style.display = "none";
                    }
                }, 10000); // 10 секунд
            </script>
        </#if>

        <form id="createIngredientForm" action="/ingredients/create" method="post" class="needs-validation">
            <div class="form-group">
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

            <input type="hidden" id="ingredientExists" name="ingredientExists" value="${ingredientExists?string('true', 'false')}"/>

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
                var messageDiv = document.createElement('div');
                messageDiv.className = 'alert alert-danger';
                messageDiv.role = 'alert';
                messageDiv.innerText = 'Such raw materials for this product already exist';
                document.body.insertBefore(messageDiv, document.body.firstChild);
                setTimeout(function() {
                    messageDiv.style.display = 'none';
                }, 10000);
            } else {
                document.getElementById('createIngredientForm').submit();
            }
        }
    </script>
</@main.layout>
