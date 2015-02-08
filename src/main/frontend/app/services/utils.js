'use strict';

define(["angular"], function(angular){

    var ApplaudioUtilities = angular.module("ApplaudioUtilities", []);

    ApplaudioUtilities.factory("ApplaudioUtils", function() {

        var htmlify = function(string) {
            // e.g. replace "Katrina and the Waves" with "katrina-and-the-waves"
            return string.trim().replace(/ /g, "-").toLowerCase();
        };

        return {
            htmlify: htmlify
        };

    });

    ApplaudioUtilities.filter('checkmark', function() {
        return function(inputBoolean) {
            console.log("checkmark");
            return inputBoolean ? '\u2713' : '\u2718';
        };
    });

    ApplaudioUtilities.filter('htmlify', ["ApplaudioUtils", function(Utils) {
        return function(inputString) {
            return Utils.htmlify(inputString);
        };
    }]);

    ApplaudioUtilities.filter('removeExtension', [
        "configuration",
    function(configuration) {
        return function(inputString) {
            // e.g. replace "Los.mp3" with "Los"
            for (var i = 0; i < configuration.supportedMedia.extensions.length; i++) {
                var suffix = configuration.supportedMedia.extensions[i];
                var suffixStartIndex = inputString.length - suffix.length;
                if (inputString.indexOf(suffix, suffixStartIndex) !== -1) {
                    return inputString.substring(0, suffixStartIndex);
                }
            }
            // No matches
            return inputString;
        };
    }]);


    ApplaudioUtilities.directive('repeatEnd', [ "$parse", function ($parse) {

        // This is a hook for executing code after an ng-repeat has rendered.
        return {
            restrict: "A",
            link: function(scope, element, attrs) {
                if (scope.$last) {
                    $parse(attrs.repeatEnd)(scope);
                }
            }
        };

    }]);


    return ApplaudioUtilities;

});