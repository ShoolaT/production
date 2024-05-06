<#import "../layout.ftlh" as main>

<@main.layout>
    <div class="container mt-5">
        <h2>Create Employee</h2>

        <form action="/employees/create" id="registrationForm" method="post" class="needs-validation">
            <div class="form-group">
                <label for="fullName">Full Name:</label>
                <input type="text" class="form-control" id="fullName" name="fullName" required>
            </div>

            <div class="form-group">
                <label for="positionId">Position:</label>
                <select class="form-control" id="positionId" name="position.id" required>
                    <#list positions as position>
                        <option value="${position.id}">${position.name}</option>
                    </#list>
                </select>
            </div>

            <div class="form-group">
                <label for="salary">Salary:</label>
                <input type="number" class="form-control" id="salary" name="salary" required>
            </div>

            <div class="form-group">
                <label for="address">Address:</label>
                <input type="text" class="form-control" id="address" name="address" required>
            </div>

            <div class="form-group">
                <label for="phoneNumber">Phone Number:</label>
                <input type="text" class="form-control" id="phoneNumber" name="phoneNumber">
            </div>

            <div class="form-group">
                <label for="email">Email:</label>
                <input type="email" class="form-control" id="email" name="email" required>
            </div>

            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" class="form-control" id="password" name="password" required>
                <p id="passwordError" class="text-danger" style="display: none;">Пароль
                    должен содержать хотя бы одну заглавную букву, одну цифру и быть не
                    менее 5 символов длиной.</p>
            </div>

            <div class="mt-2">
                <button type="submit" class="btn btn-primary">Create</button>
                <a href="/employees/all" class="btn btn-secondary mx-3">Cancel</a>
            </div>
        </form>
    </div>
    <script>
        const passwordInput = document.getElementById('password');
        const passwordError = document.getElementById('passwordError');

        function validatePassword() {
            const password = passwordInput.value;
            const uppercaseRegex = /[A-Z]/;
            const digitRegex = /\d/;

            if (password.length < 5 || !uppercaseRegex.test(password) || !digitRegex.test(password)) {
                passwordError.style.display = 'block';
                return false;
            }

            passwordError.style.display = 'none';
            return true;
        }

        const registrationForm = document.getElementById('registrationForm');
        registrationForm.addEventListener('submit', function (event) {
            if (!validatePassword()) {
                event.preventDefault();
            }
        });
    </script>
</@main.layout>
