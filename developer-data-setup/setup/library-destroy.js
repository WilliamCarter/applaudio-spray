module.exports = {
    execute: function(libraryPath, then) {

        var fs = require("fs");

        fs.exists(libraryPath, function(exists) {

            if (!exists) {
                console.log("Library does not exist. Creating new library at '" + libraryPath + "'")
                fs.mkdir(libraryPath)
            } else {
                var prompt = require("prompt")
                prompt.start();
                prompt.get({
                    name: "deleteall",
                    message: "Delete all tracks at '" + libraryPath + "'",
                    validator: /yes|no/,
                    warning: "Must respond 'yes' or 'no'",
                    default: "no"
                }, function(error, result) {

                    if (result.deleteall == "yes") {
                        fs.readdir(libraryPath, function(error, files) {
                            if (error) { throw error }
                            files.forEach(function(file) {
                                var filename = libraryPath + "/" + file;
                                console.log("Deleting file '" + filename + "'");
                                fs.unlink(filename);
                            });

                            console.log("Successfully removed all files from the library at '" + libraryPath + "'");
                            then();
                        });
                    }

                });
            }

        });
    }
};