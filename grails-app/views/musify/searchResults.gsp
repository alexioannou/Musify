<%--
  Created by IntelliJ IDEA.
  User: alex
  Date: 29/1/2021
  Time: 9:58 π.μ.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Search Results</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
    <style>
        th {text-align: center;}
        #searchedFor {
            padding-top: 15px;
            padding-bottom: 15px;
            text-align: center;
            background-color: #999999;
            color: #222222;
            left: 25%;
            width: 100%;
        }
    </style>
</head>

<body>
    <h1 id="searchedFor">Search results for Title: ${criteria.getAt(0)}      , Artist: ${criteria.getAt(1)}</h1>
    <table class="table table-striped table-bordered">
        <tr>
            <th>Artist</th>
            <th>Title</th>
        </tr>
        <g:each in="${albums}" var="album">
            <tr>
                <th>${album.artist}</th>
                <th>${album.title}</th>
            </tr>
        </g:each>
    </table>
%{--    <g:each in="${albums}" var="album">--}%
%{--        <label id="titleLabel">Title: </label>--}%
%{--        <label id="titleValue">${album.title}</label><br/>--}%
%{--        <label id="artistLabel">Artist: </label>--}%
%{--        <label id="artistValue">${album.artist}</label><br/>--}%
%{--    </g:each>--}%
    <br/><br/><br/>
    <h1>Search for albums</h1>
    <g:form action="search">
        <label id="titleSearch">Title </label>
        <input type="text" id="titleField" name="title"/><br/>
        <label id="artistSearch">Artist </label>
        <input type="text" id="artistField" name="artist"/><br/>
        <button type="submit" value="search">Search</button>
    </g:form> <br/>
    <g:link action="link">Got back</g:link>
</body>
</html>