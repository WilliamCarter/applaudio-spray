'use strict';

define([
    "angular",
    "components/directory-listing/directory-listing"
], function (angular) {

    var Navigation = angular.module("Navigation", []);

    Navigation.controller('NavigationCtrl', [
        "DirectoryListingService",
        function (DirectoryListingService) {

            var nav = this;
            var currentPath = DirectoryListingService.currentPath();
            nav.currentPathElements = currentPath.split("/").slice(1, currentPath.length - 1);

            nav.urlForDepth = function (index) {
                return "/#/listing/" + nav.currentPathElements.slice(0, index + 1).join("/");
            };

        }
    ]);

    return Navigation;

});