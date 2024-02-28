<#macro layout>

    <!DOCTYPE html>
    <html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"
              name="viewport">
        <meta content="ie=edge" http-equiv="X-UA-Compatible">
        <title>Http server Index page</title>
        <!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
              integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC"
              crossorigin="anonymous">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
                integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
                crossorigin="anonymous"></script>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.3/font/bootstrap-icons.css">
        <link href="/static/styles.css">
        <script type="text/javascript" src="/static/js/main.js"></script>
    </head>

    <body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <a class="navbar-brand" href="/">Production</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item active">
                    <a class="nav-link" href="/employees/all">Employees<span class="sr-only"></span></a>
                </li>
            </ul>
            <ul class="navbar-nav mr-auto">
                <li class="nav-item active">
                    <a class="nav-link" href="/ingredients/all">Ingredients<span class="sr-only"></span></a>
                </li>
            </ul>
            <ul class="navbar-nav mr-auto">
                <li class="nav-item active">
                    <a class="nav-link" href="/products/all">Products<span class="sr-only"></span></a>
                </li>
            </ul>
            <ul class="navbar-nav mr-auto">
                <li class="nav-item active">
                    <a class="nav-link" href="/materials/all">Raw materials<span class="sr-only"></span></a>
                </li>
            </ul>
            <ul class="navbar-nav mr-auto">
                <li class="nav-item active">
                    <a class="nav-link" href="/materialPurchases/all">Raw material purchases<span class="sr-only"></span></a>
                </li>
            </ul>
            <ul class="navbar-nav mr-auto">
                <li class="nav-item active">
                    <a class="nav-link" href="/productSales/all">Product sales<span class="sr-only"></span></a>
                </li>
            </ul>
            <#--            <ul class="navbar-nav mr-auto">-->
            <#--                <li class="nav-item active">-->
            <#--                    <a class="nav-link" href="/projects/all">Projects<span class="sr-only"></span></a>-->
            <#--                </li>-->
            </ul>
        </div>
    </nav>

    <i class="m-5"></i>
    <div class="container border border-2 border-dark rounded-3 m-5" style="padding: 40px 55px;min-height: 300px">
        <#nested>
    </div>

    </body>
    </html>
</#macro>

