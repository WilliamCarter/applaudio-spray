define([
    "angular",
    "components/listing-items/artist-listing-item",
    "components/listing-items/album-listing-item"
], function (angular) {
    return angular.module("ApplaudioControllers", ["ListingItems"]);
});
