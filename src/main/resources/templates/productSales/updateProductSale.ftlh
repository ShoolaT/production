<#import "../layout.ftlh" as main>
<@main.layout>
    <div class="container mt-5">
        <h2>Edit Product Sale</h2>

        <form action="/productSales/${productSaleDto.id}/edit" method="post" class="needs-validation" novalidate>
            <div class="form-group">
                <label for="productId">Product:</label>
                <select class="form-control" id="productId" name="product.id" required>
                    <#list products as product>
                        <option value="${product.id}"<#if product.id == productSaleDto.product.id> selected</#if>>${product.name}</option>
                    </#list>
                </select>
            </div>

            <div class="form-group">
                <label for="quantity">Quantity:</label>
                <input type="text" class="form-control" id="quantity" name="quantity" value="${formattedQuantity}" required>
            </div>

            <div class="form-group">
                <label for="amount">Amount:</label>
                <input type="text" class="form-control" id="amount" name="amount" value="${formattedCost}" required>
            </div>

            <div class="form-group">
                <label for="date">Date:</label>
                <input type="date" class="form-control" id="date" name="date" value="${productSaleDto.date?string("yyyy-MM-dd")}" />
            </div>


            <div class="form-group">
                <label for="employeeId">Employee:</label>
                <select class="form-control" id="employeeId" name="employee.id" required>
                    <#list employees as employee>
                        <option value="${employee.id}"<#if employee.id == productSaleDto.employee.id> selected</#if>>${employee.fullName}</option>
                    </#list>
                </select>
            </div>

            <div class="mt-2">
                <button type="submit" class="btn btn-primary">Update</button>
                <a href="/productSales/all" class="btn btn-secondary mx-3">Cancel</a>
            </div>
        </form>
    </div>
</@main.layout>