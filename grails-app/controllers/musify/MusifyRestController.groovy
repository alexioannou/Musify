package musify

import groovy.json.JsonOutput

class MusifyRestController {

    def musifyRestService

    def index() {

    }
    //-------------------------------------------------------------- GET --------------------------------------------------------------
    def getAllAlbums()
    {
        //example:  /albums
        def albums = musifyRestService.requestAllAlbums()
        render JsonOutput.toJson(albums)
    }

    def getAlbum()
    {
        //example:   /albums/$albumId
        def album = musifyRestService.requestAlbum(params.albumId.toInteger())
        render JsonOutput.toJson(album)
    }

    def getAllAlbumsOfGenre()
    {
        //example:  /genres/$genreId/albums
        def albums = musifyRestService.requestAllAlbumsOfGenre(params.genreId.toInteger())
        render JsonOutput.toJson(albums)
    }

    def getAllGenres()
    {
        //example:  /genres
        def genres = musifyRestService.requestAllGenres()
        render JsonOutput.toJson(genres)
    }

    def getGenre()
    {
        //example:  /genres/$genreId
        def genre = musifyRestService.requestGenre(params.genreId.toInteger())
        render JsonOutput.toJson(genre)
    }

    def getAllGenresOfAlbum()
    {
        //example:  /albums/$albumId/genres
        def genres = musifyRestService.requestAllGenresOfAlbum(params.albumId.toInteger())
        render JsonOutput.toJson(genres)
    }

    //-------------------------------------------------------------- POST --------------------------------------------------------------
    def postAlbum()
    {
        //body: {title:"albumTitle", artist:"albumArtist", genres:["genreId1","genreId2"]}
        //example: /albums
        def albumCreated = JsonOutput.toJson(musifyRestService.createAlbum(params.title, params.artist, params.genres))
        response.status = 201;
        render albumCreated
    }

    def postGenre()
    {
        //body: {name:"genreName"}
        //example: /genres
        def genreCreated = JsonOutput.toJson(musifyRestService.createGenre(params.name))
        response.status = 201;
        render genreCreated
    }

    //-------------------------------------------------------------- PUT --------------------------------------------------------------
    def putAlbum()
    {
        //body: {title:"albumTitle", artist:"albumArtist", genres:["genreId1","genreId2"]}
        //example: /albums/$albumId
        def albumUpdated = JsonOutput.toJson(musifyRestService.updateAlbum(params.albumId.toInteger(), params.title, params.artist, params.genres))
        response.status = 200;
        render albumUpdated
    }

    def putGenre()
    {
        //body: {name:"genreName"}
        //example: /genres/$genreId
        def genreUpdated = JsonOutput.toJson(musifyRestService.updateGenre(params.genreId.toInteger(), params.name))
        response.status = 200;
        render genreUpdated
    }

    //-------------------------------------------------------------- DELETE --------------------------------------------------------------
    def deleteAlbum()
    {
        //example:  /albums/$albumId
        def albumDeleted = JsonOutput.toJson(musifyRestService.deleteAlbum(params.albumId.toInteger()))
        response.status = 200;
        render albumDeleted+"\nYes I know 204 was the \"more correct\" Status Code here, but I actually sent back a response so I used 200 instead"
    }

    def deleteGenre()
    {
        //example: /genres/$genreId
        def genreDeleted = JsonOutput.toJson(musifyRestService.deleteGenre(params.genreId.toInteger()))
        response.status = 200;
        render genreDeleted+"\nYes I know 204 was the \"more correct\" Status Code here, but I actually sent back a response so I used 200 instead"
    }

    def test() {
        render "Is this good enough of a response for ya?"
    }
}
