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
        def json = groovy.json.JsonOutput.toJson(albums)
        render json
    }

    def getAlbum()
    {
        //example:   /albums/$albumId
        def album = musifyRestService.requestAlbum(params.albumId.toInteger())
        def json = groovy.json.JsonOutput.toJson(album)
        render json
    }

    def getAllAlbumsOfGenre()
    {
        //example:  /genres/$genreId/albums
        def albums = musifyRestService.requestAllAlbumsOfGenre(params.genreId.toInteger())
        def json = groovy.json.JsonOutput.toJson(albums)
        render json
    }

    def getAlbumOfGenre()
    {
        //example:  /genres/$genreId/albums/$albumId
        def album = musifyRestService.requestAlbumOfGenre(params.genreId.toInteger(), params.albumId.toInteger())
        def json = groovy.json.JsonOutput.toJson(album)
        render json
    }

    def getAllGenres()
    {
        //example:  /genres
        def genres = musifyRestService.requestAllGenres()
        def json = groovy.json.JsonOutput.toJson(genres)
        render json
    }

    def getGenre()
    {
        //example:  /genres/$genreId
        def genre = musifyRestService.requestGenre(params.genreId.toInteger())
        def json = groovy.json.JsonOutput.toJson(genre)
        render json
    }

    def getAllGenresOfAlbum()
    {
        //example:  /albums/$albumId/genres
        def genres = musifyRestService.requestAllGenresOfAlbum(params.albumId.toInteger())
        def json = groovy.json.JsonOutput.toJson(genres)
        render json
    }

    def getGenreOfAlbum()
    {
        //example:  /albums/$albumId/genres/$genreId
        def genre = musifyRestService.requestGenreOfAlbum(params.albumId.toInteger(), params.genreId.toInteger())
        def json = groovy.json.JsonOutput.toJson(genre)
        render json
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
