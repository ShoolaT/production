<#import "../layout.ftlh" as main>
<@main.layout>
    <section class="vh-100">
        <div class="container py-5 h-100">
            <div class="row d-flex align-items-center justify-content-center h-100">
                <div class="col-md-8 col-lg-7 col-xl-6">
                    <img src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-login-form/draw2.svg"
                         class="img-fluid" alt="Phone image">
                </div>
                <div class="col-md-7 col-lg-5 col-xl-5 offset-xl-1">
                    <form action="/auth/login" method="post" id="login-form">

                    <div class="form-outline mb-4">
                            <input type="email" name="username" id="form1Example13" required
                                   class="form-control form-control-lg"/>
                            <label class="form-label" for="form1Example13">Email address</label>
                        </div>

                        <div class="form-outline mb-4">
                            <input type="password" name="password" id="form1Example23" required
                                   class="form-control form-control-lg"/>
                            <label class="form-label" for="form1Example23">Password</label>
                        </div>

                        <button type="submit" class="btn btn-primary btn-lg btn-block">Sign in</button>
                    </form>
                </div>
            </div>
        </div>

        <script>
            document.getElementById('login-form').addEventListener('submit', onLoginHandler)

            function onLoginHandler(event) {
                let form = event.target
                let userFormData = new FormData(form)
                let user = Object.fromEntries(userFormData)
                saveUser(user)
            }
        </script>
    </section>
</@main.layout>