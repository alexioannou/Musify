package musify

class MusifyController {

    def musifyService

    def index() {
        redirect (action: "listAlbums")
    }

    def add() {
        def myStyles = musifyService.getStyles()
        [genres : myStyles]
    }

    def create() {
        musifyService.createAlbum(params.title, params.artist, params.genres)
        println params.title.getClass()
        redirect (action: "listAlbums")
    }

    def edit() {
        def myStyles = musifyService.getStyles()
        def myAlbum = musifyService.getSingleAlbum(params.id)
        [album: myAlbum, genres : myStyles]
    }

    def update() {
        musifyService.updateAlbum(params.id, params.title, params.artist, params.genres)
        redirect (action: "listAlbums")
    }

    def show() {

    }

    def listAlbums() {
        def myAlbums = musifyService.getAlbums()
        myAlbums[0].each{alb ->
            alb.genres = new ArrayList<>()
            myAlbums[1].each {gen ->
                if(gen.albumid == alb.id)
                    alb.genres.add(gen.name)
            }
        }
        [ albums : myAlbums[0], criteria: ["", "", ""]]
    }

    def search() {
        def myAlbums = musifyService.searchAlbums(params.title, params.artist, params.genre)
        myAlbums[0].each{alb ->
            alb.genres = new ArrayList<>()
            myAlbums[1].each {gen ->
                if(gen.albumid == alb.id)
                    alb.genres.add(gen.name)
            }
        }

        return render(view: 'search', model: [albums : myAlbums[0], criteria: params.criteria]);
    }

    def delete() {
        musifyService.deleteAlbum(params.id)
        redirect (action: "listAlbums")
    }

    def test() {

    }

    def test2() {
        def myAlbums = musifyService.searchAlbums(params.criteria[0], params.criteria[1], params.criteria[2])
        myAlbums[0].each{alb ->
            alb.genres = new ArrayList<>()
            myAlbums[1].each {gen ->
                if(gen.albumid == alb.id)
                    alb.genres.add(gen.name)
            }
        }

        return render(view: 'search', model: [albums : myAlbums[0], criteria: params.criteria]);
    }
}
