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
        def myAlbums = musifyService.listAlbumsServiceMethod()
        [ albums : myAlbums]
    }

    def search() {
        def myJson
        if(params.title || params.artist || params.genre)
            myJson = musifyService.searchAlbumsAsJSON(params.title, params.artist, params.genre)
        else
            myJson = musifyService.fetchAllAlbumsAsJSON()
        response.setContentType("application/json")
        return render(myJson as JSON)
    }

    def delete() {
        musifyService.deleteAlbum(params.id)
        redirect (action: "listAlbums")
    }

    def test() {

    }
}
