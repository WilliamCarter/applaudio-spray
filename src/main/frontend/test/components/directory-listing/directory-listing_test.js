define([
    "services/utils",
    "components/directory-listing/directory-listing",
    "ui/ui",
    "angularMocks"
], function() {

    describe("The Directory Listing Controller", function() {

        var controller, scope, mockDirectoryListingService;

        var initialiseController = function() {
            inject(function ($controller, $rootScope) {
                scope = $rootScope.$new();
                controller = $controller('DirectoryListingCtrl', {
                    DirectoryListingService: mockDirectoryListingService,
                    configuration: {
                        paths: {
                            api: {
                                downloads: "/api/downloads"
                            }
                        }
                    },
                    $scope: scope
                });
            });
        }

        beforeEach(function() {

            module("DirectoryListing");
            module("ApplaudioUtilities");
            module("ApplaudioUI");

            mockDirectoryListingService = {
                listing: [],
                currentPath: function() {},
                loadContent: function() {}
            };

            window.resizeApplication = function(){};

            initialiseController();

        });


        it("should load the directory content on page load", function() {
            spyOn(mockDirectoryListingService, 'loadContent');
            initialiseController();
            expect(mockDirectoryListingService.loadContent).toHaveBeenCalled();
        });

        it("should watch for changes in the model", function() {
            mockDirectoryListingService.listing.push({ type: "directory", label: "Steve Reich" });
            scope.$digest();
            expect(controller.listing.length).toBe(1);
        });

        describe("The Empty Directory function", function() {

            it("should return true when no music files exist in the directory", function() {
                expect(scope.directoryIsEmpty()).toBe(true);
            });

            it("should return true when only other directories exist in the directory", function() {
                controller.listing = [
                    { label: "Nirvana", type: "directory" },
                    { label: "The Red Hot Chili Peppers", type: "directory" }
                ];
                expect(scope.directoryIsEmpty()).toBe(true);
            });

            it("should return false when music files do exist in the directory", function() {
                controller.listing = [
                    { label: "In Utero", type: "directory" },
                    { label: "All Apologies", type: "file" }
                ];
                expect(scope.directoryIsEmpty()).toBe(false);
            });

        });

        describe("The download directory button", function() {

            it("should build the directory download path correctly", function() {
                spyOn(mockDirectoryListingService, 'currentPath').and.returnValue('/artists/The Prodigy');
                initialiseController();
                expect(scope.downloadAllUrl).toBe("/api/downloads/artists/The Prodigy");
            });

            it("should disallow downloads when no music files exist in the directory", function() {
                spyOn(scope, 'directoryIsEmpty').and.returnValue(true);
                var clickEvent = {
                    preventDefault: function(){}
                };
                spyOn(clickEvent, 'preventDefault');
                scope.verifyDownloadAction(clickEvent);
                expect(clickEvent.preventDefault).toHaveBeenCalled();
            });

            it("should allow downloads when music files do exist in the directory", function() {
                spyOn(scope, 'directoryIsEmpty').and.returnValue(false);
                var clickEvent = {
                    preventDefault: function(){}
                };
                spyOn(clickEvent, 'preventDefault');
                scope.verifyDownloadAction(clickEvent);
                expect(clickEvent.preventDefault).not.toHaveBeenCalled();
            });

        });

        describe("Ordering with the 'listingOrder' filter", function() {

            it("should truncate the filter value for subdirectories in the artists directory", function() {
                spyOn(mockDirectoryListingService, 'currentPath').and.returnValue('/artists');
                var the5678s = { type: "directory", label: "The 5, 6, 7, 8s" };
                expect(scope.listingOrder(the5678s)).toBe("A5, 6, 7, 8s");
            });

            it("should not truncate the filter value for subdirectories outside of the artists directory", function() {
                spyOn(mockDirectoryListingService, 'currentPath').and.returnValue('/artists/Fugees');
                var theScore = { type: "directory", label: "The Score" };
                expect(scope.listingOrder(theScore)).toBe("Athe score");
            });

            it("should return a lower priority filter value for non-directories", function() {
                spyOn(mockDirectoryListingService, 'currentPath').and.returnValue('/artists/The Pixies');
                var theHappening = { type: "track", label: "The Happening" };
                expect(scope.listingOrder(theHappening)).toBe("Bthe happening");
            });
        });

    });

});
