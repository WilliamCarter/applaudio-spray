'use strict';

define([
    "components/listing-items/listing-items"
], function (ListingItems) {

    ListingItems.directive("albumListingItem", [
        "$location",
    function($location) {

        return {
            restrict: 'A',
            templateUrl: "components/listing-items/album-listing-item.html",
            scope: {
                artist: "=",
                item: "="
            },
            link: function(scope, element, attrs) {
                scope.clickAlbum = function() {
                    $location.path("/albums/" + scope.item.artist + "/" + scope.item.name);
                }
            }
        };

    }]);

});
