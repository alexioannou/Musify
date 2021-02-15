package musify

import grails.transaction.Transactional
import groovy.sql.Sql

@Transactional
class MusifyService {

    def dataSource

    //-------------------------------------------------------------- CREATE --------------------------------------------------------------
    def createAlbum(String title, String artist, def genres)   //Creates an Album
    {
        genres = genres.getClass() == String ? [genres] : genres
        int newAlbumId = insertAlbum(title, artist)
        genres.each {gen ->
            addGenreToAlbum(newAlbumId, gen.toInteger())
        }
        return newAlbumId
    }

    def insertAlbum(String title, String artist)    //Inserts an Album in the Database
    {
        Sql sql = new Sql(dataSource)
        def nextIdResult = sql.firstRow("SELECT nextval('albums_id_sequence')")
        int newAlbumId = nextIdResult.nextval
        sql.executeInsert("INSERT INTO albums(id, title, artist) VALUES(${newAlbumId}, ${title}, ${artist})")
        return newAlbumId
    }

    //-------------------------------------------------------------- List --------------------------------------------------------------
    def listAlbums()   //Lists all Albums in the Database
    {
        def albumList = fetchAllAlbums()
        albumList.allAlbums.each{alb ->
            alb.genres = []
            albumList.allAlbumGenres.each {gen ->
                if(gen.albumid == alb.id)
                {
                    alb.genres.add([id:gen.id, name:gen.name])
                }
            }
        }
        return albumList.allAlbums
    }

    //-------------------------------------------------------------- SEARCH --------------------------------------------------------------
    def searchAlbumsAlongWithGenres(String title, String artist, String genre)   //Searches for Albums in the Database
    {
        def albums = fetchCorrectAlbums(title, artist, genre)
        albums.each{alb ->
            def genres = fetchGenresOfAlbum(alb.id)
            alb.genres = []
            genres.each {gen ->
                alb.genres.add(id:gen.id, name:gen.name)
            }
        }
        return albums
    }

    //-------------------------------------------------------------- UPDATE --------------------------------------------------------------
    def updateAlbum(int id, String title, String artist, def genres)   //Updates an Album in the database
    {
        genres = genres.getClass() == String ? [genres] : genres
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
        singleAlbumResult.genres = sql.rows("Select genreId as id, genres.name FROM genres, represents where genres.id = genreId AND albumid = ${id}")
        return singleAlbumResult
    }

    def fetchCorrectAlbums(String title, String artist, String genre)    //Fetches the correct Albums
    {
        Sql sql = new Sql(dataSource)
        return sql.rows("""SELECT DISTINCT albums.id, albums.title, albums.artist
                                                    FROM albums, represents, genres 
                                                    WHERE genres.id = genreId AND albums.id = albumid AND (albums.title = ${title} OR albums.artist = ${artist} OR genres.name = ${genre})""")
    }

    def fetchAllAlbums()    //Fetches all Albums from the Database
    {
        Sql sql = new Sql(dataSource)
        def albumsResults = sql.rows("Select * FROM albums")
        def albumGenresResults = sql.rows("Select albumid, genres.id, genres.name FROM represents, genres WHERE genres.id = genreId")
        return [allAlbums: albumsResults, allAlbumGenres: albumGenresResults]
    }

    def fetchGenresOfAlbum(int id)   //Fetches the Genres of an Album from the Database
    {
        Sql sql = new Sql(dataSource)
        sql.rows("Select albumid, genres.id, genres.name FROM represents, genres WHERE genreId = genres.id AND albumid = ${id}")
    }

    def fetchAllGenres()    //Fetches all Genres from the Database
    {
        Sql sql = new Sql(dataSource)
        def genresResults = sql.rows("Select * FROM genres")
        return genresResults
    }
}