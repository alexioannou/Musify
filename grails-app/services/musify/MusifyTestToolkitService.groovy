package musify

import grails.transaction.Transactional
import groovy.sql.Sql

@Transactional
class MusifyTestToolkitService {
    
    def dataSource
    
    def fetchAlbum(int id)
    {
        Sql sql = new Sql(dataSource)
        sql.firstRow("SELECT * FROM albums WHERE id = ${id}")
    }
    
    /**
     * Fetches an Album along with the IDs of
     * all the Genres associated with it
     * @param id : The id of the Album to be fetched
     */
    def fetchAlbumAlongWithGenreIds(int id)
    {
        def album = fetchAlbum(id)
        def albumGenres = fetchAllGenresOfAlbum(id)
        album.genres = []
        albumGenres.each { gen ->
            album.genres << gen.id
        }
        return album
    }
    
    def fetchGenre(int id)
    {
    
    }

    def fetchAlbumGenre(int albumId, int genreId)
    {
        Sql sql = new Sql(dataSource)
        def albumGenre = sql.firstRow("SELECT * FROM represents WHERE albumid = ${albumId} AND genreid = ${genreId}")
        return albumGenre
    }
    
    def fetchAllGenresOfAlbum(int id)
    {
        Sql sql = new Sql(dataSource)
        def genres = sql.rows("SELECT id, name FROM genres, represents WHERE genreId = genres.id AND albumId = ${id}")
        return genres
    }
    
    def searchAlbum(String title, String artist, String genreName)
    {
    
    }
    
    def searchGenre(String name)
    {
    
    }
    
    def persistAlbum(int id, String title, String artist, def genres)
    {
        Sql sql = new Sql(dataSource)
        def albumCreated = sql.executeInsert("INSERT INTO albums(id, title, artist) VALUES(${id}, ${title}, ${artist})",
        ["id", "title", "artist"]).first()
        albumCreated.genres = []
        genres.each {gen ->
            persistGenre(gen.id, gen.name)
            associateGenreWithAlbum(id, gen.id)
        }
        return albumCreated
    }

    def persistAlbum(def album)
    {
        Sql sql = new Sql(dataSource)
        def albumCreated = sql.executeInsert("INSERT INTO albums(id, title, artist) VALUES(${album.id}, ${album.title}, ${album.artist})",
                ["id", "title", "artist"]).first()
        albumCreated.genres = []
        album.genres.each {gen ->
            persistGenre(gen.id, gen.name)
            associateGenreWithAlbum(album.id, gen.id)
        }
        return albumCreated
    }
    
    def persistGenre(int id, String name)
    {
        Sql sql = new Sql(dataSource)
        def genreCreated = sql.executeInsert("INSERT INTO genres(id, name) VALUES(${id}, ${name})")
        return genreCreated
    }

    def persistGenre(def genre)
    {
        Sql sql = new Sql(dataSource)
        def genreCreated = sql.executeInsert("INSERT INTO genres(id, name) VALUES(${genre.id}, ${genre.name})")
        return genreCreated
    }

    def associateGenreWithAlbum(int albumId, int genreId)
    {
        Sql sql = new Sql(dataSource)
        def albumGenreCreated = sql.executeInsert("INSERT INTO represents(albumid, genreid) VALUES(${albumId}, ${genreId})")
        return albumGenreCreated
    }
}
