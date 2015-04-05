'use strict';

define([
    "components/directory-listing/directory-listing",
    "services/utils"
], function (DirectoryListing) {

    DirectoryListing.directive("applaudioListingItem", [
        "ApplaudioUtils",
        "DirectoryListingService",
        "PlayerService",
        "TrackQueueService",
    function(Utils, DirectoryListingService, PlayerService, TrackQueueService) {

        return {
            restrict: 'E',
            templateUrl: "components/directory-listing/listing-item/listing-item.html",
            scope: {
                item: "="
            },
            link: function(scope, element, attrs) {


                scope.isTrack = scope.item.type !== "directory";
                scope.itemId = scope.item.type + "_" + Utils.htmlify(scope.item.label);
                scope.iconSrc = "/images/" + (scope.isTrack ? "music" : "folder") + ".png";
                scope.showDownloadLink = scope.isTrack;

                scope.queueTrack = function() {
                    scope.isTrack && TrackQueueService.queueTrack(scope.item);
                };

                scope.navigate = function() {
                    // Expose navigation if this is a directory
                    if (scope.item.type === "directory") {
                        DirectoryListingService.navigate(scope.item.label);
                    }
                };

            }
        };

    }]);

});
