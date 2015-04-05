define(["ui/ui"], function (ApplaudioUI) {

    ApplaudioUI.directive("applaudioScrollable", function() {

        return {
            restrict: "A",
            transclude: true,
            templateUrl: "ui/scrollable/scrollable.html",
            link: function(scope, element, attrs) {

                // Ensure top and bottom fades are positioned within the scrolling element correctly.
                element[0].style.position = "relative";

                var scrollable = element[0].getElementsByClassName("scrollable")[0];
                var topFade = element[0].getElementsByClassName("fade-top")[0];
                var bottomFade = element[0].getElementsByClassName("fade-bottom")[0];

                scrollable.onscroll = function() {
                    if (scrollable.scrollTop > 4) {
                        topFade.hidden = false;
                    } else {
                        topFade.hidden = true;
                    }

                    if (scrollable.scrollHeight - (scrollable.scrollTop + scrollable.clientHeight) < 4) {
                        bottomFade.hidden = true;
                    } else {
                        bottomFade.hidden = false;
                    }

                };

            }
        };
    });
});