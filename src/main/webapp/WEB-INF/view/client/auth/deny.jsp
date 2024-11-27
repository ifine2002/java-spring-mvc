<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <!DOCTYPE html>
    <html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Access Deny</title>
        <link href="/client/css/cssdeny.css" rel="stylesheet" />
    </head>

    <body>
        <div class="container">
            <div class="component PC"></div>
            <div class="component connection">
                <div class="dot first"></div>
                <div class="dot second"></div>
                <div class="dot third"></div>
            </div>
            <div class="component server">
                <div class="slot"></div>
                <div class="slot"></div>
                <div class="button"></div>
                <div class="button"></div>
            </div>
            <button class="redirect " onclick="connect()"><a href="/" style="text-decoration: none;">Trở lại trang
                    chủ</a></button>
        </div>
        <script src="/client/js/jsdeny.js"></script>
    </body>

    </html>