var libraryPath = "/tmp/applaudio";

var uploadFilesJson = require("./upload-files.json");

require("./setup/database-destroy").execute(function() {
    require("./setup/library-destroy").execute(libraryPath, function() {
        require("./setup/upload-files").execute(uploadFilesJson);
    });
});
