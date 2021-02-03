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

    def getSingleAlbum(String id)
    {
        int correctId = Integer.parseInt(id)
        Sql sql = new Sql(dataSource)
        def singleAlbumResult = sql.firstRow("Select * FROM albums where id = ${correctId}")
        singleAlbumResult.genres = sql.rows("Select styleid as id FROM styles, represents where styles.id = styleid AND albumid = ${correctId}")
        return singleAlbumResult
    }

    def updateAlbum(String id, String title, String artist, def genres)
    {
        int correctId = Integer.parseInt(id)
        Sql sql = new Sql(dataSource)
        sql.execute("UPDATE albums SET title = ${title}, artist = ${artist} WHERE id = ${correctId}")
        def albumGenresResults = sql.rows("Select styleid FROM represents WHERE albumid = ${correctId}")
        if(genres)
        {
            albumGenresResults.each {albGen ->
                if(!genres.contains(albGen.styleid.toString()))
                {
                    sql.execute("DELETE FROM represents WHERE albumid = ${correctId} AND styleid = ${albGen.styleid}")
                }
            }
            //For each Genre selected in the Edit form
            genres.each { gen ->
                boolean flag = false; //false = not associated
                albumGenresResults.find {albGen ->
                    if (albGen.styleid == Integer.parseInt(gen))    //It's already associated with this album
                    {
                        flag = true;
                        return true;
                    }
                    return false    //It's not already associated with this album
                }
                if(!flag)    //Associate it with this album
                {
                    int genId = Integer.parseInt(gen)
                    sql.execute("INSERT INTO represents VALUES(${correctId},${genId})")
                }
            }
        }
    }

    def deleteAlbum(String id)
    {
        int correctId = Integer.parseInt(id)
        Sql sql = new Sql(dataSource)
        sql.execute("DELETE FROM albums WHERE id = ${correctId}")
    }

    def getStyles() {
        Sql sql = new Sql(dataSource)
        def stylesResults = sql.rows("Select * FROM styles")
        return stylesResults
    }
}