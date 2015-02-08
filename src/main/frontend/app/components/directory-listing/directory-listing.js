'use strict';

define([
    "angular",
    "catext/catext-arrays",
    "components/directory-listing/file",
    "components/player/player",
    "services/upload"
], function (angular, ArraysExtension, File) {

    var DirectoryListing = angular.module("DirectoryListing", ["ApplaudioUI", "ApplaudioUpload", "MessageBar", "ApplaudioPlayer", "ApplaudioTrackQueue"]);

    DirectoryListing.service("DirectoryListingService", [
        "$location",
        "$http",
        "MessageBarService",
        "configuration",
    function($location, $http, MessageBarService, configuration) {

        var DirectoryListingService = this;

        DirectoryListingService.currentPath = function() {
            return $location.path().replace(/^\/listing/, ""); // remove prefix "/listing"
        };

        DirectoryListingService.listing = [];

        DirectoryListingService.navigate = function(directoryName) {
            console.log("DirectoryListingService.navigate(" + directoryName + ")");
            $location.path($location.path() + "/" + directoryName);
        };

        DirectoryListingService.loadContent = function () {
            console.log("DirectoryListingService.loadContent()");
            var path = DirectoryListingService.currentPath();
            $http.get(configuration.paths.api.getDirectory + path)
                .success(function(data) {
                    console.log(data);
                    DirectoryListingService.listing = data.listing;
                })
                .error(function(){
                    MessageBarService.addMessage("Could not find the directory '" + path + "' in the library", "error");
                    console.log("Cannot find directory in /music/...");
                    DirectoryListingService.listing = [];
                });
        };

        DirectoryListingService.addDirectory = function(directoryName) {
            console.log("addDirectory(" + DirectoryListingService.currentPath() + ", " + directoryName + ")");
            $http.post(configuration.paths.api.createDirectory, { "path" : DirectoryListingService.currentPath(), "name" : directoryName })
                .success(function(){
                    var directoryPosition = 0;
                    while (directoryName > DirectoryListingService.listing[directoryPosition]) {
                        directoryPosition++;
                    }
                    DirectoryListingService.listing.push(new File(directoryName, "directory"));
                })
                .error(function(data, status){
                    MessageBarService.addMessage("There was an error adding the directory '" + directoryName + "'", "error");
                    console.log("Error adding directory: " + status);
                    console.log(data);
                });
        };

    }]);


    DirectoryListing.controller('DirectoryListingCtrl', [
        "DirectoryListingService",
        "configuration",
        "$scope",
    function (DirectoryListingService, configuration, $scope) {

        var directoryListing = this;

        directoryListing.listing = [];

        $scope.directoryIsEmpty = function() {
            return !ArraysExtension.contains(directoryListing.listing, function(item) {
                return item.type !== "directory";
            });
        };

        $scope.downloadAllUrl = configuration.paths.api.downloads + DirectoryListingService.currentPath();

        $scope.verifyDownloadAction = function(clickEvent) {
            // looks like a button but is really an anchor link so we need to disable click event manually.
            if ($scope.directoryIsEmpty()) {
                clickEvent.preventDefault();
            }
        };

        $scope.$watch(
            function () {
                return DirectoryListingService.listing;
            },
            function (newValue) {
                directoryListing.listing = newValue;
                window.resizeApplication();
            }
        );

        $scope.listingOrder = function(directory) {
            // Convert to lower case and remove preceding "The " if necessary
            var prefix = (directory.type === "directory") ? "A" : "B";
            if (DirectoryListingService.currentPath() === "/artists") {
                return prefix + directory.label.toLowerCase().replace(/^the /, "");
            } else {
                return prefix + directory.label.toLowerCase();
            }
        };

        DirectoryListingService.loadContent();

    }]);


    return DirectoryListing;

});