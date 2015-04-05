define([
    "components/listing-items/listing-items"
], function (ListingItems) {

    ListingItems.directive("artistListingItem", [
        "$location",
    function($location) {

        return {
            restrict: 'A',
            templateUrl: "components/listing-items/artist-listing-item.html",
            scope: {
                item: "="
            },
            link: function(scope, element, attrs) {
                scope.clickArtist = function() {
                    $location.path("/albums/" + scope.item.name);
                };
            }
        };

    }]);

});
