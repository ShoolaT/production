<#import "../layout.ftlh" as main>

<@main.layout>
    <div class="container mt-5">
        <h2>Update Raw Material</h2>

        <form action="/materials/${materialDto.id}/edit" method="post" class="needs-validation" novalidate>
            <div class="form-group">
                <label for="name">Name:</label>
                <input type="text" class="form-control" id="name" name="name" value="${materialDto.name}" required>
            </div>

            <div class="form-group">
                <label for="measurementId">Unit of Measurement:</label>
                <select class="form-control" id="measurementId" name="unitsOfMeasurement.id" required>
                    <#list measurements as measurement>
                        <option value="${measurement.id}"<#if measurement.id == materialDto.unitsOfMeasurement.id> selected</#if>>${measurement.name}</option>
                    </#list>
                </select>
            </div>

            <div class="form-group">
                <label for="quantity">Quantity:</label>
                <input type="text" class="form-control" id="quantity" name="quantity" value="${formattedQuantity}" readonly required>
            </div>

            <div class="form-group">
                <label for="amount">Amount:</label>
                <input type="text" class="form-control" id="amount" name="amount" value="${formattedAmount}" readonly required>
            </div>


            <div class="mt-2">
                <button type="submit" class="btn btn-primary">Update</button>
                <a href="/materials/all" class="btn btn-secondary mx-3">Cancel</a>
            </div>
        </form>
    </div>
</@main.layout>
