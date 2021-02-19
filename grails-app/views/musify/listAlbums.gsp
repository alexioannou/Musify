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
                            let properGenres = [];
                            for(genre of album.genres)
                            {
                                properGenres.push(genre.name)
                            }
                            properGenres = properGenres.toString().split(',').join(', ');
                            let row = document.getElementById("tableBody").insertRow(-1);
                            row.insertCell(0).outerHTML = "<th id=artistCell><label>"+album.artist+"</label></th>";
                            row.insertCell(1).outerHTML = "<th id=titleCell><label>"+album.title+"</label></th>";
                            row.insertCell(2).outerHTML = "<th id=genresCell><label id=\"genres\">"+properGenres+"</label></th>";
                            row.insertCell(3).outerHTML = "<th id=editCell><a href=/Musify/musify/edit/"+parseInt(album.id)+" class='btn btn-primary' value="+album.id+">Edit</a></th>";
                            row.insertCell(4).outerHTML = "<th id=deleteCell><a href=/Musify/musify/delete/"+parseInt(album.id)+" class='btn btn-danger' value="+album.id+">Delete</a></th>";
                        }
                    }});
                $('#pageHeader').html("Search results for:<br/>Title: "+formData.title+", Artist: "+formData.artist+", Genre: "+formData.genre);
            });

            //Clear
            $("#clearButton").click(function(event) {
                event.preventDefault();
                let url = "${createLink(controller:'Musify', action:'search')}";
                $.ajax({url: url, dataType: "json", success: function(result) {
                        // $("#myTable").html("<tr><th class=tableColumnHeader>Artist</th><th class=tableColumnHeader>Title</th> <th class=tableColumnHeader>Genres</th> <th/> <th/> </tr>");
                        $("#tableBody").html("")
                        for(album of result)
                        {
                            let properGenres = [];
                            for(genre of album.genres)
                            {
                                properGenres.push(genre.name)
                            }
                            properGenres = properGenres.toString().split(',').join(', ');
                            let row = document.getElementById("tableBody").insertRow(-1);
                            row.insertCell(0).outerHTML = "<th id=artistCell><label>"+album.artist+"</label></th>";
                            row.insertCell(1).outerHTML = "<th id=titleCell><label>"+album.title+"</label></th>";
                            row.insertCell(2).outerHTML = "<th id=genresCell><label id=\"genres\">"+properGenres+"</label></th>";
                            row.insertCell(3).outerHTML = "<th id=editCell><a href=/musify/edit/"+parseInt(album.id)+" class='btn btn-primary' value="+album.id+">Edit</a></th>";
                            row.insertCell(4).outerHTML = "<th id=deleteCell><a href=/musify/delete/"+parseInt(album.id)+" class='btn btn-danger' value="+album.id+">Delete</a></th>";
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
<g:applyLayout name="headerLayout"/>
<h1 id="pageHeader">These are all your albums</h1>
<div id="tableDiv">
    <table id="myTable" class="table table-striped table-bordered">
        <thead id="tableHead">
        <tr>
            <th class="tableColumnHeader">Artist</th>
            <th class="tableColumnHeader">Title</th>
            <th class="tableColumnHeader">Genres</th>
            <th/>
            <th/>
        </tr>
        </thead>
        <tbody id="tableBody">
        <g:each in="${albums}" var="album">
            <tr>
                <th id="artistCell"><label>${album.artist}</label></th>
                <th id="titleCell"><label>${album.title}</label></th>
                <th id="genresCell"><label id="genres"><g:eachJoin in="${album.genres}" var="genre" delimiter=", ">${genre.name}</g:eachJoin></label></th>
                <th id="editCell"><g:link class="btn btn-primary" value="${album.id}" action="edit" params="['id': album.id]">Edit</g:link></th>
                <th id="deleteCell"><g:link class="btn btn-danger" value="${album.id}" action="delete" params="['id': album.id]">Delete</g:link></th>
            </tr>
        </g:each>
        </tbody>
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
        <g:actionSubmit id="newButton" class="button" value="New..." action="add" onclick="this.form.action='${createLink(action:'add')}'"/>
    </form>
</div>
<g:applyLayout name="footerLayout"/>
</body>
</html>