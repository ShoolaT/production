<#import "../layout.ftlh" as main>

<@main.layout>
    <div class="container mt-5">
        <h2>Create Product Production</h2>

        <form id="createProductProductionForm" action="/productProductions/create" method="post" class="needs-validation">
            <div class="form-group">
                <label for="productId">Product:</label>
                <select class="form-control" id="productId" name="product.id" required>
                    <#list products as product>
                        <option value="${product.id}" <#if productId?exists && productId.id == product.id>selected</#if>>${product.name}</option>
                    </#list>
                </select>
            </div>

            <div class="form-group">
                <label for="quantity">Quantity:</label>
                <input type="text" class="form-control" id="quantity" name="quantity" value="${quantity?if_exists}" required>
            </div>

            <div class="form-group">
                <label for="date">Date:</label>
                <input type="date" class="form-control" id="date" name="date" value="${.now?string("yyyy-MM-dd")}" />
            </div>

            <div class="form-group">
                <label for="employeeId">Employee:</label>
                <select class="form-control" id="employeeId" name="employee.id" required>
                    <#list employees as employee>
                        <option value="${employee.id}" <#if employeeId?exists && employeeId.id == employee.id>selected</#if>>${employee.fullName}</option>
                    </#list>
                </select>
            </div>

            <input type="hidden" id="errorMessageQuantity" value="${errorQuantity}" />

            <div class="mt-2" id="errorMessageContainer"></div>

            <div class="mt-2">
                <button type="submit" class="btn btn-primary">Create</button>
                <a href="/productProductions/all" class="btn btn-secondary mx-3">Cancel</a>
            </div>
        </form>
    </div>

    <script>
        var errorMessageQuantity = document.getElementById('errorMessageQuantity').value;

        if (errorMessageQuantity !== "") {
            var errorMessageContainer = document.getElementById('errorMessageContainer');
            var errorMessageElement = document.createElement('div');
            errorMessageElement.className = 'alert alert-danger';
            errorMessageElement.textContent = errorMessageQuantity;
            errorMessageContainer.appendChild(errorMessageElement);
        }
    </script>
</@main.layout>
