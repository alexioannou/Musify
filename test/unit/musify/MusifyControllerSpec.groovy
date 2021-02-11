package musify


import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import groovy.json.JsonOutput
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(MusifyController)
class MusifyControllerSpec extends Specification {

    def sampleTitle = "sampleTitle"
    def sampleArtist = "sampleArtist"
    def sampleGenre = "sampleGenre"
    def sampleGenres1 = [[id:1, name:"Genre1"], [id:2, name:"Genre2"], [id:3, name:"Genre3"]]
    def sampleGenres2 = [[id:4, name:"Genre4"], [id:5, name:"Genre5"]]
    def sampleAlbum1 = [id:1, title:"sampleTitle1", artist:"sampleArtist1", genres:sampleGenres1]
    def sampleAlbum2 = [id:2, title:"sampleTitle2", artist:"sampleArtist2", genres:sampleGenres2]
    
    def setup() {
        controller.musifyService = [
                fetchAllGenres: {
                    return sampleGenres1
                },

                createAlbum: { String title, String artist, def genres ->
                    //Note 1
                },

                fetchSingleAlbum: {int id ->
                    return [id:id, title:sampleTitle, artist:sampleArtist, genres: sampleGenres2]
                },

                updateAlbum: {int id, String title, String artist, def genres ->
                    //Note 1
                },

                listAlbums: {
                    return [sampleAlbum1, sampleAlbum2]
                },

                searchAlbumsAlongWithGenres: {String title, String artist, String genre ->
                    return [sampleAlbum1, sampleAlbum2]
                },

                deleteAlbum: {int id ->
                    //Note 1
                }
        ] as MusifyService
    }

    def testAdd() {
        when:
            def model = controller.add()
        then:
            assertEquals(sampleGenres1, model.genres)
    }

    def testCreate() {
        when:
            controller.create()
        then:
        assertEquals("/musify/listAlbums", response.redirectedUrl)
    }

    def testEdit() {
        given:
            controller.params.id = 12
        when:
            def model = controller.edit()
        then:
            assertEquals(sampleGenres1, model.genres)
            assertEquals(12, model.album.id)
            assertEquals(sampleTitle, model.album.title)
            assertEquals(sampleArtist, model.album.artist)
            assertEquals(sampleGenres2, model.album.genres)
    }

    def testUpdate() {
        given:
            controller.params.id = 14
            controller.params.title = sampleTitle
            controller.params.artist = sampleArtist
            controller.params.genres = sampleGenres1
        when:
            controller.update()
        then:
            assertEquals("/musify/listAlbums", response.redirectedUrl)
    }

    def testListAlbums() {
        when:
            def model = controller.listAlbums()
        then:
            //Note 3
            assertEquals([sampleAlbum1, sampleAlbum2], model.albums)
    }

    def testSearchWithParams() {
        given:
            controller.params.title = sampleTitle
            controller.params.artist = sampleArtist
            controller.params.genre = sampleGenre
        when:
            controller.search()
        then:
            assertEquals(JsonOutput.toJson([sampleAlbum1, sampleAlbum2]), response.text)
    }

    def testSearchWithoutParams() {
        when:
            controller.search()
        then:
            assertEquals(JsonOutput.toJson([sampleAlbum1, sampleAlbum2]), response.text)
    }

    def testDelete() {
        given:
            controller.params.id = 9
        when:
            controller.delete()
        then:
            assertEquals("/musify/listAlbums", response.redirectedUrl)
    }
}
