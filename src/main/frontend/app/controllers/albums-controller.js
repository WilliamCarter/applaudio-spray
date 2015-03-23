define([
    "controllers/controllers"
], function (ApplaudioControllers) {

    ApplaudioControllers.controller('AlbumsController', [
        "configuration",
        "MessageBarService",
        "$scope",
        "$http",
        "$routeParams",
    function (configuration, MessageBarService, $scope, $http, $routeParams) {
        $scope.type = "album";
        $scope.artist = $routeParams.artist
        $scope.heading = $scope.artist;

        $http.get(configuration.paths.api.albums + "/" + $scope.artist).success(function(data) {
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
