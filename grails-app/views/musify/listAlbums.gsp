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
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script>
        $(document).ready(function() {
            $("#searchButton").click(function(event) {
                event.preventDefault();
                console.log("DID IT WORK THO")
                if($('#titleField').val() == "" && $('#artistField').val() == "" && $('#genreField').val() == "")
                {
                    console.log("Ya need to type some criteria first, buddy");
                    return;
                }
                let myForm = document.getElementById('myForm');
                let formData = {};
                // $(myForm).find("input[name]").each(function (index, node) {
                //     formData[node.name] = node.value;
                // });
                formData.title = $('#titleField').val();
                formData.artist = $('#artistField').val();
                formData.genre = $('#genreField').val();
                console.log(formData);
                let url = "${createLink(controller:'Musify', action:'search')}";
                let data = {title: document.getElementById("titleField"), artist: document.getElementById("artistField"), genre: document.getElementById("genreField")}
                $.ajax({url: url, data: formData, success: function(result) {
                        $("#tableDiv").html(result);
                        console.log(result);
                    }});
            });

            $("#clearButton").click(function(event) {
               event.preventDefault();
            });
        });
    </script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
    <style>
    th {text-align: center;}

    #test {
        display: flex;
        flex-direction: column;
        justify-content: center;
        min-height: 140px;
        width: 100%;

        border-style: solid;
        border-color: #dddddd;
    }

    #addButton{
    }

    #searchedFor {
        text-align: center;
    }

    .form-group {
        text-align: center;
        width: 100%;
    }

    form {
        width: 50%;
        display: inline-block;
    }

    .form-control {

    }

    label {
        width: 10%;
        padding-top: 4px;
    }

    span {
        width: 100%;
        display: block;
        overflow: hidden;
        padding: 0 0 0 15px;
    }

    .button {
        margin: 0 20px;
        width: 100px;
    }

    #genres {
        width: 100%;
    }

    .tableCol {
        font-size: xx-large;
        text-decoration: underline;
    }

    #searchButton {
        margin-bottom: 5px;
    }
    </style>
</head>

<body onload="test()">
    <div id="test">
        <h1 id="searchedFor" data-criteria="${criteria}">
            Search results for <br/>Title: ${criteria.getAt(0)}, Artist: ${criteria.getAt(1)}, Genre: ${criteria.getAt(2)}
        </h1>
    </div>
    <div id="tableDiv">
        <table class="table table-striped table-bordered">
            <tr>
                <th class="tableCol">Artist</th>
                <th class="tableCol">Title</th>
                <th class="tableCol">Genres</th>
                <th/>
                <th/>

            </tr>
            <g:each in="${albums}" var="album">
                <tr>
                    <th>${album.artist}</th>
                    <th>${album.title}</th>
                    <th><label readonly id="genres"><g:eachJoin in="${album.genres}" var="genre" delimiter=", ">${genre}</g:eachJoin></label></th>
                    <th><g:link value="${album.id}" action="edit" params="['id': album.id]">Edit</g:link></th>
                    <th><g:link value="${album.id}" action="delete" params="['id': album.id]">Delete</g:link></th>
                </tr>
            </g:each>
        </table>
    </div>
    <br/><br/><br/>
    <div class="form-group">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
        <form id="myForm" name="myForm">
            <h1>Search for albums</h1>
            <label id="titleSearch" for="titleField">Title</label>
            <span><input class="form-control" type="text" id="titleField" name="title"/></span><br/>
            <label id="artistSearch" for="artistField">Artist </label>
            <span><input class="form-control" type="text" id="artistField" name="artist"/></span><br/>
            <label id="genreSearch" for="genreField">Genre </label>
            <span><input class="form-control" type="text" id="genreField" name="genre"/></span><br/>
            <button class="button" id="searchButton">Search</button>
        </form><br/>
        <g:link action="listAlbums">
            <input type="button" id="addButton" value="Clear" class="button"/>
        </g:link>
        <g:link action="add" id="testing">
            <input type="button" id="addButton" value="New..." class="button"/>
        </g:link>
    </div>
</body>
<script>
    function test() {
        if(document.getElementById("searchedFor").dataset.criteria == '[, , ]')
            document.getElementById("searchedFor").innerHTML = "These are all your albums"
    }



    function disablez() {
        document.getElementById("form").addEventListener("click", function(event){
            event.preventDefault()
        });
    }

    jQuery(function ($) {
        var $inputs = $('input[name=title], input[name=artist], input[name=genre]');
        $inputs.on('input', function () {
            // Set the required property of the other input to false if this input is not empty.
            $inputs.not(this).prop('required', !$(this).val().length);
        });
    });
</script>
</html>