define([
    "controllers/controllers"
], function (ApplaudioControllers) {

    ApplaudioControllers.controller('UploadController', [
        "configuration",
        "MessageBarService",
        "$scope",
        "$http",
    function (configuration, MessageBarService, $scope, $http) {

        console.log("Upload Controller defined");
    }]);

});
