<table class="table table-striped table-bordered">
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