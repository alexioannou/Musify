package musify

import grails.transaction.Transactional
import groovy.sql.Sql

@Transactional
class MusifyService {

    def dataSource

    def createAlbum(Object title, Object artist, Object[] genres) {
        String nextIdQuery = "SELECT nextval('albums_id_sequence');"
        String createAlbumQuery = "INSERT INTO albums VALUES(?,?,?);";
        String addAlbumGenreQuery = "INSERT INTO represents VALUES(?,?);"
        Sql sql = new Sql(dataSource)
        def nextIdResult = sql.firstRow(nextIdQuery)
        int newAlbumId = nextIdResult.nextval
        sql.execute(createAlbumQuery, [newAlbumId, title.toString(),artist.toString()])
        genres.each {gen ->
            sql.execute(addAlbumGenreQuery, [newAlbumId, Integer.parseInt(gen.toString())])
        }
    }

    def getAlbums() {
        String getAlbumsQuery = "Select * FROM albums;"
        String getAlbumGenresQuery = "Select albumid,styles.name FROM represents,styles WHERE styles.id = styleid;"
        Sql sql = new Sql(dataSource)
        def albumsResults = sql.rows(getAlbumsQuery)
        def albumGenresResults = sql.rows(getAlbumGenresQuery)
        return [albumsResults,albumGenresResults]
    }

    def getSingleAlbum(Object id) {
        String getSingleAlbumQuery = "Select * FROM albums where id = ?"
        String getSingleAlbumStylesQuery = "Select styleid as id FROM styles,represents where styles.id = styleid AND albumid = ?;"
        Sql sql = new Sql(dataSource)
        def singleAlbumResult = sql.firstRow(getSingleAlbumQuery, [Integer.parseInt(id.toString())])
        singleAlbumResult.genres = sql.rows(getSingleAlbumStylesQuery, [Integer.parseInt(id.toString())])
        return singleAlbumResult
    }

    def updateAlbum(Object id, Object title, Object artist, Object[] genres)
    {
        println "param genres: "+genres
        String updateAlbumQuery = "UPDATE albums SET title = ?, artist = ? WHERE id = ?;"
        String getAlbumGenresQuery = "Select styleid FROM represents WHERE albumid = ?;"
        String removeAlbumGenreQuery = "DELETE FROM represents WHERE albumid = ? AND styleid = ?;"
        String addAlbumGenreQuery = "INSERT INTO represents VALUES(?,?);"
        Sql sql = new Sql(dataSource)
        sql.execute(updateAlbumQuery,[title.toString(), artist.toString(), Integer.parseInt(id.toString())])
        def albumGenresResults = sql.rows(getAlbumGenresQuery, [Integer.parseInt(id.toString())])
        albumGenresResults.each {gen ->
            println "GEN: "+gen.styleid.getClass().getName()
            println "genres: "+genres.getClass().getName()
            if(!genres.contains((Object)gen.styleid))
                sql.execute(removeAlbumGenreQuery, [Integer.parseInt(id.toString()), Integer.parseInt(gen.styleid.toString())])
        }
        genres.each { gen ->
            if(!albumGenresResults.contains(gen))
                sql.execute(addAlbumGenreQuery, [Integer.parseInt(id.toString()), Integer.parseInt(gen.toString())])
        }
    }

    def searchAlbums(Object title, Object artist, Object genre) {
        String searchAlbumsQuery = "SELECT DISTINCT albums.id,albums.title,albums.artist FROM albums,represents,styles WHERE styles.id = styleid AND albums.id = albumid AND (albums.title = ? OR albums.artist = ? OR styles.name = ?);"
        String searchAlbumsGenresQuery = "SELECT correctAlbums.albumid, styles.name FROM (SELECT DISTINCT albumid FROM albums,represents,styles WHERE styles.id = styleid AND albums.id = albumid AND (albums.title = ? OR albums.artist = ? OR styles.name = ?)) AS correctAlbums, represents, styles WHERE represents.styleid = styles.id AND represents.albumid = correctAlbums.albumid;"
        Sql sql = new Sql(dataSource)
        def albumSearchResults = sql.rows(searchAlbumsQuery, [title.toString(),artist.toString(),genre.toString()])
        def albumGenreSearchResults = sql.rows(searchAlbumsGenresQuery, [title.toString(),artist.toString(),genre.toString()])
        return [albumSearchResults,albumGenreSearchResults]
    }

    def deleteAlbum(Object id)
    {
        String deleteAlbumQuery = "DELETE FROM albums WHERE id = ?"
        Sql sql = new Sql(dataSource)
        sql.execute(deleteAlbumQuery, [Integer.parseInt(id.toString())])
    }

    def getStyles() {
        String getStylesQuery = "Select * FROM styles;"
        Sql sql = new Sql(dataSource)
        def stylesResults = sql.rows(getStylesQuery)
        return stylesResults
    }
}