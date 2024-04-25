<#import "../layout.ftlh" as main>

<@main.layout>
    <div class="container mt-5">
        <h2>Payment History</h2>

        <div id="errorMessageContainer"></div>

        <table class="table">
            <thead>
            <tr>
                <th scope="col">Payment Date</th>
                <th scope="col">Base Amount</th>
                <th scope="col">Percent Amount</th>
                <th scope="col">All Amount</th>
                <th scope="col">Overdue</th>
                <th scope="col">Fine Amount</th>
                <th scope="col">Total Amount</th>
                <th scope="col">Residue</th>
            </tr>
            </thead>
            <tbody>
            <tr>
                <td>
                    <form id="paymentForm" action="/credits/pay/${paymentHistory.bank.id}?payment_date=" method="get" class="needs-validation">
                        <div class="form-group">
                            <input type="date" class="form-control" id="payment_date" name="payment_date" value="${paymentHistory.paymentDate?string("yyyy-MM-dd")}">
                        </div>
                    </form>
                </td>
                <td>${paymentHistory.baseAmount}</td>
                <td>${paymentHistory.percentAmount}</td>
                <td>${paymentHistory.allAmount}</td>
                <td>${paymentHistory.overdue}</td>
                <td>${paymentHistory.fineAmount}</td>
                <td>${paymentHistory.totalAmount}</td>
                <td>${paymentHistory.residue}</td>
            </tr>
            </tbody>
        </table>
        <form id="paymentFormPost" action="/credits/pay/${paymentHistory.bank.id}" method="post" class="needs-validation">
            <input type="text" class="form-control" id="payment_date" name="payment_date" value="${paymentHistory.paymentDate?string("yyyy-MM-dd")}" hidden>
            <input type="hidden" name="baseAmount" value="${baseAmount}">
            <input type="hidden" name="percentAmount" value="${percentAmount}">
            <input type="hidden" name="allAmount" value="${allAmount}">
            <input type="hidden" name="overdue" value="${paymentHistory.overdue}">
            <input type="hidden" name="fineAmount" value="${fineAmount}">
            <input type="hidden" name="totalAmount" value="${totalAmount}">
            <input type="hidden" name="residue" value="${residue}">
            <input type="hidden" name="bankId" value="${paymentHistory.bank.id}">
            <button type="submit" class="btn btn-outline-warning mx-2">Credit pay</button>
        </form>
        <div class="mt-3">
            <a href="/credits/all" class="btn btn-success">All Credits Page</a>
        </div>

        <input type="hidden" id="errorMessage" value="${error}" />
    </div>

    <script>
        // Отправка формы при изменении даты
        document.getElementById("payment_date").addEventListener("change", function() {
            var paymentDate = document.getElementById("payment_date").value;

            document.getElementById("paymentForm").action = "/credits/pay/${paymentHistory.bank.id}?payment_date=" + paymentDate;
            document.getElementById("paymentForm").submit();
        });
        document.getElementById("paymentFormPost").addEventListener("submit", function(event) {
            var paymentDateField = document.createElement("input");
            paymentDateField.setAttribute("type", "hidden");
            paymentDateField.setAttribute("name", "paymentDate");

            // Преобразование даты в формат "yyyy-MM-dd"
            var paymentDate = document.getElementById("payment_date").value;
            paymentDateField.setAttribute("value", paymentDate);

            document.getElementById("paymentFormPost").appendChild(paymentDateField);
        });
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
