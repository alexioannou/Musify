class UrlMappings {

	static mappings = {
        "/albums" {
                controller = "MusifyRest"
                action = [GET: "getAllAlbums", POST: "postAlbum"]
        }

        "/albums/$albumId?" {
                controller = "MusifyRest"
                action = "getAlbum"
        }

        "/genres/$genreId?/albums" {
                controller = "MusifyRest"
                action = "getAllAlbumsOfGenre"
        }

        "/genres/$genreId?/albums/$albumId" {
                controller = "MusifyRest"
                action = "getAlbumOfGenre"
        }

        "/genres" {
                controller = "MusifyRest"
                action = [GET: "getAllGenres", POST: "postGenre"]
        }

        "/genres/$genreId?" {
                controller = "MusifyRest"
                action = "getGenre"
        }

        "/albums/$albumId?/genres" {
                controller = "MusifyRest"
                action = "getAllGenresOfAlbum"
        }

        "/albums/$albumId?/genres/$genreId?" {
                controller = "MusifyRest"
                action = "getGenreOfAlbum"
        }

        "/"(view:"/index")
        "500"(view:'/error')
	}
}
