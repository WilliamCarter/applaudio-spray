'use strict';

define([
    "angular",
    "angularAnimate",
    "angularRoute",

    "controllers/artists-controller",
    "controllers/albums-controller",

    "services/utils",

    "ui/draggable/draggable",
    "ui/infinite-label/infinite-label",
    "ui/modal/modal",
    "ui/on-enter/on-enter",
    "ui/progress-bar/progress-bar",
    "ui/scrollable/scrollable",

    "components/navigation/navigation",
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

        "Navigation",
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
            api: {
                allArtists: "/api/artists",
                albums: "/api/albums"
//                createDirectory: "/api/librarymanager/directory",
//                upload: "/api/librarymanager/upload",
//                downloads: "/api/library/downloads"
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

        $routeProvider.
            when('/', {
                redirectTo : '/artists'
            }).
            when('/artists', {
                templateUrl: '/views/listing.html',
                controller: 'ArtistsController'
            }).
            when('/albums/:artist', {
                templateUrl: '/views/listing.html',
                controller: 'AlbumsController'
            }).
            otherwise({
                templateUrl: '/404/view.html',
                controller: "FourOhFourController" // non-existent!
            });

    }]);

    return Applaudio;
});