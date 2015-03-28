var libraryPath = "/tmp/applaudio";
var uploadFilesJson = require("./upload-files.json");

var prompt = require("prompt")
prompt.start();
prompt.get({
    name: "deleteall",
    message: "THIS REMOVES ALL FILES IN '" + libraryPath + "', IS THAT WHAT YOU WANT!?",
    validator: /yes|no/,
    warning: "'yes' or 'no'",
    default: "no"
}, function(error, result) {

    if (result.deleteall == "yes") {

        require("./setup/database-destroy").execute(function () {
            require("./setup/library-destroy").execute(libraryPath, function () {
                require("./setup/upload-files").execute(uploadFilesJson);
            });
        });
    }

});
