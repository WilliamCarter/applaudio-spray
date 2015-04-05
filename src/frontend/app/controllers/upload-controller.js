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

        $scope.clickFileInputElement = function() {
            var fileInputElement = document.querySelector("#upload-file-input");
            fileInputElement.click();
        };

        $scope.readUploadFiles = function(files) {
            $scope.$apply(function() {
                $scope.filename = files[0].name;
            });

            UploadService.getMetadata(files[0], function(metadata) {
                $scope.$apply(function() {
                    $scope.upload.title = metadata.title;
                    $scope.upload.artist = metadata.artist;
                    $scope.upload.album = metadata.album;
                    $scope.upload.albumTrack = metadata.albumTrack;
                    $scope.upload.year = metadata.year;
                    $scope.upload.encoding = metadata.encoding;
                });
            });

        };


    }]);

});
