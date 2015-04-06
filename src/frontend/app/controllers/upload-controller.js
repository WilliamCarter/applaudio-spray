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

        $scope.upload = $scope.upload || {};
        var formFieldIds = [ "title", "artist", "album", "albumTrack", "year", "encoding" ];

        var clearForm = function() {
            $scope.upload = {};
        };

        $scope.clickFileInputElement = function() {
            var fileInputElement = document.querySelector("#upload-file-input");
            fileInputElement.click();
        };

        $scope.readUploadFiles = function(files) {
            $scope.$apply(function() {
                $scope.upload.file = files[0];
                $scope.upload.filename = $scope.upload.file.name;
            });

            UploadService.getMetadata(files[0], function(metadata) {
                $scope.$apply(function() {
                    formFieldIds.forEach(function(field) {
                       $scope.upload[field] = metadata[field];
                    });
                });
            });
        };

        $scope.startUpload = function() {
            MessageBarService.addMessage("Uploading file: " + $scope.upload.filename);
            UploadService.upload($scope.upload, function(data) {
                console.log("Upload complete");
                console.log(data);
            });
            clearForm();
        }

    }]);

});
