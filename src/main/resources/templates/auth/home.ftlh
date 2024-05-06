<#import "../layout.ftlh" as main>

<@main.layout>
    <div class="container mt-5">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <h2 class="text-center">Home Page</h2>

                <div class="card mt-3">
                    <div class="card-body">
                        <h2 class="card-title text-center">Welcome ${email}!</h2>
                    </div>
                </div>
            </div>
        </div>
    </div>
</@main.layout>
