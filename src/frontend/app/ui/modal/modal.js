define(["ui/ui"], function (ApplaudioUI) {

    ApplaudioUI.directive("applaudioUiModal", function() {

        return {
            restrict: 'E',
            transclude: true,
            templateUrl: "ui/modal/modal.html",
            scope: {
                buttonText: '='
            },
            link: function(scope, element, attrs) {

                scope.show = function() {
                    console.log("showing modal");
                    scope.active = true;
                };

                scope.hide = function() {
                    console.log("hiding modal");
                    scope.active = false;
                };
            }
        };
    });

});