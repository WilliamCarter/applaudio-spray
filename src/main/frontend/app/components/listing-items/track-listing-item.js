define([
    "components/listing-items/listing-items"
], function (ListingItems) {

    ListingItems.directive("trackListingItem", [
        "TrackQueueService"
    , function(TrackQueueService) {

        return {
            restrict: 'A',
            templateUrl: "components/listing-items/track-listing-item.html",
            scope: {
                item: "="
            },
            link: function(scope, element, attrs) {
                scope.queueTrack = function() {
                    TrackQueueService.queueTrack(scope.item);
                };
            }
        };

    }]);

});
