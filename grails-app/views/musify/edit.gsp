<%--
  Created by IntelliJ IDEA.
  User: alex
  Date: 1/2/2021
  Time: 9:17 π.μ.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Edit Album</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
    <style>

        h1 {
            padding: 50px 50px 50px 50px;
            font-size: 50px;
        }

        form {
            width: 25%;
            min-width: 350px;
        }

        .form-group {
            width: 100%;
            display: flex;
            justify-content: center;
            align-items: center;
        }

        #genresLabel {

        }

        #genres {
            min-height: 150px;
            margin-bottom: 70px;
        }
    </style>
</head>

<body>
    <div style="text-align:center; align-self:center; width:100%;">
        <h1>Edit your album</h1>
        <div class="form-group">
            <form>
                <div>
                    <label id="titleLabel" for="titleField">Title</label>
                    <span><input class="form-control" value="${album.title}" type="text" id="titleField" name="title"/></span><br/>
                    <label id="artistLabel" for="artistField">Artist </label>
                    <span><input class="form-control" value="${album.artist}" type="text" id="artistField" name="artist"/></span><br/>
                </div>
                <div>
                    <label for="genres" id="genresLabel">Genres</label><br/>
                    <select name="genres" id="genres" multiple>
                        <g:each in="${genres}" var="genre">
                            <option value=${genre.id}>${genre.name}</option>
                        </g:each>
                    </select>
                </div>
                <g:actionSubmit class="button" id="confirmButton" value="Confirm" params="['id': album.id]" action="update"/>
            </form>

        </div>
    </div>
</body>
</html>