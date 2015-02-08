define([
    "angular",
    "components/track-queue/track-queue",
    "howler"
], function (angular) {

    var ApplaudioPlayer = angular.module("ApplaudioPlayer", ["ApplaudioTrackQueue"]);

    ApplaudioPlayer.controller("PlayerCtrl", [
        "$scope",
        "PlayerService",
        "TrackQueueService",
    function($scope, PlayerService, TrackQueueService) {


        $scope.display = function(){
            return PlayerService.hasTrack() ? PlayerService.getTrack().label : "Empty";
        };

        $scope.playPauseLabel = function() {
            return PlayerService.isPaused() ? "Play" : "||";
        };

        $scope.playable = function() {
            return PlayerService.hasTrack() || !TrackQueueService.isEmpty();
        };

        $scope.skippable = function() {
            return PlayerService.hasTrack();
        };

        $scope.playPause = function() {
            PlayerService.isPaused() ? play() : pause();
        };

        $scope.skip = function() {
            nextTrack();
        };

        var play = function () {
            console.log("PlayerCtrl.play()");
            if (!PlayerService.hasTrack()) {
                nextTrack();
            }
            PlayerService.play();
        };

        var pause = function() {
            console.log("PlayerCtrl.pause()");
            PlayerService.pause();
        };

        var nextTrack = function() {
            console.log("PlayerCtrl.nextTrack()");
            PlayerService.setTrack(TrackQueueService.getNext());
        };

    }]);

    ApplaudioPlayer.service("PlayerService", function() {

        var PlayerService = this;

        var howler = null; // audio library object
        var currentTrack = null;
        var pause = true;

        PlayerService.setTrack = function(track) {
            if (howler) { howler.stop(); }
            currentTrack = track;
            if (currentTrack) {
                howler = new Howl({ urls: [ currentTrack.location ]});
                if (!pause) { howler.play(); }
            } else {
                pause = true;
            }
        };

        PlayerService.hasTrack = function() {
            return currentTrack !== null;
        };

        PlayerService.getTrack = function() {
            return currentTrack;
        };

        PlayerService.play = function() {
            console.log("PlayerService.play " + currentTrack.label);
            howler.play();
            pause = false;
        };

        PlayerService.pause = function() {
            console.log("PlayerService.pause()");
            howler.pause();
            pause = true;
        };

        PlayerService.isPaused = function() {
            return pause;
        };

        return PlayerService;
    });

    return ApplaudioPlayer;

});
