package musify

import grails.converters.JSON

class MusifyController {

    def musifyService

    def index() {
        redirect (action: "listAlbums")
    }

    def add() {
        def myStyles = musifyService.getStyles()
        [genres : myStyles]
    }

    def create() {
        musifyService.createAlbum(params.title, params.artist, params.genres)
        println params.title.getClass()
        redirect (action: "listAlbums")
    }

    def edit() {
        def myStyles = musifyService.getStyles()
        def myAlbum = musifyService.getSingleAlbum(params.id)
        [album: myAlbum, genres : myStyles]
    }

    def update() {
        musifyService.updateAlbum(params.id, params.title, params.artist, params.genres)
        redirect (action: "listAlbums")
    }

    def listAlbums() {
        def myAlbums = musifyService.getAlbums()
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
            myAlbums = musifyService.searchAlbums(params.title, params.artist, params.genre)
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
            myAlbums = musifyService.getAlbums()
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
