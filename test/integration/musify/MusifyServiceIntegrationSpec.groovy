package musify

import grails.test.spock.IntegrationSpec
import groovy.json.JsonOutput

class MusifyServiceIntegrationSpec extends IntegrationSpec {
    
    def musifyService
    def musifyTestToolkitService
    def sampleTitle = "sampleTitle"
    def sampleArtist = "sampleArtist"
    def sampleGenre1 = [id:12345, name:"GenOne"]
    def sampleGenre2 = [id:23456, name:"GenTwo"]
    def sampleGenre3 = [id:34567, name:"GenThree"]
    def sampleGenre4 = [id:45678, name:"GenFour"]
    def sampleGenre5 = [id:56789, name:"GenFive"]
    def sampleGenre6 = [id:67890, name:"GenSix"]
    def sampleGenreIds1 = [1, 2, 3]
    def sampleAlbum1 = [id:1111111, title:"sampleTitle1", artist:"sampleArtist1", genres:[sampleGenre1, sampleGenre2]]
    def sampleAlbum2 = [id:2222222, title:"sampleTitle2", artist:"sampleArtist2", genres:[sampleGenre3, sampleGenre4, sampleGenre5]]
    def sampleAlbum3 = [id:3333333, title:"sampletitle3", artist:"sampleArtist3", genres:[sampleGenre6]]

    def setup() {
    
    }

    def cleanup() {
    }

    def testCreateAlbum() {
        when:
            int newAlbumId = musifyService.createAlbum(sampleTitle, sampleArtist, sampleGenreIds1)
            def createdAlbum = musifyTestToolkitService.fetchAlbumAlongWithGenreIds(newAlbumId)
        then:
//            println JsonOutput.prettyPrint(JsonOutput.toJson(createdAlbum))
            assert createdAlbum.title == sampleTitle
            assert createdAlbum.artist == sampleArtist
            assert createdAlbum.genres.sort() == sampleGenreIds1
    }
    
    def testListAlbums() {
        when:
            musifyTestToolkitService.persistAlbum(sampleAlbum1)
            musifyTestToolkitService.persistAlbum(sampleAlbum2)
            def albumList = musifyService.listAlbums()
        then:
//            println JsonOutput.prettyPrint(JsonOutput.toJson(albumList))
            assert albumList.contains(sampleAlbum1)
            assert albumList.contains(sampleAlbum2)
    }

    def testSearchAlbumsAlongWithGenres() {
        when:
            musifyTestToolkitService.persistAlbum(sampleAlbum1)
            musifyTestToolkitService.persistAlbum(sampleAlbum2)
            musifyTestToolkitService.persistAlbum(sampleAlbum3)
            def searchResults = musifyService.searchAlbumsAlongWithGenres(sampleAlbum1.title, sampleAlbum2.artist, sampleAlbum3.genres.name.first())
        then:
//            println JsonOutput.prettyPrint(JsonOutput.toJson(searchResults))
            assert searchResults.contains(searchResults.find{it.title == sampleAlbum1.title})
            assert searchResults.contains(searchResults.find{it.artist == sampleAlbum2.artist})
            assert searchResults.contains(searchResults.find{it.genres.name.contains(sampleAlbum3.genres.name.first())})
    }

    def testUpdateAlbum() {
        when:
            musifyTestToolkitService.persistAlbum(sampleAlbum1)
            musifyService.updateAlbum(sampleAlbum1.id, sampleTitle, sampleArtist, sampleGenreIds1)
            def updatedAlbum = musifyTestToolkitService.fetchAlbumAlongWithGenreIds(sampleAlbum1.id)
        then:
//            println JsonOutput.prettyPrint(JsonOutput.toJson(updatedAlbum))
            assert updatedAlbum.title == sampleTitle
            assert updatedAlbum.artist == sampleArtist
            assert updatedAlbum.genres.sort() == sampleGenreIds1
    }

    def testAddGenreToAlbum() {
        when:
            musifyTestToolkitService.persistAlbum(sampleAlbum1)
            musifyTestToolkitService.persistGenre(sampleGenre6)
            musifyService.addGenreToAlbum(sampleAlbum1.id, sampleGenre6.id)
            def albumGenreAdded = musifyTestToolkitService.fetchAlbumGenre(sampleAlbum1.id, sampleGenre6.id)
        then:
//            println JsonOutput.prettyPrint(JsonOutput.toJson(albumGenreAdded))
            assert albumGenreAdded.albumid == sampleAlbum1.id
            assert albumGenreAdded.genreid == sampleGenre6.id
    }

    def testDeleteAlbum() {
        when:
            musifyTestToolkitService.persistAlbum(sampleAlbum1)
            musifyService.deleteAlbum(sampleAlbum1.id)
            def albumDeleted = musifyTestToolkitService.fetchAlbum(sampleAlbum1.id)
        then:
//            println JsonOutput.prettyPrint(JsonOutput.toJson(albumDeleted))
            assert albumDeleted == null
    }

    def testClearAlbumGenres() {
        when:
            musifyTestToolkitService.persistAlbum(sampleAlbum1)
            musifyService.clearAlbumGenres(sampleAlbum1.id)
            def genresCleared = musifyTestToolkitService.fetchAllGenresOfAlbum(sampleAlbum1.id)
        then:
//            println JsonOutput.prettyPrint(JsonOutput.toJson(genresCleared))
            assert genresCleared.size() == 0
    }

    def testFetchSingleAlbum() {
        when:
            musifyTestToolkitService.persistAlbum(sampleAlbum1)
            def albumFetched = musifyService.fetchSingleAlbum(sampleAlbum1.id)
        then:
//            println JsonOutput.prettyPrint(JsonOutput.toJson(albumFetched))
            assert albumFetched == sampleAlbum1
    }

    def testFetchCorrectAlbums() {
        when:
            musifyTestToolkitService.persistAlbum(sampleAlbum1)
            musifyTestToolkitService.persistAlbum(sampleAlbum2)
            musifyTestToolkitService.persistAlbum(sampleAlbum3)
            def fetchedAlbums = musifyService.fetchCorrectAlbums(sampleAlbum1.title, sampleAlbum2.artist, sampleAlbum3.genres.name.first())
        then:
//            println JsonOutput.prettyPrint(JsonOutput.toJson(fetchedAlbums))
            assert fetchedAlbums.contains(fetchedAlbums.find{it.id == sampleAlbum1.id})
            assert fetchedAlbums.contains(fetchedAlbums.find{it.title == sampleAlbum1.title})
            assert fetchedAlbums.contains(fetchedAlbums.find{it.artist == sampleAlbum1.artist})
            assert fetchedAlbums.contains(fetchedAlbums.find{it.id == sampleAlbum2.id})
            assert fetchedAlbums.contains(fetchedAlbums.find{it.title == sampleAlbum2.title})
            assert fetchedAlbums.contains(fetchedAlbums.find{it.artist == sampleAlbum2.artist})
            assert fetchedAlbums.contains(fetchedAlbums.find{it.id == sampleAlbum3.id})
            assert fetchedAlbums.contains(fetchedAlbums.find{it.title == sampleAlbum3.title})
            assert fetchedAlbums.contains(fetchedAlbums.find{it.artist == sampleAlbum3.artist})
    }

    def testFetchAllAlbums() {
        when:
            musifyTestToolkitService.persistAlbum(sampleAlbum1)
            musifyTestToolkitService.persistAlbum(sampleAlbum2)
            def fetchedAlbums = musifyService.fetchAllAlbums()
        then:
//            println JsonOutput.prettyPrint(JsonOutput.toJson(fetchedAlbums))
            assert fetchedAlbums.allAlbums.id.containsAll([sampleAlbum1.id, sampleAlbum2.id])
            assert fetchedAlbums.allAlbums.title.containsAll([sampleAlbum1.title, sampleAlbum2.title])
            assert fetchedAlbums.allAlbums.artist.containsAll([sampleAlbum1.artist, sampleAlbum2.artist])
            assert fetchedAlbums.allAlbumGenres.id.containsAll((sampleAlbum1.genres.id+sampleAlbum2.genres.id).toArray())
            assert fetchedAlbums.allAlbumGenres.name.containsAll((sampleAlbum1.genres.name+sampleAlbum2.genres.name).toArray())
    }

    def testFetchGenresOfAlbum() {
        when:
            musifyTestToolkitService.persistAlbum(sampleAlbum1)
            def albumGenres = musifyService.fetchGenresOfAlbum(sampleAlbum1.id)
        then:
//            println JsonOutput.prettyPrint(JsonOutput.toJson(albumGenres))
            albumGenres.albumid.each{albumid ->
                assert albumid == sampleAlbum1.id
            }
            assert albumGenres.id.sort() == sampleAlbum1.genres.id
            assert albumGenres.name.sort() == sampleAlbum1.genres.name
    }

    def testFetchAllGenres() {
        when:
            musifyTestToolkitService.persistGenre(sampleGenre1)
            musifyTestToolkitService.persistGenre(sampleGenre2)
            musifyTestToolkitService.persistGenre(sampleGenre3)
            def fetchedGenres = musifyService.fetchAllGenres()
        then:
//            println JsonOutput.prettyPrint(JsonOutput.toJson(fetchedGenres))
            assert fetchedGenres.contains(sampleGenre1)
            assert fetchedGenres.contains(sampleGenre2)
            assert fetchedGenres.contains(sampleGenre3)
    }
}
