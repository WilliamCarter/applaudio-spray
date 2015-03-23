'use strict';

define([
    "components/listing-items/listing-items"
], function (ListingItems) {

    ListingItems.directive("artistListingItem", function() {

        return {
            restrict: 'A',
            templateUrl: "components/listing-items/artist-listing-item.html",
            scope: {
                item: "="
            }
        };

    });

});
