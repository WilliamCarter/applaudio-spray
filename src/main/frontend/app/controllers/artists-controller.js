define([
    "controllers/controllers"
], function (ApplaudioControllers) {

    ApplaudioControllers.controller('ArtistsCtrl', [
        "configuration",
        "MessageBarService",
        "$scope",
        "$http",
    function (configuration, MessageBarService, $scope, $http) {
        $scope.heading = "Artists";

        $scope.messagePlease = function() {
            MessageBarService.addMessage("This is your requested message.", 'standard', 6000);

        };


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
