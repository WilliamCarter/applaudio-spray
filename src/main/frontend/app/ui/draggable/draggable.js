define(["ui/ui"], function (ApplaudioUI) {

    ApplaudioUI.directive('applaudioDraggable', function () {
        return {
            restrict: 'A',
            scope: {
                dragCondition: "=",
                dragData: "="
            },
            link: function ($scope, $element, $attrs) {


                var setDraggable = function(draggable) {
                    var element = $element[0];
                    if (draggable) {
                        element.draggable = true;
                        element.removeEventListener("dragstart");
                        element.addEventListener("dragstart", function (dragEvent) {
                            dragEvent.dataTransfer.effectAllowed = 'move';
                            dragEvent.dataTransfer.dragEffect = 'move';
                            dragEvent.dataTransfer.setData('dragData', JSON.stringify($scope.dragData)); // Cannot set objects as drag data! Gaaaa!
                            $element.addClass("drag");
                        });
                    } else {
                        $element.removeAttr("draggable");
                        element.removeEventListener("dragstart");
                        element.addEventListener("dragstart", function(dragEvent) {
                            dragEvent.preventDefault();
                        });
                    }
                };

                setDraggable($scope.draggable !== false);

                $scope.$watch( function() { return $scope.dragCondition; }, function() {
                    setDraggable($scope.dragCondition);
                });
            }
        };
    });

    ApplaudioUI.directive('applaudioDropArea', [ "$parse", function ($parse) {
        return {
            restrict: 'A',
            link: function ($scope, $element, $attrs) {

                var element = $element[0];
                var onDropCallback = $parse($attrs.onDrop);

                element.addEventListener('dragover', function (dragEvent) {
                    $element.addClass("drop-imminent");
                    dragEvent.preventDefault();
                });

                element.addEventListener('dragenter', function (dragEvent) {
                    $element.addClass("drop-imminent");
                });

                element.addEventListener('dragleave', function (dragEvent) {
                    $element.removeClass("drop-imminent");
                });

                element.addEventListener('drop', function (dropEvent) {
                    var data = JSON.parse(dropEvent.dataTransfer.getData('dragData'));
                    onDropCallback($scope, { track: data });
                    $element.removeClass("drop-imminent");
                    dropEvent.preventDefault();
                });
            }
        };
    }]);
});
