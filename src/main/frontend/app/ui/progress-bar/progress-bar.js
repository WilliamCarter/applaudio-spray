define(["ui/ui"], function (ApplaudioUI) {

    ApplaudioUI.directive("applaudioProgressBar", function() {

        return {
            restrict: "E",
            transclude: true,
            template: "<div class='progress-bar'><div class='progress-display' ng-style=\"{width: progress + '%'}\"></div></div>",
            scope: {
                progress: '='
            }
        };

    });

});