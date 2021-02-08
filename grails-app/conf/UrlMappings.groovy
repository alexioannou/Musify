class UrlMappings {

	static mappings = {

        "/"(view:"/index")
        "500"(view:'/error')

        "/albums" {
                controller = "MusifyRest"
                action = [GET: "getAllAlbums", POST: "postAlbum"]
        }

        //Can't send form-data through PUT, so doing a POST & redirecting it to the PUT Controller Action
        "/albums/$albumId?" {
                controller = "MusifyRest"
                action = [GET: "getAlbum", POST: "putAlbum", DELETE: "deleteAlbum"]
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

        //Can't send form-data through PUT, so doing a POST & redirecting it to the PUT Controller Action
        "/genres/$genreId?" {
                controller = "MusifyRest"
                action = [GET: "getGenre", POST: "putGenre", DELETE: "deleteGenre"]
        }

        "/albums/$albumId?/genres" {
                controller = "MusifyRest"
                action = "getAllGenresOfAlbum"
        }

        "/albums/$albumId?/genres/$genreId?" {
                controller = "MusifyRest"
                action = "getGenreOfAlbum"
        }
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }
	}
}
