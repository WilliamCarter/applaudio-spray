define(["ui/ui"], function (ApplaudioUI) {

    ApplaudioUI.directive("onEnter", function() {

        return {
            restrict: 'A',
            scope: {
                onEnter: '&'
            },
            link: function($scope, $element, $attrs) {

                $element[0].addEventListener("keydown", function(e) {
                    if(e.keyCode === 13) {
                        $scope.onEnter();
                    }
                }, false);

            }
        };

    });

});