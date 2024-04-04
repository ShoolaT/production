<#import "../layout.ftlh" as main>

<@main.layout>
    <div class="container mt-5">
        <h2>Update Ingredient</h2>

        <div id="message" class="alert alert-info" role="alert">
            ${message!""}
        </div>

        <form action="/ingredients/${ingredient.id}/edit" method="post" class="needs-validation" novalidate>

            <div class="form-group">
                <label for="productId">Product:</label>
                <select class="form-control" id="productId" name="product.id" required>
                    <#list products as product>
                        <option value="${product.id}"<#if product.id == ingredient.product.id> selected</#if>>${product.name}</option>
                    </#list>
                </select>
            </div>

            <div class="form-group">
                <label for="rawMaterialId">Raw Material:</label>
                <select class="form-control" id="rawMaterialId" name="rawMaterial.id" required>
                    <#list materials as material>
                        <option value="${material.id}"<#if material.id == ingredient.rawMaterial.id> selected</#if>>${material.name}</option>
                    </#list>
                </select>
            </div>
<#--            <div class="form-group">-->
<#--                <label for="productId">Product:</label>-->
<#--                <input type="text" class="form-control" id="productName" name="productName" value="${ingredient.product.name}" readonly>-->
<#--                <input type="hidden" id="productId" name="product.id" value="${ingredient.product.id}">-->
<#--            </div>-->

<#--            <div class="form-group">-->
<#--                <label for="rawMaterialId">Raw Material:</label>-->
<#--                <input type="text" class="form-control" id="rawMaterialName" name="rawMaterialName" value="${ingredient.rawMaterial.name}" readonly>-->
<#--                <input type="hidden" id="rawMaterialId" name="rawMaterial.id" value="${ingredient.rawMaterial.id}">-->
<#--            </div>-->

            <div class="form-group">
                <label for="quantity">Quantity:</label>
                <input type="number" class="form-control" id="quantity" name="quantity" value="${ingredient.quantity}" required>
            </div>

            <div class="mt-2">
                <button type="submit" class="btn btn-primary">Update</button>
                <a href="/ingredients/all?productId=${selectedProductId}" class="btn btn-secondary mx-3">Cancel</a>
            </div>
        </form>

    </div>
    <script>
        setTimeout(function(){
            var messageDiv = document.getElementById("message");
            if (messageDiv) {
                messageDiv.style.display = "none";
            }
        }, 10000); // 15 секунд
    </script>
</@main.layout>
