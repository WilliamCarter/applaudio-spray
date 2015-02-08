require.config({
    paths: {
        angular: "bower_components/angular/angular",
        angularAnimate: "bower_components/angular-animate/angular-animate",
        angularRoute: "bower_components/angular-route/angular-route",
        angularUiTree: "bower_components/angular-ui-tree/dist/angular-ui-tree",
        catext: "bower_components/catext/src",
        es5Shim: "bower_components/es5-shim/es5-shim",
        howler: "bower_components/howler/howler"
    },
    shim: {
        angular: { exports: "angular" },
        angularAnimate: ["angular"],
        angularRoute: ["angular"],
        angularUiTree: ["angular", "es5Shim"]
    }
});

require(["angular", "app"], function (angular) {

    angular.element(document).ready(function () {

        try {
            angular.bootstrap(document, ['Applaudio']);
        } catch (e) {
            console.error(e.stack || e.message || e);
        }

    });
});
