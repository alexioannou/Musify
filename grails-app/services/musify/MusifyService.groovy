package musify

import grails.transaction.Transactional
import groovy.sql.Sql

@Transactional
class MusifyService {

    def dataSource

    //-------------------------------------------------------------- CREATE --------------------------------------------------------------
    def createAlbumServiceMethod(String title, String artist, def genres)   //Creates an Album in the Database
    {
        Sql sql = new Sql(dataSource)
        def nextIdResult = sql.firstRow("SELECT nextval('albums_id_sequence')")
        int newAlbumId = nextIdResult.nextval
        sql.execute("INSERT INTO albums VALUES(${newAlbumId}, ${title}, ${artist})")
        genres.each {gen ->
            sql.execute("INSERT INTO represents VALUES(${newAlbumId}, ${Integer.parseInt(gen)})")
        }
    }

    //-------------------------------------------------------------- List --------------------------------------------------------------
    def listAlbumsServiceMethod()   //Lists all Albums in the Database
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
        def albumSearchResults = searchAlbumsGetAlbums(title, artist, genre)
        def albumGenreSearchResults = searchAlbumsGetGenres(title, artist, genre)
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

    def searchAlbumsGetAlbums(String title, String artist, String genre)    //Fetches the correct Albums
    {
        Sql sql = new Sql(dataSource)
        return sql.rows("""SELECT DISTINCT albums.id,albums.title, albums.artist
                                                    FROM albums, represents, styles 
                                                    WHERE styles.id = styleid AND albums.id = albumid AND (albums.title = ${title} OR albums.artist = ${artist} OR styles.name = ${genre})""")
    }

    def searchAlbumsGetGenres(String title, String artist, String genre)    //Fetches all the Genres that correspond to the correct albums
    {
        Sql sql = new Sql(dataSource)
        return sql.rows("""SELECT correctAlbums.albumid, styles.name 
                                                        FROM represents, styles, (SELECT DISTINCT albumid 
                                                                FROM albums, represents, styles 
                                                                WHERE styles.id = styleid AND albums.id = albumid AND (albums.title = ${title} OR albums.artist = ${artist} OR styles.name = ${genre})) AS correctAlbums
                                                        WHERE represents.styleid = styles.id AND represents.albumid = correctAlbums.albumid""")
    }
    //def searchAlbumsSearviceMethod(String title, String artist, String genre)   //Searches for Albums in the Database
    //{
    //    def albumSearchResults = searchAlbumsGetAlbums(title, artist, genre)
    //    def albumGenreSearchResults = searchAlbumsGetGenres(title, artist, genre)
    //    return [albumSearchResults: albumSearchResults, albumGenreSearchResults: albumGenreSearchResults]
    //}

    //-------------------------------------------------------------- UPDATE --------------------------------------------------------------
    def updateAlbumsServiceMethod(String id, String title, String artist, def genres)   //Updates an Album in the database
    {
        updateAlbum(Integer.parseInt(id), title, artist)
        def albumGenresResults = fetchAlbumStyles(Integer.parseInt(id))
        if(genres)
        {
            albumGenresResults.each {albGen ->
                if(!genres.contains(albGen.styleid.toString()))
                    updateAlbumRemoveGenre(Integer.parseInt(id), albGen.styleid)
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
                    updateAlbumAddGenre(Integer.parseInt(id), Integer.parseInt(gen))
            }
        }
    }

    def updateAlbumRemoveGenre(int albumId, int styleId)    //Removes Genres from the Album
    {
        Sql sql = new Sql(dataSource)
        sql.execute("DELETE FROM represents WHERE albumid = ${albumId} AND styleid = ${styleId}")
    }

    def updateAlbumAddGenre(int albumId, int styleId)   //Adds Genres to the Album
    {
        Sql sql = new Sql(dataSource)
        sql.execute("INSERT INTO represents VALUES(${albumId},${styleId})")
    }

    def updateAlbum(int albumId, String title, String artist)    //Changes the Album's info
    {
        Sql sql = new Sql(dataSource)
        sql.execute("UPDATE albums SET title = ${title}, artist = ${artist} WHERE id = ${albumId}")
    }

    //-------------------------------------------------------------- DELETE --------------------------------------------------------------
    def deleteAlbum(String id)  //Deletes an Album from the Database
    {
        Sql sql = new Sql(dataSource)
        sql.execute("DELETE FROM albums WHERE id = ${Integer.parseInt(id)}")
    }

    //-------------------------------------------------------------- FETCH --------------------------------------------------------------
    def fetchSingleAlbum(String id) //Fetches an Album from the Database
    {
        Sql sql = new Sql(dataSource)
        def singleAlbumResult = sql.firstRow("Select * FROM albums where id = ${Integer.parseInt(id)}")
        singleAlbumResult.genres = sql.rows("Select styleid as id FROM styles, represents where styles.id = styleid AND albumid = ${Integer.parseInt(id)}")
        return singleAlbumResult
    }

    def fetchAllAlbums()    //Fetches all Albums from the Database
    {
        Sql sql = new Sql(dataSource)
        def albumsResults = sql.rows("Select * FROM albums")
        def albumGenresResults = sql.rows("Select albumid, styles.name FROM represents, styles WHERE styles.id = styleid")
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

    def fetchAlbumStyles(int albumId)   //Fetches the Genres of an Album from the Database
    {
        Sql sql = new Sql(dataSource)
        sql.rows("Select styleid FROM represents WHERE albumid = ${albumId}")
    }

    def fetchAllStyles()    //Fetches all Genres from the Database
    {
        Sql sql = new Sql(dataSource)
        def stylesResults = sql.rows("Select * FROM styles")
        return stylesResults
    }
}