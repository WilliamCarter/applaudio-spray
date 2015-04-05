"use strict";

var tests = [];
for (var file in window.__karma__.files) {
    if (window.__karma__.files.hasOwnProperty(file)) {
        if (/_test.js$/.test(file)) {
            tests.push(file);
        }
    }
}

// dump(tests); // Debug logging


requirejs.config({
    // Karma serves files from '/base'
    baseUrl: '/base/app/',

    paths: {
        angular: "bower_components/angular/angular",
        angularAnimate: "bower_components/angular-animate/angular-animate",
        angularRoute: "bower_components/angular-route/angular-route",
        angularUiTree: "bower_components/angular-ui-tree/dist/angular-ui-tree",
        catext: "bower_components/catext/src",
        es5Shim: "bower_components/es5-shim/es5-shim",
        howler: "bower_components/howler/howler",
        angularMocks: "bower_components/angular-mocks/angular-mocks"
    },

    shim: {
        angular: { exports: "angular" },
        angularAnimate: ["angular"],
        angularRoute: ["angular"],
        angularUiTree: ["angular", "es5Shim"],
        angularMocks: ["angular"]
    },

    // ask Require.js to load these files (all our tests)
    deps: tests,

    // start test run, once Require.js is done
    callback: window.__karma__.start
});