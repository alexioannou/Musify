package musify

import grails.converters.JSON

class MusifyController {

    def musifyService

    def index() {
        redirect (action: "listAlbums")
    }

    def add() {
        def myGenres = musifyService.fetchAllGenres()
        [genres : myGenres]
    }

    def create() {
        musifyService.createAlbum(params.title, params.artist, params.genres)
        redirect (action: "listAlbums")
    }

    def edit() {
        def allGenres = musifyService.fetchAllGenres()
        def myAlbum = musifyService.fetchSingleAlbum(params.id.toInteger())
        [album: myAlbum, genres : allGenres]
    }

    def update() {
        musifyService.updateAlbum(params.id.toInteger(), params.title, params.artist, params.genres)
        redirect (action: "listAlbums")
    }

    def listAlbums() {
        def myAlbums = musifyService.listAlbums()
        [ albums : myAlbums]
    }

    def search() {
        def myJson
        if(params.title || params.artist || params.genre)
            myJson = musifyService.searchAlbumsAsJSON(params.title, params.artist, params.genre)
        else
            myJson = musifyService.fetchAllAlbumsAsJSON()
        response.setContentType("application/json")
        render myJson as JSON
    }

    def delete() {
        musifyService.deleteAlbum(params.id.toInteger())
        redirect (action: "listAlbums")
    }

    def test() {
        render "*gesture* This is not the Action you are looking for"
    }
}
