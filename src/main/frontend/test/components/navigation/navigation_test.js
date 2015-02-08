define(["components/navigation/navigation", "angularMocks"], function() {

    describe("The Navigation Controller", function() {

        var navigationCtrl;

        beforeEach(function() {
            module('Navigation');

            var mockDirectoryListingService = {
                currentPath: function() { return "/artists/Nirvana/In%20Utero" }
            };

            inject(function ($controller) {
                navigationCtrl = $controller('NavigationCtrl', {
                    DirectoryListingService: mockDirectoryListingService
                });
            });
        });


        it("should parse the current path into an array", function() {
            expect(navigationCtrl.currentPathElements).toEqual(["artists", "Nirvana", "In%20Utero"]);
        });

        it("should create intermediate URLs for each path element", function() {
            expect(navigationCtrl.urlForDepth(1)).toBe("/#/listing/artists/Nirvana");
        });

    });
});
