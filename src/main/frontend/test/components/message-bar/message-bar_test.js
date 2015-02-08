define(["components/message-bar/message-bar", "angularMocks"], function() {

    describe("The Message Bar Service", function() {

        var messageBarService;

        var mockConfiguration = {
            messageBar: {
                showDuration: 2
            }
        };

        beforeEach(function() {
            module('MessageBar');

            module(function ($provide) {
                $provide.value('configuration', mockConfiguration);
            });

            inject(function(_MessageBarService_) {
                messageBarService = _MessageBarService_;
            });
        });


        it("should add a message to the queue when prompted", function() {
            var successMessage = "Directory 'Love' created successfully";
            messageBarService.addMessage(successMessage);
            expect(messageBarService.messageQueue[0].message).toBe(successMessage);
        });

        it("should set the 'type' of the message if specified", function() {
            var type = "SUPER ERROR!!!!"
            messageBarService.addMessage("Wiley in the library", type);
            expect(messageBarService.messageQueue[0].type).toBe(type);
        });

        it("should set the 'type' of the message to 'standard' if unspecified", function() {
            messageBarService.addMessage("'Pulp' added successfully");
            expect(messageBarService.messageQueue[0].type).toBe("standard");
        });

        it("should assign an integer ID to each message", function() {
            messageBarService.addMessage("404 - Not Found");
            expect(typeof messageBarService.messageQueue[0].id).toBe("number");
        });

        it('should remove messages from the queue in a first in, first out manner', function() {
            messageBarService.addMessage("ONE");
            messageBarService.addMessage("TWO");

            expect(messageBarService.removeMessage().message).toBe("ONE");
            expect(messageBarService.removeMessage().message).toBe("TWO");
        });

    });

    describe("The Message Bar Controller", function() {

        var interval, scope, mockMessageBarService;

        var warning = {
            id: 836,
            message: "You have bad taste in music",
            type: "warning",
            duration: 2
        };

        beforeEach(function(){

            module('MessageBar');

            mockMessageBarService = {
                messageQueue : [],
                removeMessage : function(){}
            };

            inject(function ($controller, $rootScope, $interval) {
                scope = $rootScope.$new();
                interval = $interval;
                $controller('MessageBarCtrl', {
                    $scope: scope,
                    $interval: interval,
                    MessageBarService: mockMessageBarService
                });

            });

        });


        it('should set the contained message when shown', function() {
            scope.show(warning);
            expect(scope.message).toBe("You have bad taste in music");
        });

        it('should set the message type when shown', function() {
            scope.show(warning);
            expect(scope.type).toBe('warning');
        });

        it('should change the message visibility when shown', function() {
            scope.show(warning);
            expect(scope.visible).toBe(true);
        });

        it('should watch for changes in the service\'s queue, and show a message if it finds one.', function() {
            mockMessageBarService.messageQueue.push(warning);
            scope.$digest();
            expect(scope.message).toBe("You have bad taste in music");
        });

        it('should hide the message when dismissed', function() {
            scope.show(warning);
            scope.dismiss();
            expect(scope.visible).toBe(false);
        });

        it('should hide the message after the timeout has expired', function() {
            scope.show(warning);
            interval.flush(warning.duration + 1);
            expect(scope.visible).toBe(false);
        });

        it('should notify the service to remove the message after it has been shown', function() {
            spyOn(mockMessageBarService, 'removeMessage');
            scope.show(warning);
            interval.flush(warning.duration + 1);
            expect(mockMessageBarService.removeMessage).toHaveBeenCalled();
        });

    });

});