package musify

import grails.test.mixin.TestFor
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(MusifyRestController)
class MusifyRestControllerSpec extends Specification {

    def sampleTitle = "sampleTitle"
    def sampleArtist = "sampleArtist"
    def sampleGenreName = "sampleGenre"
    def sampleGenre = [id:7, name:"SampleGenre7"]
    def sampleGenres1 = [[id:1, name:"Genre1"], [id:2, name:"Genre2"], [id:3, name:"Genre3"]]
    def sampleGenres2 = [[id:4, name:"Genre4"], [id:5, name:"Genre5"]]
    def sampleAlbum1 = [id:1, title:"sampleTitle1", artist:"sampleArtist1", genres:sampleGenres1]
    def sampleAlbum2 = [id:2, title:"sampleTitle2", artist:"sampleArtist2", genres:sampleGenres2]

    def setup() {
        controller.musifyRestService = [
                requestAllAlbums: {
                    return [sampleAlbum1, sampleAlbum2]
                },

                requestAlbum: {int id ->
                    return sampleAlbum1
                },

                requestAllAlbumsOfGenre: {int id ->
                    return [sampleAlbum1, sampleAlbum2]
                },

                requestAllGenres: {
                    return sampleGenres1
                },

                requestGenre: {int id ->
                    return sampleGenre
                },

                requestAllGenresOfAlbum: {int id ->
                    return sampleGenres1
                },

                createAlbum: {String title, String artist, def genres ->
                    return [id:5, title:title, artist:artist, genres:genres]
                },

                createGenre: {String name ->
                    return [id: 17, name:name]
                },

                updateAlbum: {int id, String title, String artist, def genres ->
                    return [id:id, title:title, artist:artist, genres:genres]
                },

                updateGenre: {int id, String name ->
                    return [id:id, name:name]
                },

                deleteAlbum: {int id ->
                    return [id:id, title:sampleTitle, artist:sampleArtist, genres:sampleGenres1]
                },

                deleteGenre: {int id ->
                    return [id:id, name:sampleGenreName]
                }
        ]
    }

    def testGetAllAlbums() {
        when:
            controller.getAllAlbums()
        then:
            assertEquals(JsonOutput.toJson([sampleAlbum1, sampleAlbum2]), response.text)
    }

    def testGetAlbum() {
        given:
            controller.params.albumId = 11
        when:
            controller.getAlbum()
        then:
            assertEquals(JsonOutput.toJson(sampleAlbum1), response.text)
    }

    def testGetAllAlbumsOfGenre() {
        given:
            controller.params.genreId = 28
        when:
            controller.getAllAlbumsOfGenre()
        then:
            assertEquals(JsonOutput.toJson([sampleAlbum1, sampleAlbum2]), response.text)
    }

    def testGetAllGenres() {
        when:
            controller.getAllGenres()
        then:
            assertEquals(JsonOutput.toJson(sampleGenres1), response.text)
    }

    def testGetGenre() {
        given:
            controller.params.genreId = 5
        when:
            controller.getGenre()
        then:
            assertEquals(JsonOutput.toJson(sampleGenre), response.text)
    }

    def testGetAllGenresOfAlbum() {
        given:
            controller.params.albumId = 56
        when:
            controller.getAllGenresOfAlbum()
        then:
            assertEquals(JsonOutput.toJson(sampleGenres1), response.text)
    }

    def testPostAlbum() {
        given:
            controller.params.title = sampleTitle
            controller.params.artist = sampleArtist
            controller.params.genres = sampleGenres1
        when:
            controller.postAlbum()
        then:
            def result = new JsonSlurper().parseText(response.text)
            assertEquals(201, response.getStatus())
            assertEquals(sampleTitle, result.title)
            assertEquals(sampleArtist, result.artist)
            assertEquals(sampleGenres1, result.genres)
    }

    def testPostGenre() {
        given:
            controller.params.name = sampleGenreName
        when:
            controller.postGenre()
        then:
            def result = new JsonSlurper().parseText(response.text)
            assertEquals(201, response.getStatus())
            assertEquals(sampleGenreName, result.name)
    }

    def testPutAlbum() {
        given:
            controller.params.albumId = 30
            controller.params.title = sampleTitle
            controller.params.artist = sampleArtist
            controller.params.genres = sampleGenres1
        when:
            controller.putAlbum()
        then:
            def result = new JsonSlurper().parseText(response.text)
            assertEquals(200, response.getStatus())
            assertEquals(30, result.id)
            assertEquals(sampleTitle, result.title)
            assertEquals(sampleArtist, result.artist)
            assertEquals(sampleGenres1, result.genres)
    }

    def testPutGenre() {
        given:
            controller.params.genreId = 120
            controller.params.name = sampleGenreName
        when:
            controller.putGenre()
        then:
            def result = new JsonSlurper().parseText(response.text)
            assertEquals(200, response.getStatus())
            assertEquals(120, result.id)
            assertEquals(sampleGenreName, result.name)
    }

    def testDeleteAlbum() {
        given:
            controller.params.albumId = 10
        when:
            controller.deleteAlbum()
        then:
            def result = new JsonSlurper().parseText(response.text)
            assertEquals(200, response.getStatus())
            assertEquals(10, result.id)
    }

    def testDeleteGenre() {
        given:
            controller.params.genreId = 20
        when:
            controller.deleteGenre()
        then:
            def result = new JsonSlurper().parseText(response.text)
            assertEquals(200, response.getStatus())
            assertEquals(20, result.id)
    }
}
