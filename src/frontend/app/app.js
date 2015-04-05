'use strict';

define([
    "angular",
    "angularAnimate",
    "angularRoute",

    "controllers/artists-controller",
    "controllers/artist-albums-controller",
    "controllers/album-controller",
    "controllers/upload-controller",

    "services/utils",

    "ui/draggable/draggable",
    "ui/infinite-label/infinite-label",
    "ui/on-enter/on-enter",
    "ui/progress-bar/progress-bar",
    "ui/scrollable/scrollable",

    "components/message-bar/message-bar",
    "components/player/player",
    "components/track-queue/track-queue"

], function(angular) {

    // Hack to fix application height. Can't seem to do this with CSS alone.
    window.resizeApplication = function() {
        var header = document.getElementById("application-header");
        var lowerContent = document.getElementById("lower-content");
        lowerContent.style.height = (window.innerHeight - header.clientHeight - 20) + "px";
    };
    window.onresize = function() {
        console.log("resize");
        window.resizeApplication();
    };

    console.log("Defining Applaudio");
    var Applaudio = angular.module('Applaudio', [
        "ngRoute",
        "ngAnimate",

        "ApplaudioPlayer",
        "ApplaudioTrackQueue",
        "MessageBar",

        "ApplaudioControllers",
        "ApplaudioUI",
        "ApplaudioUtilities"
    ]);

    Applaudio.constant("configuration", {

        paths: {
            home: "/artists",
            albumsBy: function(artist) {
                return "/albums/" + artist;
            },
            album: function(artist, album) {
                return "/albums/" + artist + "/" + album;
            },
            api: {
                allArtists: "/api/artists",
                albumsBy: function(artist) {
                    return "/api/albums/" + artist;
                },
                album: function(artist, album) {
                    return "/api/tracks/" + artist + "/" + album;
                },
                metadata: "/api/metadata"
            }
        },

        messageBar: {
            showDuration: 3000
        },

        alerts: {
            uploadProgressNotSupported: "Your browser does not support upload feedback. That's why the progress bar doesn't move.",
            uploadComplete: "Upload complete. Tracks will appear soon.",
            trackQueueDuplicates: "That track is already in the queue and due to a bizarre bug in angular-ui-tree, you can't add it again. !?"
        },

        supportedMedia : {
            types: ["audio/mpeg", "audio/mp3"],
            extensions: [".mpeg", ".mp3"]
        }

    });

    Applaudio.config([
        "configuration",
        "$routeProvider",
    function(configuration, $routeProvider) {

        $routeProvider
            .when("/", {
                redirectTo : configuration.paths.home
            })
            .when("/artists", {
                templateUrl: "/views/listing.html",
                controller: "ArtistsController"
            })
            .when("/albums/:artist", {
                templateUrl: "/views/listing.html",
                controller: "ArtistAlbumsController"
            })
            .when("/albums/:artist/:album", {
                templateUrl: "/views/listing.html",
                controller: "AlbumController"
            })
            .when("/upload", {
                templateUrl: "views/upload.html",
                controller: "UploadController"
            })
            .otherwise({
                templateUrl: "/404/view.html",
                controller: "FourOhFourController" // non-existent!
            });

    }]);

    return Applaudio;
});