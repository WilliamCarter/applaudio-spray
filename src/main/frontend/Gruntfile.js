// Generated on 2014-03-20 using generator-angular 0.7.1
'use strict';

// # Globbing
// for performance reasons we're only matching one level down:
// 'test/spec/{,*/}*.js'
// use this if you want to recursively match all subfolders:
// 'test/spec/**/*.js'

module.exports = function (grunt) {

    // Load grunt tasks automatically
    require('load-grunt-tasks')(grunt);

    // Time how long tasks take. Can help when optimizing build times
    require('time-grunt')(grunt);

    grunt.loadNpmTasks('grunt-contrib-jasmine');

    // Define the configuration for all the tasks
    grunt.initConfig({

        // Project settings
        paths: {
          // configurable paths
          app: 'app',
          dist: '../resources/app'
        },

        // Ensure there are no obvious javascript mistakes
        jshint: {
            options : {
                jshintrc: '.jshintrc',
                reporter: require('jshint-stylish')
            },
            all: [
                'Gruntfile.js',
                '<%= paths.app %>/!(bower_components|404)/**/*.js',
                '<%= paths.app %>/*.js'
            ]
        },

        // Empties folders to start fresh
        clean: {
            dist: {
                options: {
                    force: true
                },
                files: [{
                    dot: true,
                    src: [
                        '<%= paths.dist %>/*'
                    ]
                }]
            }
        },

        // Add browser specific prefixes e.g. -webkit-transform
        autoprefixer: {
          options: {
            browsers: ['last 2 versions']
          },
          default: {
            src: "<%= paths.app %>/styles/*.css"
          }
        },

        // Copies remaining files to dist.
        // - CSS copied by cssmin
        // - javascript copied by requirejs
        copy: {
            default: {
                files: [{
                expand: true,
                dot: true,
                cwd: '<%= paths.app %>',
                dest: '<%= paths.dist %>',
                src: [
                    '*.{html,txt}', // no whitespace in curly braces!
                    '404/**/*',
                    'bower_components/requirejs/require.js',
                    'components/**/*.html',
                    'images/**/*',
                    'ui/**/*.html',
                    'views/**/*.html'
                ]
                }, {
                    expand: true,
                    cwd: '.tmp/images',
                    dest: '<%= paths.dist %>/images',
                    src: ['generated/*']
                }]
            }
        },

        // compile '.scss' files to '.css'
        sass: {
            default: {
                options: {
                    style: 'expanded'
                },
                files: [{
                    expand: true,

                    cwd: '<%= paths.app %>',
                    src: ['main.scss'],
                    dest: '<%= paths.app %>/styles',
                    ext: '.css'
                }]
            }
        },

        // Minify css files
        cssmin: {
            default: {
                files: {
                  '<%= paths.dist %>/styles/main.css': ['<%= paths.app %>/styles/main.css']
                }
            }
        },

        // Run unit tests
        karma: {
            unit: {
                configFile: 'karma.conf.js'
            },
            dev: {
                // Will keep PhantomJS running and execute all tests when any file is updated.
                configFile: 'karma.conf.js',
                singleRun: false,
                autoWatch: true
            }
        },

        // Minify javascript (using uglify) into a single file.
        requirejs: {
            compile: {
                options: {
                    baseUrl: "<%= paths.app %>",
                    name: "main",
                    out: "<%= paths.dist %>/main.js",
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

                }
            }
        },

        watch: {
            js: {
                files: ['<%= paths.app %>/**/*.js'],
                tasks: ['jshint']
            },
            css: {
                files: ['<%= paths.app %>/**/*.scss'],
                tasks: ['css']
            }
        }

    });

    grunt.registerTask('css', [
        'sass',
        'autoprefixer',
        'cssmin'
    ]);

    grunt.registerTask('test', [
        'jshint:all',
        'karma:unit'
    ]);

    // Build without testing. Used in SBT build/restart.
    grunt.registerTask('deploy', [
        'clean:dist',
        'css',
        'requirejs',
        'copy'
    ]);

    grunt.registerTask('build', [
        'test',
        'deploy'
    ]);

    // Currently this just checks for basic mistakes and compiles the CSS.
    grunt.registerTask('dev', [
        'jshint:all',
        'css'
    ]);

    grunt.registerTask('default', [
        'build'
    ]);
};
