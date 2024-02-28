<#import "../layout.ftlh" as main>

<@main.layout>
    <div class="container mt-5">
        <h2>Create Raw material</h2>

        <form id="createRawMaterialForm" action="/materials/create" method="post" class="needs-validation">
            <div class="form-group">
                <label for="name">Name:</label>
                <input type="text" class="form-control" id="name" name="name" required>
            </div>

            <div class="form-group">
                <label for="measurementId">Unit of Measurement:</label>
                <select class="form-control" id="measurementId" name="unitsOfMeasurement.id" required>
                    <#list measurements as measurement>
                        <option value="${measurement.id}">${measurement.name}</option>
                    </#list>
                </select>
            </div>

            <div class="form-group">
                <label for="quantity">Quantity:</label>
                <input type="number" class="form-control" id="quantity" name="quantity" readonly value="0" required>
            </div>

            <div class="form-group">
                <label for="amount">Amount (for 1):</label>
                <input type="number" class="form-control" id="amount" name="amount" readonly value="0" required>
            </div>


            <div class="mt-2">
                <button type="submit" class="btn btn-primary">Create</button>
                <a href="/materials/all" class="btn btn-secondary mx-3">Cancel</a>
            </div>
        </form>
    </div>


</@main.layout>
