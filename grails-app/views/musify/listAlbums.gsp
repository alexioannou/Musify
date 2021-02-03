<%--
  Created by IntelliJ IDEA.
  User: alex
  Date: 28/1/2021
  Time: 3:59 μ.μ.
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Album List</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'musify.css')}" type="text/css">
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
    <script>
        $(document).ready(function() {

            //Search
            $("#searchButton").click(function(event) {
                event.preventDefault();
                if($('#titleField').val() == "" && $('#artistField').val() == "" && $('#genreField').val() == "")
                {
                    alert("Please fill at least one field");
                    return;
                }
                let formData = {};
                formData.title = $('#titleField').val();
                formData.artist = $('#artistField').val();
                formData.genre = $('#genreField').val();
                let url = "${createLink(controller:'Musify', action:'search')}";
                $.ajax({url: url, data: formData, dataType: "json", success: function(result) {
                        $("#myTable").html("<tr><th class=tableColumnHeader>Artist</th><th class=tableColumnHeader>Title</th> <th class=tableColumnHeader>Genres</th> <th/> <th/> </tr>");
                        for(album of result)
                        {
                            let properGenres = album.genres.toString().split(',').join(', ');
                            console.log(properGenres);
                            let row = document.getElementById("myTable").insertRow(1);
                            row.insertCell(0).outerHTML = "<th><label>"+album.artist+"</label></th>";
                            row.insertCell(1).outerHTML = "<th><label>"+album.title+"</label></th>";
                            row.insertCell(2).outerHTML = "<th><label id=\"genres\">"+properGenres+"</label></th>";
                            row.insertCell(3).outerHTML = "<th><a href=/Musify/musify/edit/"+parseInt(album.id)+" class=editLink value="+album.id+">Edit</a></th>";
                            row.insertCell(4).outerHTML = "<th><a href=/Musify/musify/delete/"+parseInt(album.id)+" class=deleteLink value="+album.id+">Delete</a></th>";
                        }
                    }});
                $('#pageHeader').html("Search results for:<br/>Title: "+formData.title+", Artist: "+formData.artist+", Genre: "+formData.genre);
            });

            //Clear
            $("#clearButton").click(function(event) {
               event.preventDefault();
                let url = "${createLink(controller:'Musify', action:'search')}";
                $.ajax({url: url, dataType: "json", success: function(result) {
                        $("#myTable").html("<tr><th class=tableColumnHeader>Artist</th><th class=tableColumnHeader>Title</th> <th class=tableColumnHeader>Genres</th> <th/> <th/> </tr>");
                        for(album of result)
                        {
                            let properGenres = album.genres.toString().split(',').join(', ');
                            console.log(properGenres);
                            let row = document.getElementById("myTable").insertRow(1);
                            row.insertCell(0).outerHTML = "<th><label>"+album.artist+"</label></th>";
                            row.insertCell(1).outerHTML = "<th><label>"+album.title+"</label></th>";
                            row.insertCell(2).outerHTML = "<th><label id=\"genres\">"+properGenres+"</label></th>";
                            row.insertCell(3).outerHTML = "<th><a href=/Musify/musify/edit/"+parseInt(album.id)+" class=editLink value="+album.id+">Edit</a></th>";
                            row.insertCell(4).outerHTML = "<th><a href=/Musify/musify/delete/"+parseInt(album.id)+" class=deleteLink value="+album.id+">Delete</a></th>";
                        }
                    }});
                $('#titleField').val("");
                $('#artistField').val("");
                $('#genreField').val("");
                $('#pageHeader').html("These are all your albums");
            });
        });
    </script>
</head>

<body>
<h1 id="pageHeader">These are all your albums</h1>
    <div id="tableDiv">
        <table id="myTable" class="table table-striped table-bordered">
            <tr>
                <th class="tableColumnHeader">Artist</th>
                <th class="tableColumnHeader">Title</th>
                <th class="tableColumnHeader">Genres</th>
                <th/>
                <th/>
            </tr>
            <g:each in="${albums}" var="album">
                <tr>
                    <th><label>${album.artist}</label></th>
                    <th><label>${album.title}</label></th>
                    <th><label id="genres"><g:eachJoin in="${album.genres}" var="genre" delimiter=", ">${genre}</g:eachJoin></label></th>
                    <th><g:link class="editLink" value="${album.id}" action="edit" params="['id': album.id]">Edit</g:link></th>
                    <th><g:link class="deleteLink" value="${album.id}" action="delete" params="['id': album.id]">Delete</g:link></th>
                </tr>
            </g:each>
        </table>
    </div>
    <div id="formDiv">
        <form id="myForm" name="myForm" controller="Musify">
            <h1 id="formHeader">Search for albums</h1>
            <label class="formLabel" id="titleSearch" for="titleField">Title</label>
            <input class="form-control" type="text" id="titleField" name="title"/><br/>
            <label id="artistSearch" for="artistField">Artist </label>
            <input class="form-control" type="text" id="artistField" name="artist"/><br/>
            <label id="genreSearch" for="genreField">Genre </label>
            <input class="form-control" type="text" id="genreField" name="genre"/><br/>
            <g:actionSubmit id="searchButton" class="button" value="Search"/>
            <g:actionSubmit id="clearButton" class="button" value="Clear"/>
            <g:actionSubmit value="New..." action="add" class="button"/>
        </form>
    </div>
</body>
</html>