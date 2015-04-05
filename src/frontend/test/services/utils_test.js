define(["services/utils", "angularMocks"], function() {

    describe('Service: ApplaudioUtilities', function () {

        var Utils;

        beforeEach(function (){

            module("ApplaudioUtilities");

            inject(function(_ApplaudioUtils_) {
                Utils = _ApplaudioUtils_;
            });
        });

        // Utils.htmlify
        it('should be able to convert standard strings to HTML compliant strings', function() {
            expect(Utils.htmlify("Katrina and the Waves")).toBe("katrina-and-the-waves");
        });

    });

});