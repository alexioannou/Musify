package musify

import grails.transaction.Transactional
import groovy.sql.Sql

@Transactional
class MusifyRestService {

    def dataSource

    def requestAllAlbums()   //Lists all Albums in the Database
    {
        Sql sql = new Sql(dataSource)
        def albumsResults = sql.rows("Select * FROM albums")
    }

    def requestAlbum(int albumId)
    {
        Sql sql = new Sql(dataSource)
        def albumResult = sql.firstRow("Select * FROM albums WHERE id = ${albumId}")
    }

    def requestAllAlbumsOfGenre(int genreId)
    {
        Sql sql = new Sql(dataSource)
        def albumsResults = sql.rows("SELECT id, title, artist FROM albums, represents WHERE albums.id = albumId AND styleId = ${genreId}")
    }

    def requestAlbumOfGenre(int genreId, int albumId)
    {
        Sql sql = new Sql(dataSource)
        def albumResult = sql.firstRow("SELECT id, title, artist FROM albums, represents WHERE id = albumId AND id = ${albumId} AND styleId = ${genreId}")
    }

    def requestAllGenres()
    {
        Sql sql = new Sql(dataSource)
        def genresResults = sql.rows("SELECT * FROM styles")
    }

    def requestGenre(int genreId)
    {
        Sql sql = new Sql(dataSource)
        def albumResult = sql.firstRow("Select * FROM styles WHERE id=${genreId}")
    }

    def requestAllGenresOfAlbum(int albumId)
    {
        Sql sql = new Sql(dataSource)
        def genresResults = sql.rows("SELECT id, name FROM styles, represents WHERE styles.id = styleId AND albumId = ${albumId}")
    }

    def requestGenreOfAlbum(int albumId, int genreId)
    {
        Sql sql = new Sql(dataSource)
        def genreResult = sql.firstRow("SELECT id, name FROM styles, represents WHERE styles.id = styleId AND albumId = ${albumId} AND styles.id = ${genreId}")
    }

    def createAlbum(String title, String artist, def genres)
    {
        Sql sql = new Sql(dataSource)
        def nextIdResult = sql.firstRow("SELECT nextval('albums_id_sequence')")
        int newAlbumId = nextIdResult.nextval
        sql.execute("INSERT INTO albums VALUES(${newAlbumId}, ${title}, ${artist})")
        genres.each {gen ->
            sql.execute("INSERT INTO represents VALUES(${newAlbumId}, ${Integer.parseInt(gen)})")
        }
        return requestAlbum(newAlbumId)
    }

    def createGenre(String name)
    {
        Sql sql = new Sql(dataSource)
        def nextIdResult = sql.firstRow("SELECT nextval('genres_id_sequence')")
        int newGenreId = nextIdResult.nextval
        sql.execute("INSERT INTO styles(id,name) VALUES(${newGenreId}, ${name})")
        return requestGenre(newGenreId)
    }
}
