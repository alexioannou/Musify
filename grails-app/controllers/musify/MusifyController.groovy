package musify

import grails.converters.JSON

class MusifyController {

    def musifyService

    def index() {
        redirect (action: "listAlbums")
    }

    def add() {
        def myStyles = musifyService.fetchAllStyles()
        [genres : myStyles]
    }

    def create() {
        musifyService.createAlbumServiceMethod(params.title, params.artist, params.genres)
        println params.title.getClass()
        redirect (action: "listAlbums")
    }

    def edit() {
        def myStyles = musifyService.fetchAllStyles()
        def myAlbum = musifyService.fetchSingleAlbum(params.id)
        [album: myAlbum, genres : myStyles]
    }

    def update() {
        musifyService.updateAlbumsServiceMethod(params.id, params.title, params.artist, params.genres)
        redirect (action: "listAlbums")
    }

    def listAlbums() {
        def myAlbums = musifyService.fetchAllAlbums()
        myAlbums.albumsResults.each{alb ->
            def albumGenres = []
            myAlbums.albumGenresResults.each {gen ->
                if(gen.albumid == alb.id)
                    albumGenres.add(gen.name)
            }
            alb.genres = albumGenres
        }
        [ albums : myAlbums.albumsResults]
    }

    def search() {
        def myAlbums
        def myJson = []
        if(params.title || params.artist || params.genre)
        {
            myAlbums = musifyService.searchAlbumsSearviceMethod(params.title, params.artist, params.genre)
            myAlbums.albumSearchResults.each{alb ->
                def albumGenres = []
                def albumObject = [:]
                albumObject.id = alb.id
                albumObject.title = alb.title
                albumObject.artist = alb.artist
                myAlbums.albumGenreSearchResults.each {gen ->
                    if(gen.albumid == alb.id)
                        albumGenres.add(gen.name)
                }
                alb.genres = albumGenres
                albumObject.genres = alb.genres
                myJson.add(albumObject)
            }
            response.setContentType("application/json")
            return render(myJson as JSON)
        }
        else
        {
            myAlbums = musifyService.fetchAllAlbums()
            myAlbums.albumsResults.each{alb ->
                def albumGenres = []
                def albumObject = [:]
                albumObject.id = alb.id
                albumObject.title = alb.title
                albumObject.artist = alb.artist
                myAlbums.albumGenresResults.each {gen ->
                    if(gen.albumid == alb.id)
                        albumGenres.add(gen.name)
                }
                alb.genres = albumGenres
                albumObject.genres = alb.genres
                myJson.add(albumObject)
            }
            response.setContentType("application/json")
            return render(myJson as JSON)
        }
    }

    def delete() {
        musifyService.deleteAlbum(params.id)
        redirect (action: "listAlbums")
    }

    def test() {

    }
}
