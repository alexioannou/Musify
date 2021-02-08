package musify

import groovy.json.JsonOutput

class MusifyRestController {

    def musifyRestService
    def musifyService

    def index() {

    }

    def getAllAlbums()
    {
        //example:  /albums
        def albums = musifyRestService.requestAllAlbums()
        def json = groovy.json.JsonOutput.toJson(albums)
        render json
    }

    def getAlbum()
    {
        //params:    albumId
        //example:   /albums/23
        def album = musifyRestService.requestAlbum(params.albumId.toInteger())
        def json = groovy.json.JsonOutput.toJson(album)
        render json
    }

    def getAllAlbumsOfGenre()
    {
        //params:    genreId
        //example:  /genres/3/albums
        def albums = musifyRestService.requestAllAlbumsOfGenre(params.genreId.toInteger())
        def json = groovy.json.JsonOutput.toJson(albums)
        render json
    }

    def getAlbumOfGenre()
    {
        //params:    genreId, albumId
        //example:  /genres/5/albums/12
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
        //params:    genreId
        //example:  /genres/7
        def genre = musifyRestService.requestGenre(params.genreId.toInteger())
        def json = groovy.json.JsonOutput.toJson(genre)
        render json
    }

    def getAllGenresOfAlbum()
    {
        //params:    albumId
        //example:  /albums/45/genres
        def genres = musifyRestService.requestAllGenresOfAlbum(params.albumId.toInteger())
        def json = groovy.json.JsonOutput.toJson(genres)
        render json
    }

    def getGenreOfAlbum()
    {
        //params:    albumId, genreId
        //example:  /albums/43/genres/8
        def genre = musifyRestService.requestGenreOfAlbum(params.albumId.toInteger(), params.genreId.toInteger())
        def json = groovy.json.JsonOutput.toJson(genre)
        render json
    }

    def postAlbum()
    {
        //body: {title:"albumTitle", artist:"albumArtist", genres:["genre1","genre2"]}
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

    def putAlbum()
    {
        def albumUpdated = JsonOutput.toJson(musifyRestService.updateAlbum(params.albumId.toInteger(), params.title, params.artist, params.genres))
        response.status = 200;
        render albumUpdated
    }

    def putGenre()
    {
        def genreUpdated = JsonOutput.toJson(musifyRestService.updateGenre(params.genreId.toInteger(), params.name))
        response.status = 200;
        render genreUpdated
    }

    def deleteAlbum()
    {
        def albumDeleted = JsonOutput.toJson(musifyRestService.deleteAlbum(params.albumId.toInteger()))
        response.status = 200;
        render albumDeleted+"\nYes I know 204 was the \"more correct\" Status Code here, but I actually sent back a response so I used 200 instead"
    }

    def deleteGenre()
    {
        def genreDeleted = JsonOutput.toJson(musifyRestService.deleteGenre(params.genreId.toInteger()))
        response.status = 200;
        render genreDeleted+"\nYes I know 204 was the \"more correct\" Status Code here, but I actually sent back a response so I used 200 instead"
    }

    def test() {
        render "Is this good enough of a response for ya?"
    }
}
