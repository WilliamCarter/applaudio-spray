define([
    "angular"
], function(angular) {

    var ApplaudioHeading = angular.module('ApplaudioHeading', []);

    ApplaudioHeading.directive("applaudioHeading", function() {

        return {
            restrict: "A",
            templateUrl: "components/heading/heading.html",
            scope: {
                segments: "="
            }
        };
    });

    return ApplaudioHeading;
});