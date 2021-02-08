package musify

import grails.transaction.Transactional
import groovy.sql.Sql

@Transactional
class MusifyService {

    def dataSource

    //-------------------------------------------------------------- CREATE --------------------------------------------------------------
    def createAlbum(String title, String artist, def genres)   //Creates an Album in the Database
    {
        Sql sql = new Sql(dataSource)
        def nextIdResult = sql.firstRow("SELECT nextval('albums_id_sequence')")
        int newAlbumId = nextIdResult.nextval
        sql.execute("INSERT INTO albums VALUES(${newAlbumId}, ${title}, ${artist})")
        genres.each {gen ->
            sql.execute("INSERT INTO represents VALUES(${newAlbumId}, ${gen.toInteger()})")
        }
    }

    //-------------------------------------------------------------- List --------------------------------------------------------------
    def listAlbums()   //Lists all Albums in the Database
    {
        def albumList = fetchAllAlbums()
        albumList.albumsResults.each{alb ->
            def albumGenres = []
            albumList.albumGenresResults.each {gen ->
                if(gen.albumid == alb.id)
                    albumGenres.add(gen.name)
            }
            alb.genres = albumGenres
        }
        return albumList.albumsResults
    }

    //-------------------------------------------------------------- SEARCH --------------------------------------------------------------
    def searchAlbumsAsJSON(String title, String artist, String genre)   //Searches for Albums in the Database
    {
        def albumSearchResults = searchAlbums(title, artist, genre)
        def albumGenreSearchResults = searchGenresOfAlbums(title, artist, genre)
        def myJson = []
        def albumList = [albumSearchResults: albumSearchResults, albumGenreSearchResults: albumGenreSearchResults]
        albumList.albumSearchResults.each{alb ->
            def albumGenres = []
            def albumObject = [:]
            albumObject.id = alb.id
            albumObject.title = alb.title
            albumObject.artist = alb.artist
            albumList.albumGenreSearchResults.each {gen ->
                if(gen.albumid == alb.id)
                    albumGenres.add(gen.name)
            }
            albumObject.genres = albumGenres
            myJson.add(albumObject)
        }
        return myJson
    }

    private def searchAlbums(String title, String artist, String genre)    //Fetches the correct Albums
    {
        Sql sql = new Sql(dataSource)
        return sql.rows("""SELECT DISTINCT albums.id,albums.title, albums.artist
                                                    FROM albums, represents, genres 
                                                    WHERE genres.id = genreId AND albums.id = albumid AND (albums.title = ${title} OR albums.artist = ${artist} OR genres.name = ${genre})""")
    }

    private def searchGenresOfAlbums(String title, String artist, String genre)    //Fetches all the Genres that correspond to the correct albums
    {
        Sql sql = new Sql(dataSource)
        return sql.rows("""SELECT correctAlbums.albumid, genres.name 
                                                        FROM represents, genres, (SELECT DISTINCT albumid 
                                                                FROM albums, represents, genres 
                                                                WHERE genres.id = genreId AND albums.id = albumid AND (albums.title = ${title} OR albums.artist = ${artist} OR genres.name = ${genre})) AS correctAlbums
                                                        WHERE represents.genreId = genres.id AND represents.albumid = correctAlbums.albumid""")
    }
    //def searchAlbumsSearviceMethod(String title, String artist, String genre)   //Searches for Albums in the Database
    //{
    //    def albumSearchResults = searchAlbumsGetAlbums(title, artist, genre)
    //    def albumGenreSearchResults = searchAlbumsGetGenres(title, artist, genre)
    //    return [albumSearchResults: albumSearchResults, albumGenreSearchResults: albumGenreSearchResults]
    //}

    //-------------------------------------------------------------- UPDATE --------------------------------------------------------------
    def updateAlbum(int id, String title, String artist, def genres)   //Updates an Album in the database
    {
        clearAlbumGenres(id)
        genres.each{ gen ->
            addGenreToAlbum(id, gen.toInteger())
        }
        Sql sql = new Sql(dataSource)
        sql.execute("UPDATE albums SET title = ${title}, artist = ${artist} WHERE id = ${id}")
    }

    def addGenreToAlbum(int albumId, int genreId)   //Adds Genres to the Album
    {
        Sql sql = new Sql(dataSource)
        sql.execute("INSERT INTO represents VALUES(${albumId}, ${genreId})")
    }

    //-------------------------------------------------------------- DELETE --------------------------------------------------------------
    def deleteAlbum(int id)  //Deletes an Album from the Database
    {
        Sql sql = new Sql(dataSource)
        sql.execute("DELETE FROM albums WHERE id = ${id}")
    }

    def clearAlbumGenres(int id)
    {
        Sql sql = new Sql(dataSource)
        sql.execute("DELETE FROM represents WHERE albumId = ${id}")
    }

    //-------------------------------------------------------------- FETCH --------------------------------------------------------------
    def fetchSingleAlbum(int id) //Fetches an Album from the Database
    {
        Sql sql = new Sql(dataSource)
        def singleAlbumResult = sql.firstRow("Select * FROM albums where id = ${id}")
        singleAlbumResult.genres = sql.rows("Select genreId as id FROM genres, represents where genres.id = genreId AND albumid = ${id}")
        return singleAlbumResult
    }

    def fetchAllAlbums()    //Fetches all Albums from the Database
    {
        Sql sql = new Sql(dataSource)
        def albumsResults = sql.rows("Select * FROM albums")
        def albumGenresResults = sql.rows("Select albumid, genres.name FROM represents, genres WHERE genres.id = genreId")
        return [albumsResults: albumsResults, albumGenresResults: albumGenresResults]
    }

    def fetchAllAlbumsAsJSON()
    {
        def albumList = fetchAllAlbums()
        def myJson = []
        albumList.albumsResults.each{alb ->
            def albumGenres = []
            def albumObject = [:]
            albumObject.id = alb.id
            albumObject.title = alb.title
            albumObject.artist = alb.artist
            albumList.albumGenresResults.each {gen ->
                if(gen.albumid == alb.id)
                    albumGenres.add(gen.name)
            }
            albumObject.genres = albumGenres
            myJson.add(albumObject)
        }
        return myJson
    }

    def fetchGenresOfAlbum(int id)   //Fetches the Genres of an Album from the Database
    {
        Sql sql = new Sql(dataSource)
        sql.rows("Select genreId FROM represents WHERE albumid = ${id}")
    }

    def fetchAllGenres()    //Fetches all Genres from the Database
    {
        Sql sql = new Sql(dataSource)
        def genresResults = sql.rows("Select * FROM genres")
        return genresResults
    }
}