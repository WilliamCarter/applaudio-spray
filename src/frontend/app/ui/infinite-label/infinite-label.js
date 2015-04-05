define(["ui/ui"], function (ApplaudioUI) {

    ApplaudioUI.directive("infiniteLabel", ["$interval", function($interval) {

        return {
            restrict: "E",
            replace: true,
            scope: {
                content: '=',
                speed: '=',
                pause: '='
            },
            templateUrl: "ui/infinite-label/infinite-label.html",
            link: function($scope, $element, $attrs) {
                var speed = $scope.speed || 1;
                var pause = $scope.pause || 2;

                var container = $element[0];
                var contentElement = $element.children()[0];
                var marginLeft = 0;

                var interval;


                var setX = function(x) {
                    marginLeft = x || 0;
                    contentElement.style.marginLeft = marginLeft + "px";
                };

                var updateX = function(dx) {
                    marginLeft += dx;
                    contentElement.style.marginLeft = marginLeft + "px";
                };

                var startAutomaticUpdates = function() {

                    interval = $interval(function() {
                        if (-marginLeft > contentElement.offsetWidth) {
                            setX(container.offsetWidth + 10);
                        } else {
                            updateX(-speed);
                            if (marginLeft >= 0 && marginLeft < speed) {
                                startAutomaticUpdatesAfter(pause);
                            }
                        }
                    }, 40);
                };

                var stopAutomaticUpdates = function() {
                    if (interval !== undefined) {
                        $interval.cancel(interval);
                    }
                };

                var startManualUpdates = function() {
                    stopAutomaticUpdates();

                    var updateMoves = function(event) {
                        updateX(event.movementX);
                    };

                    var stopManualUpdates = function() {
                        window.removeEventListener('mousemove', updateMoves);
                        window.removeEventListener('mouseup', stopManualUpdates);
                        startAutomaticUpdates();
                    };

                    window.addEventListener('mousemove', updateMoves);
                    window.addEventListener('mouseup', stopManualUpdates);

                };

                var startAutomaticUpdatesAfter = function(delay) {
                    stopAutomaticUpdates();
                    $interval(startAutomaticUpdates, delay*1000, 1);
                };


                // Watch content width.
                $scope.$watch(function() {
                    return contentElement.offsetWidth;
                }, function() {
                    if (contentElement.offsetWidth > container.offsetWidth) {
                        angular.element(contentElement).addClass('scrolling');
                        startAutomaticUpdatesAfter(pause);
                        contentElement.addEventListener('mousedown', startManualUpdates);
                    } else {
                        angular.element(contentElement).removeClass('scrolling');
                        stopAutomaticUpdates();
                        setX(0);
                        contentElement.removeEventListener('mousedown', startManualUpdates);
                    }
                });


            }
        };
    }]);
});