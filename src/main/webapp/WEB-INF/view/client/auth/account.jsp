<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
            <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
                <html lang="en">

                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>User Account - Laptopshop</title>

                    <!-- Google Web Fonts -->
                    <link rel="preconnect" href="https://fonts.googleapis.com">
                    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
                    <link
                        href="https://fonts.googleapis.com/css2?family=Open+Sans:wght@400;600&family=Raleway:wght@600;800&display=swap"
                        rel="stylesheet">

                    <!-- Icon Font Stylesheet -->
                    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.4/css/all.css" />
                    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.4.1/font/bootstrap-icons.css"
                        rel="stylesheet">

                    <!-- Libraries Stylesheet -->
                    <link href="/client/lib/lightbox/css/lightbox.min.css" rel="stylesheet">
                    <link href="/client/lib/owlcarousel/assets/owl.carousel.min.css" rel="stylesheet">

                    <!-- Latest compiled and minified CSS -->
                    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
                        rel="stylesheet">

                    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
                    <script>
                        $(document).ready(() => {
                            const avatarFile = $("#avatarFile");
                            avatarFile.change(function (e) {
                                const imgURL = URL.createObjectURL(e.target.files[0]);
                                $("#avatarPreview").attr("src", imgURL);
                                $("#avatarPreview").css({ "display": "block" });
                            });
                        });
                    </script>
                    <!-- Customized Bootstrap Stylesheet -->
                    <link href="/client/css/bootstrap.min.css" rel="stylesheet">

                    <!-- Template Stylesheet -->
                    <link href="/client/css/style.css" rel="stylesheet">

                </head>

                <body>

                    <!-- Spinner Start -->
                    <div id="spinner"
                        class="show w-100 vh-100 bg-white position-fixed translate-middle top-50 start-50  d-flex align-items-center justify-content-center">
                        <div class="spinner-grow text-primary" role="status"></div>
                    </div>
                    <!-- Spinner End -->

                    <jsp:include page="../layout/header.jsp" />


                    <!-- Viết -->
                    <div class="container-fluid px-4">
                        <h1 class="mt-4">Manage User</h1>
                        <ol class="breadcrumb mb-4">
                            <li class="breadcrumb-item active"><a href="/admin">Dashboard</a></li>
                            <li class="breadcrumb-item active">Dashboard</li>
                        </ol>
                        <div class="mt-5">
                            <div class="row">
                                <div class="col-md-6 col-12 mx-auto">
                                    <h3 class="text-center">Thông tin tài khoản</h3>
                                    <hr />
                                    <form:form action="/account" method="post" modelAttribute="currentUser" class="row"
                                        enctype="multipart/form-data">
                                        <!-- ID field (hidden) -->
                                        <div class="mb-3 col-12 col-md-6" style="display: none;">
                                            <label class="form-label">ID:</label>
                                            <form:input type="text" class="form-control" path="id" />
                                        </div>

                                        <!-- Email field (disabled) -->
                                        <div class="mb-3 col-12" style="display: none;">
                                            <label class="form-label">Email:</label>
                                            <form:input type="email" class="form-control" path="email"
                                                disabled="true" />
                                        </div>

                                        <!-- Avatar preview -->
                                        <div class="mb-3 col-12 d-flex justify-content-center align-items-center">
                                            <c:choose>
                                                <c:when test="${sessionScope.avatar != null}">
                                                    <img id="avatarPreview" src="/images/avatar/${sessionScope.avatar}"
                                                        style="max-height: 200px; display: block; margin-bottom: 10px; border-radius: 50%; overflow: hidden;" />
                                                </c:when>
                                                <c:otherwise>
                                                    <img id="avatarPreview" src="/images/avatar/noneAvatar.png"
                                                        style="max-height: 200px; display: block; margin-bottom: 10px; border-radius: 50%; overflow: hidden;" />
                                                </c:otherwise>
                                            </c:choose>
                                        </div>

                                        <!-- File upload -->
                                        <div class="mb-3 col-12 d-flex justify-content-center align-items-center">
                                            <input type="file" class="form-control" style="width: 200px;"
                                                id="avatarFile" accept=".png, .jpg, .jpeg" name="hoidanitFile" />
                                        </div>

                                        <!-- Full name field -->
                                        <div class="mb-3 col-12 col-md-6">
                                            <label class="form-label">Full name:</label>
                                            <form:input type="text" class="form-control" path="fullName"
                                                value="${sessionScope.fullName}" />
                                        </div>

                                        <!-- Phone field -->
                                        <div class="mb-3 col-12 col-md-6">
                                            <label class="form-label">Phone number:</label>
                                            <form:input type="text" class="form-control" path="phone"
                                                value="${sessionScope.user.phone}" />
                                        </div>

                                        <!-- Address field -->
                                        <div class="mb-3 col-12">
                                            <label class="form-label">Address:</label>
                                            <form:input type="text" class="form-control" path="address"
                                                value="${sessionScope.user.address}" />
                                        </div>

                                        <!-- Role field (hidden) -->
                                        <div class="mb-3 col-12 col-md-6" style="display: none;">
                                            <label class="form-label">Role:</label>
                                            <form:select class="form-select" path="role.name">
                                                <form:option value="ADMIN">ADMIN</form:option>
                                                <form:option value="USER">USER</form:option>
                                            </form:select>
                                        </div>

                                        <!-- Submit buttons -->
                                        <div class="mb-5 col-12">
                                            <button type="submit" class="btn btn-warning">SAVE CHANGES</button>
                                            <a href="/" class="btn btn-success ms-2">Back</a>
                                        </div>
                                    </form:form>
                                </div>
                            </div>
                        </div>
                    </div>

                    <jsp:include page="../layout/footer.jsp" />


                    <!-- Back to Top -->
                    <a href="#" class="btn btn-primary border-3 border-primary rounded-circle back-to-top"><i
                            class="fa fa-arrow-up"></i></a>


                    <!-- JavaScript Libraries -->
                    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
                    <script src="/client/lib/easing/easing.min.js"></script>
                    <script src="/client/lib/waypoints/waypoints.min.js"></script>
                    <script src="/client/lib/lightbox/js/lightbox.min.js"></script>
                    <script src="/client/lib/owlcarousel/owl.carousel.min.js"></script>

                    <!-- Template Javascript -->
                    <script src="/client/js/main.js"></script>
                    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"
                        crossorigin="anonymous"></script>
                    <script src="/js/scripts.js"></script>
                </body>

                </html>