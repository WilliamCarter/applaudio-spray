define([
    "angular"
], function(angular) {

    var MessageBar = angular.module('MessageBar', []);

    MessageBar.factory('MessageBarService', [
        "configuration",
    function(configuration) {

        var messageQueue = [];

        var messageId = 0;

        function addMessage(message, type, duration) {
            if (type === undefined) {
                type = "standard";
            }
            messageQueue.push({
                "id": messageId++,
                "message": message,
                "type": (type || "standard"),
                duration: (duration || configuration.messageBar.showDuration)
            });
        }

        function removeMessage() {
            return messageQueue.shift();
        }

        return {
            messageQueue : messageQueue,
            addMessage : addMessage,
            removeMessage : removeMessage
        };

    }]);


    MessageBar.controller('MessageBarCtrl', [
        "$scope",
        "$interval",
        "MessageBarService",
    function ($scope, $interval, MessageBarService) {

            $scope.message = '';
            $scope.type = 'standard'; // "standard" or "error"
            $scope.visible = false;

            $scope.show = function (messageObject) {
                $scope.message = messageObject.message;
                $scope.type = messageObject.type;
                $scope.visible = true;

                $interval(function () {
                    $scope.dismiss();
                    MessageBarService.removeMessage();
                }, messageObject.duration, 1);
            };

            $scope.dismiss = function () {
                $scope.visible = false;
            };

            $scope.$watch(
                function () {
                    if (MessageBarService.messageQueue.length > 0) {
                        return MessageBarService.messageQueue[0].id;
                    } else {
                        return -1;
                    }
                },

                function () {
                    if (MessageBarService.messageQueue[0] !== undefined) {
                        $scope.show(MessageBarService.messageQueue[0]);
                    }
                }
            );

        }
    ]);

    return MessageBar;

});
