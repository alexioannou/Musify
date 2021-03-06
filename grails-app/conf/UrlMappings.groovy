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

        "/musifyRest/$action?"{
                view = "/illegalUrl"
        }

        "/$controller/$action?/$id?(.$format)?"{
            constraints {

            }
        }
	}
}
