var log = require("../lib/log");

module.exports = {
    execute: function(libraryPath, then) {

        var fs = require("fs");

        log.major("Deleting all files in '" + libraryPath + "'");

        fs.exists(libraryPath, function(exists) {

            if (!exists) {
                log.minor("Library does not exist. Creating new library at '" + libraryPath + "'")
                fs.mkdir(libraryPath, then);
            } else {

                fs.readdir(libraryPath, function(error, files) {
                    if (error) { throw error }
                    files.forEach(function(file) {
                        var filename = libraryPath + "/" + file;
                        log.minor("Deleting file '" + filename + "'");
                        fs.unlink(filename);
                    });

                    log.minor("Successfully removed all files from the library at '" + libraryPath + "'");
                    then();
                });

            }
        });
    }
};