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

        $scope.clickFileInputElement = function() {
            var fileInputElement = document.querySelector("#upload-file-input");
            fileInputElement.click();
        };

        $scope.readUploadFiles = function(passed) {
            console.log("readUploadFiles()");
            console.log(passed);
        };


    }]);

});
