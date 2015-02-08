'use strict';

define([
    "angular",
    "angularAnimate",
    "angularRoute",

    "services/utils",

    "ui/draggable/draggable",
    "ui/infinite-label/infinite-label",
    "ui/modal/modal",
    "ui/on-enter/on-enter",
    "ui/progress-bar/progress-bar",
    "ui/scrollable/scrollable",

    "components/navigation/navigation",
    "components/message-bar/message-bar",
    "components/directory-listing/directory-listing",
    "components/directory-listing/listing-item/listing-item",
    "components/directory-listing/upload-modal/upload-modal",
    "components/directory-listing/add-directory-modal/add-directory-modal",
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
        "DirectoryListing",

        "ApplaudioUI",
        "ApplaudioUtilities"
    ]);

    Applaudio.constant("configuration", {

        paths: {
            home: "/#/listing/artists",
            api: {
                getDirectory: "/api",
                createDirectory: "/api/librarymanager/directory",
                upload: "/api/librarymanager/upload",
                downloads: "/api/library/downloads"
            }
        },

        messageBar: {
            showDuration: 3000
        },

        alerts: {
            uploadProgressNotSupported: "Your browser does not support upload feedback. That's why the progress bar doesn't move.",
            uploadComplete: "Upload complete. Tracks will appear soon.",
            trackQueueDuplicates: "Soz, that track is already in the queue and due to a bizarre bug in angular-ui-tree, you can't add it again."
        },

        supportedMedia : {
            types: ["audio/mpeg", "audio/mp3", "audio/ogg"],
            extensions: [".mpeg", ".mp3", ".ogg"]
        }

    });

    console.log("Configuring Applaudio");
    Applaudio.config(["$routeProvider", function($routeProvider) {

        $routeProvider.
            when('/', {
                redirectTo : '/listing/artists',
                templateUrl: '/views/main.html'
            }).
            when('/listing/:url*', {
                templateUrl: '/views/main.html'
            }).
            otherwise({
                templateUrl: '/404/view.html',
                controller: "FourOhFourCtrl"
            });

    }]);

    return Applaudio;
});