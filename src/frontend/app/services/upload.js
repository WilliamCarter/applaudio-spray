define(["angular"], function (angular) {

    var ApplaudioUpload = angular.module("ApplaudioUpload", ["MessageBar"]);

    ApplaudioUpload.service("UploadService", [
        "MessageBarService",
        "configuration",
    function(MessageBarService, configuration){

        var UploadService = this;

        var progressSubscriberCallbacks = [];

        UploadService.getMetadata = function(file, then) {
            var metadataForm = new FormData();
            metadataForm.append(file.name, file);

            var request = new XMLHttpRequest();
            request.open('POST', configuration.paths.api.metadata, true);
            request.addEventListener("load", function() {
                if (request.status !== 200) {
                    var errorMessage = "Error hitting " + configuration.paths.api.metadata + ": " + request.status;
                    console.log(errorMessage);
                    MessageBarService.addMessage(errorMessage, "error");
                } else {
                    then(JSON.parse(request.response));
                }
            });

            request.send(metadataForm);

        };

        UploadService.upload = function(data, then) {
            var uploadData = new FormData();
            for (var property in data) {
                uploadData.append(property, data[property]);
            }

            var request = new XMLHttpRequest();
            registerProgressEvents(request.upload);
            request.open('POST', configuration.paths.api.upload, true);

            request.addEventListener("load", function() {
                if (request.status !== 200) {
                    var errorMessage = "Error hitting " + configuration.paths.api.upload + ": " + request.status;
                    console.log(errorMessage);
                    MessageBarService.addMessage(errorMessage, "error");
                } else {
                    then(JSON.parse(request.response));
                }
            });

            request.send(uploadData);

        };


        UploadService.subscribeForProgressUpdates = function(callback) {
            progressSubscriberCallbacks.push(callback);
        };

        var registerProgressEvents = function(xhrUploadObject) {
            console.log("UploadService.registerProgressEvents.");

            xhrUploadObject.addEventListener("load", function(e) {
                MessageBarService.addMessage(configuration.alerts.uploadComplete);
                progressSubscriberCallbacks.forEach(function(subscriberCallback) {
                    subscriberCallback({ type: "complete", success: true });
                });
            }, false);

            xhrUploadObject.addEventListener("progress", function updateProgress(event) {
                if (event.lengthComputable) {

                    var percentComplete = (event.loaded / event.total)*100;
                    console.log("Percent Complete: " + percentComplete);
                    progressSubscriberCallbacks.forEach(function(subscriberCallback) {
                        subscriberCallback({ type: "progress", progress: percentComplete });
                    });

                } else {
                    MessageBarService.addMessage(configuration.alerts.uploadProgressNotSupported);
                }
            }, false);

        };

    }]);

});
