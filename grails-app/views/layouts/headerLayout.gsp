<%--
  Created by IntelliJ IDEA.
  User: alex
  Date: 4/2/2021
  Time: 4:24 μ.μ.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title></title>
    <style>

    .display-1, .display-5 {
        font-family: Georgia;
    }

    .nav {
        width: 100%;
    }

    .nav .nav-item{
        padding: 0 10px 0 10px;
    }
    </style>
</head>

<body>
<div class="header, jumbotron">
    <h2 class="display-1">Musify</h2>
    <h2 class="display-5">A place to learn Grails and store your crappy albums while doing it</h2>
    <ul class="nav justify-content-center">
        <li class="nav-item">
            <a class="nav-link disabled">Dirtiest navigation bar ever: </a>
        </li>
        <li class="nav-item">
            <a class="btn btn-secondary" href="${createLink(controller:'Musify', action:'listAlbums')}">Album View</a>
        </li>
        <li class="nav-item">
            <a class="btn btn-secondary" href="${createLink(controller:'Musify', action:'add')}">Create Album</a>
        </li>
    </ul>
</div>
</body>
</html>