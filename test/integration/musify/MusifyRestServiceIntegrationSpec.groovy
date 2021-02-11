package musify

import grails.test.spock.IntegrationSpec
import groovy.json.JsonOutput
import org.junit.Assert

class MusifyRestServiceIntegrationSpec extends IntegrationSpec {

    def musifyRestService
    def musifyTestToolkitService
    def sampleTitle = "sampleTitle"
    def sampleArtist = "sampleArtist"
    def sampleGenreName = "sampleGenre"
    def sampleGenre1 = [id:12345, name:"GenOne"]
    def sampleGenre2 = [id:23456, name:"GenTwo"]
    def sampleGenre3 = [id:34567, name:"GenThree"]
    def sampleGenre4 = [id:45678, name:"GenFour"]
    def sampleGenre5 = [id:56789, name:"GenFive"]
    def sampleGenre6 = [id:67890, name:"GenSix"]
    def sampleGenreIds1 = [1, 2, 3]
    def sampleAlbum1 = [id:12345, title:"alb1", artist:"art1"]
    def sampleAlbum2 = [id:23456, title:"alb2", artist:"art2"]
    def sampleAlbumWithGenres1 = [id:1111111, title:"sampleTitle1", artist:"sampleArtist1", genres:[sampleGenre1, sampleGenre2]]
    def sampleAlbumWithGenres2 = [id:2222222, title:"sampleTitle2", artist:"sampleArtist2", genres:[sampleGenre3, sampleGenre4, sampleGenre5]]
    def sampleAlbumWithGenres3 = [id:3333333, title:"sampletitle3", artist:"sampleArtist3", genres:[sampleGenre6]]

    def setup() {
    }

    def cleanup() {
    }

    def testRequestAllAlbums() {
        given:
            musifyTestToolkitService.persistAlbum(sampleAlbum1)
            musifyTestToolkitService.persistAlbum(sampleAlbum2)
        when:
            def albums = musifyRestService.requestAllAlbums()
        then:
//            println JsonOutput.prettyPrint(JsonOutput.toJson(albums))
            assert albums.containsAll([sampleAlbum1, sampleAlbum2])
    }

    def testRequestAlbum() {
        given:
            musifyTestToolkitService.persistAlbum(sampleAlbum1)
        when:
            def album = musifyRestService.requestAlbum(sampleAlbum1.id)
        then:
//            println JsonOutput.prettyPrint(JsonOutput.toJson(album))
            Assert.assertEquals(sampleAlbum1, album)
    }

    def testRequestAllAlbumsOfGenre() {
        given:
            musifyTestToolkitService.persistAlbum(sampleAlbum1)
            musifyTestToolkitService.persistAlbum(sampleAlbum2)
            musifyTestToolkitService.persistGenre(sampleGenre1)
            musifyTestToolkitService.associateGenreWithAlbum(sampleAlbum1.id, sampleGenre1.id)
            musifyTestToolkitService.associateGenreWithAlbum(sampleAlbum2.id, sampleGenre1.id)
        when:
            def albums = musifyRestService.requestAllAlbumsOfGenre(sampleGenre1.id)
        then:
//            println JsonOutput.prettyPrint(JsonOutput.toJson(albums))
            assert albums.containsAll([sampleAlbum1, sampleAlbum2])
    }

    def testRequestAllGenres() {
        given:
            musifyTestToolkitService.persistGenre(sampleGenre1)
            musifyTestToolkitService.persistGenre(sampleGenre2)
        when:
            def genres = musifyRestService.requestAllGenres()
        then:
//            println JsonOutput.prettyPrint(JsonOutput.toJson(genres))
            assert genres.id.containsAll([sampleGenre1.id, sampleGenre2.id])
            assert genres.name.containsAll([sampleGenre1.name, sampleGenre2.name])
    }

    def testRequestGenre() {
        given:
            musifyTestToolkitService.persistGenre(sampleGenre1)
        when:
            def genre = musifyRestService.requestGenre(sampleGenre1.id)
        then:
//            println JsonOutput.prettyPrint(JsonOutput.toJson(genre))
            Assert.assertEquals(sampleGenre1.id, genre.id)
            Assert.assertEquals(sampleGenre1.name, genre.name)
    }

    def testRequestAllGenresOfAlbum() {
        given:
            musifyTestToolkitService.persistAlbumAlongWithGenres(sampleAlbumWithGenres1)
        when:
            def genres = musifyRestService.requestAllGenresOfAlbum(sampleAlbumWithGenres1.id)
        then:
//            println JsonOutput.prettyPrint(JsonOutput.toJson(genres))
            assert genres.containsAll(sampleGenre1, sampleGenre2)
    }

    def testCreateAlbum() {
        when:
            def createdAlbumId = musifyRestService.createAlbum(sampleTitle, sampleArtist, sampleGenreIds1).id
            def createdAlbum = musifyTestToolkitService.fetchAlbumAlongWithGenreIds(createdAlbumId)
        then:
//            println JsonOutput.prettyPrint(JsonOutput.toJson(createdAlbum))
            Assert.assertEquals(sampleTitle, createdAlbum.title)
            Assert.assertEquals(sampleArtist, createdAlbum.artist)
            Assert.assertEquals(sampleGenreIds1, createdAlbum.genres.sort())

    }

    def testUpdateAlbum() {
        given:
            musifyTestToolkitService.persistAlbumAlongWithGenres(sampleAlbumWithGenres1)
        when:
            musifyRestService.updateAlbum(sampleAlbumWithGenres1.id, sampleTitle, sampleArtist, sampleGenreIds1)
            def updatedAlbum = musifyTestToolkitService.fetchAlbumAlongWithGenreIds(sampleAlbumWithGenres1.id)
        then:
//            println JsonOutput.prettyPrint(JsonOutput.toJson(updatedAlbum))
            Assert.assertEquals(sampleTitle, updatedAlbum.title)
            Assert.assertEquals(sampleArtist, updatedAlbum.artist)
            Assert.assertEquals(sampleGenreIds1, updatedAlbum.genres.sort())
    }

    def testClearAlbumGenres() {
        given:
            musifyTestToolkitService.persistAlbumAlongWithGenres(sampleAlbumWithGenres1)
        when:
            musifyRestService.clearAlbumGenres(sampleAlbumWithGenres1.id)
            def clearedAlbum = musifyTestToolkitService.fetchAlbumAlongWithGenreIds(sampleAlbumWithGenres1.id)
        then:
//            println JsonOutput.prettyPrint(JsonOutput.toJson(clearedAlbum))
            Assert.assertEquals(sampleAlbumWithGenres1.title, clearedAlbum.title)
            Assert.assertEquals(sampleAlbumWithGenres1.artist, clearedAlbum.artist)
            assert clearedAlbum.genres.size() == 0
    }

    def testAddNewAlbumGenres() {
        given:
            musifyTestToolkitService.persistAlbum(sampleAlbum1)
            musifyTestToolkitService.persistGenre(sampleGenre1)
        when:
            musifyRestService.addNewAlbumGenre(sampleAlbum1.id, sampleGenre1.id)
            def albumUpdated = musifyTestToolkitService.fetchAlbumAlongWithGenreIds(sampleAlbum1.id)
        then:
//            println JsonOutput.prettyPrint(JsonOutput.toJson(albumUpdated))
            Assert.assertEquals(sampleAlbum1.title, albumUpdated.title)
            Assert.assertEquals(sampleAlbum1.artist, albumUpdated.artist)
            assert albumUpdated.genres.contains(sampleGenre1.id)
    }

    def testDeleteAlbum() {
        given:
            musifyTestToolkitService.persistAlbum(sampleAlbum1)
        when:
            musifyRestService.deleteAlbum(sampleAlbum1.id)
            def albumDeleted = musifyTestToolkitService.fetchAlbum(sampleAlbum1.id)
        then:
//            println JsonOutput.prettyPrint(JsonOutput.toJson(albumDeleted))
            Assert.assertEquals(null, albumDeleted)
    }

    def testCreateGenre() {
        when:
            def genreCreatedId = musifyRestService.createGenre(sampleGenreName).id
            def genreCreated = musifyTestToolkitService.fetchGenre(genreCreatedId)
        then:
//            println JsonOutput.prettyPrint(JsonOutput.toJson(genreCreated))
            Assert.assertEquals(genreCreatedId, genreCreated.id)
            Assert.assertEquals(sampleGenreName, genreCreated.name)

    }

    def testUpdateGenre() {
        given:
            musifyTestToolkitService.persistGenre(sampleGenre1)
        when:
            musifyRestService.updateGenre(sampleGenre1.id, sampleGenreName)
            def genreUpdated = musifyTestToolkitService.fetchGenre(sampleGenre1.id)
        then:
//            println JsonOutput.prettyPrint(JsonOutput.toJson(genreUpdated))
            Assert.assertEquals(sampleGenre1.id, genreUpdated.id)
            Assert.assertEquals(sampleGenreName, genreUpdated.name)
    }

    def testDeleteGenre() {
        given:
            musifyTestToolkitService.persistGenre(sampleGenre1)
        when:
            musifyRestService.deleteGenre(sampleGenre1.id)
            def genreDeleted = musifyTestToolkitService.fetchGenre(sampleGenre1.id)
        then:
//            println JsonOutput.prettyPrint(JsonOutput.toJson(genreDeleted))
            Assert.assertEquals(null, genreDeleted)
    }
}
