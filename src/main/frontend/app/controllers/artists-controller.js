define([
    "controllers/controllers"
], function (ApplaudioControllers) {

    ApplaudioControllers.controller('ArtistsController', [
        "configuration",
        "MessageBarService",
        "$scope",
        "$http",
    function (configuration, MessageBarService, $scope, $http) {
        $scope.listingItemType = "artist";
        $scope.headingLinks = [ { label: "Artists", href: configuration.paths.home } ];

        $http.get(configuration.paths.api.allArtists).success(function(data) {
            console.log("success");
            console.log(data);
            $scope.listing = data;
        }).error(function(data, status){
            console.log(data);
            $scope.listing = [];
            MessageBarService.addMessage("Error hitting API: " + status);
        });

    }]);

});
