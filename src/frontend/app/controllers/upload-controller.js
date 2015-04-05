define([
    "controllers/controllers"
], function (ApplaudioControllers) {

    ApplaudioControllers.controller('UploadController', [
        "configuration",
        "MessageBarService",
        "UploadService",
        "$scope",
        "$http",
    function (configuration, MessageBarService, UploadService, $scope) {

        console.log("Upload Controller defined");

        $scope.clickFileInputElement = function() {
            var fileInputElement = document.querySelector("#upload-file-input");
            fileInputElement.click();
        };

        $scope.readUploadFiles = function(files) {
            console.log("readUploadFiles()");
            console.log(files);
            console.log(files[0].name);
            $scope.$apply(function() {
                $scope.filename = files[0].name;
            });

            UploadService.getMetadata(files[0]);

        };


    }]);

});
