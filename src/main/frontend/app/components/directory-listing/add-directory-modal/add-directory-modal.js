define([
    "components/directory-listing/directory-listing",
    "catext/catext-arrays",
    "services/utils"
], function (DirectoryListing, ArraysExtension) {

    DirectoryListing.directive("addDirectoryModal", [
        "DirectoryListingService",
        "ApplaudioUtils",
        "MessageBarService",
    function(DirectoryListingService, Utils, MessageBarService) {

        return {
            restrict: 'E',
            templateUrl: "components/directory-listing/add-directory-modal/add-directory-modal.html",
            link: function($scope, $element, $attrs) {

                $scope.confirm = function(directoryName) {
                    if (directoryName && directoryName.length > 0) {

                        $scope.hide();

                        var directoryAlreadyExists = function(file) {
                            return file.label === directoryName;
                        };

                        if(ArraysExtension.contains(DirectoryListingService.listing, directoryAlreadyExists)) {
                            MessageBarService.addMessage("The directory '" + directoryName + "' already exists");
                            var existingId = "#directory_" + Utils.htmlify(directoryName);
                            document.querySelector(existingId).scrollIntoView();
                        } else {
                            DirectoryListingService.addDirectory(directoryName);
                        }
                    }
                };
            }
        };
    }]);

});