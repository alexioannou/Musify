package musify

import grails.transaction.Transactional
import groovy.sql.Sql

@Transactional
class MusifyService {

    def dataSource

    def createAlbum(String title, String artist, def genres) {
        Sql sql = new Sql(dataSource)
        def nextIdResult = sql.firstRow("SELECT nextval('albums_id_sequence')")
        int newAlbumId = nextIdResult.nextval
        sql.execute("INSERT INTO albums VALUES(${newAlbumId}, ${title}, ${artist})")
        genres.each {gen ->
            int genId = Integer.parseInt(gen)
            sql.execute("INSERT INTO represents VALUES(${newAlbumId}, ${genId})")
        }
    }

    def getAlbums() {
        Sql sql = new Sql(dataSource)
        def albumsResults = sql.rows("Select * FROM albums")
        def albumGenresResults = sql.rows("Select albumid, styles.name FROM represents, styles WHERE styles.id = styleid")
        return [albumsResults: albumsResults, albumGenresResults: albumGenresResults]
    }

    def searchAlbums(String title, String artist, String genre) {
        Sql sql = new Sql(dataSource)
        def albumSearchResults = sql.rows("""SELECT DISTINCT albums.id,albums.title, albums.artist
                                                    FROM albums, represents, styles 
                                                    WHERE styles.id = styleid AND albums.id = albumid AND (albums.title = ${title} OR albums.artist = ${artist} OR styles.name = ${genre})""")
        def albumGenreSearchResults = sql.rows("""SELECT correctAlbums.albumid, styles.name 
                                                        FROM represents, styles, (SELECT DISTINCT albumid 
                                                                FROM albums, represents, styles 
                                                                WHERE styles.id = styleid AND albums.id = albumid AND (albums.title = ${title} OR albums.artist = ${artist} OR styles.name = ${genre})) AS correctAlbums
                                                        WHERE represents.styleid = styles.id AND represents.albumid = correctAlbums.albumid""")
        return [albumSearchResults: albumSearchResults, albumGenreSearchResults: albumGenreSearchResults]
    }

    def getSingleAlbum(int id) {
        Sql sql = new Sql(dataSource)
        def singleAlbumResult = sql.firstRow("Select * FROM albums where id = ${id}")
        singleAlbumResult.genres = sql.rows("Select styleid as id FROM styles, represents where styles.id = styleid AND albumid = ${id}")
        return singleAlbumResult
    }

    def updateAlbum(int id, String title, String artist, def genres)
    {
        Sql sql = new Sql(dataSource)
        sql.execute("UPDATE albums SET title = ${title}, artist = ${artist} WHERE id = ${id}")
        def albumGenresResults = sql.rows("Select styleid FROM represents WHERE albumid = ${id}")
        albumGenresResults.each {gen ->
            if(!genres.contains(gen.styleid))
            {
                int genId = Integer.parseInt(gen.styleid.toString())
                sql.execute("DELETE FROM represents WHERE albumid = ${id} AND styleid = ${genId}")
            }
        }
        genres.each { gen ->
            if(!albumGenresResults.contains(gen))
            {
                int genId = Integer.parseInt(gen.styleid.toString())
                sql.execute("INSERT INTO represents VALUES(${id},${genId})")
            }
        }
    }

    def deleteAlbum(int id)
    {
        String deleteAlbumQuery = "DELETE FROM albums WHERE id = ${id}"
        Sql sql = new Sql(dataSource)
        sql.execute(deleteAlbumQuery, [Integer.parseInt(id.toString())])
    }

    def getStyles() {
        String getStylesQuery = "Select * FROM styles"
        Sql sql = new Sql(dataSource)
        def stylesResults = sql.rows(getStylesQuery)
        return stylesResults
    }
}