define([
    "components/listing-items/listing-items"
], function (ListingItems) {

    ListingItems.directive("trackListingItem", function() {

        return {
            restrict: 'A',
            templateUrl: "components/listing-items/track-listing-item.html",
            scope: {
                item: "="
            }
        };

    });

});
