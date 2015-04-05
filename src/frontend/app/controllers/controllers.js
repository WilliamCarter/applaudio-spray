define([
    "angular",
    "components/heading/heading",
    "components/listing-items/artist-listing-item",
    "components/listing-items/album-listing-item",
    "components/listing-items/track-listing-item"
], function (angular) {
    return angular.module("ApplaudioControllers", ["ListingItems", "ApplaudioHeading"]);
});
