define([
    "controllers/controllers"
], function (ApplaudioControllers) {

    ApplaudioControllers.controller('AlbumController', [
        "configuration",
        "MessageBarService",
        "$scope",
        "$http",
        "$routeParams",
    function (configuration, MessageBarService, $scope, $http, $routeParams) {
        $scope.listingItemType = "track";
        $scope.artist = $routeParams.artist;
        $scope.album = $routeParams.album;
        $scope.heading = $scope.album;

        $http.get(configuration.paths.api.album($scope.artist, $scope.album)).success(function(data) {
            console.log("success");
            console.log(data);
            $scope.listing = data;
        }).error(function(data, status){
            console.log(status);
            console.log(data);
            $scope.listing = [];
            switch (status) {
                case 404:
                    MessageBarService.addMessage("No such album found in the database: " + $scope.artist + " - " + $scope.album);
                    break;
                default:
                    MessageBarService.addMessage("Error hitting API: " + status);
                    break;
            }
        });

    }]);

});
