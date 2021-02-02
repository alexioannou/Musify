<%--
  Created by IntelliJ IDEA.
  User: alex
  Date: 1/2/2021
  Time: 9:17 π.μ.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Add Album</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'musify.css')}" type="text/css">
    <style>

        select {
            text-align: center;
            display: block;
            min-height: 150px;
            margin: 0 auto 50px auto;
        }
    </style>
</head>

<body>
    <h1 id="pageHeader">Add a new album</h1>
    <div id="formDiv">
        <form id="myForm" name="myForm" controller="Musify">
            <label class="formLabel" id="titleLabel" for="titleField">Title</label>
            <input class="form-control" type="text" id="titleField" name="title"/><br/>
            <label class="formLabel" id="artistLabel" for="artistField">Artist </label>
            <input class="form-control" type="text" id="artistField" name="artist"/><br/>
            <label for="genres" id="genresLabel">Genres</label><br/>
            <select name="genres" id="genres" multiple>
                <g:each in="${genres}" var="genre">
                    <option value=${genre.id}>${genre.name}</option>
                </g:each>
            </select>
            <g:actionSubmit class="button" id="createButton" value="Create" action="create"/>
            <g:actionSubmit value="Cancel" action="listAlbums" class="button"/>
        </form>
    </div>
</body>
</html>