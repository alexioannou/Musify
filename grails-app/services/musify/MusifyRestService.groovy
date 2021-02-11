package musify

import grails.transaction.Transactional
import groovy.sql.Sql

@Transactional
class MusifyRestService {

    def dataSource

    def requestAllAlbums()   //Lists all Albums in the Database
    {
        Sql sql = new Sql(dataSource)
        return sql.rows("Select * FROM albums")
    }

    def requestAlbum(int albumId)
    {
        Sql sql = new Sql(dataSource)
        return sql.firstRow("Select * FROM albums WHERE id = ${albumId}")
    }

    def requestAllAlbumsOfGenre(int genreId)
    {
        Sql sql = new Sql(dataSource)
        return sql.rows("SELECT id, title, artist FROM albums, represents WHERE albums.id = albumId AND genreId = ${genreId}")
    }

    def requestAllGenres()
    {
        Sql sql = new Sql(dataSource)
        return sql.rows("SELECT * FROM genres")
    }

    def requestGenre(int genreId)
    {
        Sql sql = new Sql(dataSource)
        return sql.firstRow("Select * FROM genres WHERE id=${genreId}")
    }

    def requestAllGenresOfAlbum(int albumId)
    {
        Sql sql = new Sql(dataSource)
        return sql.rows("SELECT id, name FROM genres, represents WHERE genres.id = genreId AND albumId = ${albumId}")
    }

    def createAlbum(String title, String artist, def genres)
    {
        Sql sql = new Sql(dataSource)
        def nextIdResult = sql.firstRow("SELECT nextval('albums_id_sequence')")
        int newAlbumId = nextIdResult.nextval
        def albumCreated = sql.executeInsert("INSERT INTO albums(id, title, artist) VALUES(${newAlbumId}, ${title}, ${artist})",
                ["id", "title", "artist"])
        genres.each {gen ->
            sql.execute("INSERT INTO represents(albumId, genreId) VALUES(${newAlbumId}, ${gen.toInteger()})")
        }
        return albumCreated
    }

    def updateAlbum(int id, String title, String artist, def genres)
    {
        clearAlbumGenres(id)
        genres.each{ gen ->
            addNewAlbumGenre(id, gen.toInteger())
        }
        Sql sql = new Sql(dataSource)
        sql.execute("UPDATE albums SET title = ${title}, artist = ${artist} WHERE id = ${id}")
        return requestAlbum(id)
    }

    def clearAlbumGenres(int id)
    {
        Sql sql = new Sql(dataSource)
        sql.execute("DELETE FROM represents WHERE albumId = ${id}")
    }

    def addNewAlbumGenre(int albumId, int genreId)
    {
        Sql sql = new Sql(dataSource)
        sql.executeInsert("INSERT INTO represents(albumid, genreid) VALUES(${albumId}, ${genreId})")
    }

    def deleteAlbum(int id)
    {
        def albumDeleted = requestAlbum(id)
        Sql sql = new Sql(dataSource)
        sql.execute("DELETE FROM albums WHERE id = ${id}")
        return albumDeleted
    }

    def createGenre(String name)
    {
        Sql sql = new Sql(dataSource)
        def nextIdResult = sql.firstRow("SELECT nextval('genres_id_sequence')")
        int newGenreId = nextIdResult.nextval
        def genreCreated = sql.executeInsert("INSERT INTO genres(id, name) VALUES(${newGenreId}, ${name})",
            ["id", "name"]).first()
        return genreCreated
    }

    def updateGenre(int id, String name)
    {
        Sql sql = new Sql(dataSource)
        sql.execute("UPDATE genres SET name = ${name} WHERE id = ${id}")

        return requestGenre(id)
    }

    def deleteGenre(int id)
    {
        def genreDeleted = requestGenre(id)
        Sql sql = new Sql(dataSource)
        sql.execute("DELETE FROM genres WHERE id = ${id}")
        return genreDeleted
    }
}
