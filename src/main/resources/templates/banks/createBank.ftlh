<#import "../layout.ftlh" as main>

<@main.layout>
    <div class="container mt-5">
        <h2>Create Credit</h2>

        <form id="createCreditForm" action="/credits/create" method="post" class="needs-validation">
            <div class="form-group">
                <label for="sum">Sum:</label>
                <input type="text" class="form-control" id="sum" name="sum" required>
            </div>

            <div class="form-group">
                <label for="month">Months:</label>
                <input type="text" class="form-control" id="month" name="month" required>
            </div>

            <div class="form-group">
                <label for="percent">Percent:</label>
                <input type="text" class="form-control" id="percent" name="percent" required>
            </div>

            <div class="form-group">
                <label for="fine">Fine:</label>
                <input type="text" class="form-control" id="fine" name="fine" required>
            </div>

            <div class="form-group">
                <label for="receiptDate">Receipt Date:</label>
                <input type="date" class="form-control" id="receiptDate" name="receiptDate" value="${.now?string("yyyy-MM-dd")}" />
            </div>
            <input type="hidden" class="form-control" id="isPaid" name="isPaid" value="false">

            <div class="mt-2">
                <button type="submit" class="btn btn-primary">Create</button>
                <a href="/credits/all" class="btn btn-secondary mx-3">Cancel</a>
            </div>
        </form>
    </div>
</@main.layout>