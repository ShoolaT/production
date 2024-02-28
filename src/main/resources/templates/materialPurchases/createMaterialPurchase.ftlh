<#import "../layout.ftlh" as main>

<@main.layout>
    <div class="container mt-5">
        <h2>Create Raw Material Purchase</h2>

        <form id="createMaterialPurchaseForm" action="/materialPurchases/create" method="post" class="needs-validation">
            <div class="form-group">
                <label for="rawMaterialId">Raw Material:</label>
                <select class="form-control" id="rawMaterialId" name="rawMaterial.id" required>
                    <#list materials as material>
                        <option value="${material.id}" <#if rawMaterialId?exists && rawMaterialId.id == material.id>selected</#if>>${material.name}</option>
                    </#list>
                </select>

            </div>

            <div class="form-group">
                <label for="quantity">Quantity:</label>
                <input type="text" class="form-control" id="quantity" name="quantity" value="${quantity?if_exists}" required>
            </div>

            <div class="form-group">
                <label for="amount">Amount:</label>
                <input type="text" class="form-control" id="amount" name="amount" value="${amount?if_exists}" required>
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

            <input type="hidden" id="errorMessage" value="${error}" />

            <div class="mt-2" id="errorMessageContainer"></div>

            <div class="mt-2">
                <button type="submit" class="btn btn-primary">Create</button>
                <a href="/materialPurchases/all" class="btn btn-secondary mx-3">Cancel</a>
            </div>
        </form>
    </div>

    <script>
        var errorMessage = document.getElementById('errorMessage').value;

        if (errorMessage !== "") {
            var errorMessageContainer = document.getElementById('errorMessageContainer');
            var errorMessageElement = document.createElement('div');
            errorMessageElement.className = 'alert alert-danger';
            errorMessageElement.textContent = errorMessage;
            errorMessageContainer.appendChild(errorMessageElement);
        }
    </script>
</@main.layout>
