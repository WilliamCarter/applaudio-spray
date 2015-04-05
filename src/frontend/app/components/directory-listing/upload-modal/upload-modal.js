define([
    "components/directory-listing/directory-listing",
    "services/upload"
], function (DirectoryListing) {

    DirectoryListing.directive("uploadModal", [
        "DirectoryListingService",
        "UploadService",
        "$interval",
        "configuration",
        "MessageBarService",
    function(DirectoryListingService, UploadService, $interval, configuration, MessageBarService) {

        return {
            restrict: 'E',
            templateUrl: "components/directory-listing/upload-modal/upload-modal.html",
            link: function(scope, element, attrs) {

                scope.files = [];
                scope.uploadProgress = 0;

                var fileInputElement = document.querySelector("#modal-file-input");

                scope.clickFileInput = function() {
                    console.log("modal.clickFileInput()");
                    fileInputElement.click();
                };

                scope.readUploadFiles = function() {
                    console.log("readUploadFiles()");
                    var allFiles = fileInputElement.files;
                    console.log(allFiles);

                    // Filter out unsupported files
                    for (var i = 0; i < allFiles.length; i++) {
                        if (configuration.supportedMedia.types.indexOf(allFiles[i].type) !== -1) {
                            scope.files.push(allFiles[i]);
                        } else {
                            var message = allFiles[i].name + " is of " +
                                (allFiles[i].type === "" ? "unknown type " : "type '" + allFiles[i].type + "' ") +
                                "and is not supported by Applaudio";
                            MessageBarService.addMessage(message, "error");
                        }
                    }

                    scope.$apply();
                };

                scope.confirm = function() {
                    if (scope.files.length > 0) {
                        scope.uploadInProgress = true;
                        UploadService.upload(DirectoryListingService.currentPath(), scope.files);
                    }
                };


                UploadService.subscribeForProgressUpdates(function(updateEvent) {

                    if (updateEvent.type === "progress") {

                        scope.$apply(function() {
                            scope.uploadProgress = updateEvent.progress;
                        });

                    } else if (updateEvent.type === "complete" && updateEvent.success) {

                        // wait half a second to allow load animation to finish and let user know upload was successful
                        // TODO: Is this wait behaviour beneficial to the user or does it just steal time from them? Maybe replace with some sort of notification system.
                        $interval(function() {
                            scope.hide();
                            DirectoryListingService.loadContent();
                        }, 500, 1);

                    } else {
                        MessageBarService.addMessage("Upload error", "error");
                        console.log(updateEvent);
                    }
                });

            }
        };
    }]);

});