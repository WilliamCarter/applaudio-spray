define([
    "controllers/controllers"
], function (ApplaudioControllers) {

    ApplaudioControllers.controller('ArtistAlbumsController', [
        "configuration",
        "MessageBarService",
        "$scope",
        "$http",
        "$routeParams",
    function (configuration, MessageBarService, $scope, $http, $routeParams) {
        $scope.listingItemType = "album";
        $scope.artist = $routeParams.artist;
        $scope.headingLinks = [
            { label: "Artists", href: configuration.paths.home },
            { label: $scope.artist, href: configuration.paths.albumsBy($scope.artist) } ];

        $http.get(configuration.paths.api.albumsBy($scope.artist)).success(function(data) {
            console.log("success");
            console.log(data);
            $scope.listing = data;
        }).error(function(data, status){
            console.log(status);
            console.log(data);
            $scope.listing = [];
            switch (status) {
                case 404:
                    MessageBarService.addMessage("No such artist found in the database: " + $scope.artist);
                    break;
                default:
                    MessageBarService.addMessage("Error hitting API: " + status);
                    break;
            }
        });

    }]);

});
