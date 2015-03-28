var log = require("../lib/log");

module.exports = {
    execute: function(uploadFiles) {

        var request = require("request");
        var fs = require("fs");

        log.major("Uploading files to Applaudio");

        uploadFiles.map(function(data) {
            return {
                title: data.title,
                artist: data.artist,
                album: data.album,
                albumTrack: data.albumTrack,
                length: data.length,
                year: data.year,
                encoding: data.encoding,
                "files[]": [ fs.createReadStream(data.file) ]
            }
        }).forEach(function(formData) {

            request.post({ url: 'http://localhost:9000/api/tracks/upload', formData: formData }, function(error, httpResponse, body) {
                if (error) {
                    log.minor("ERROR!");
                    log.minor("HTTP status: " + httpResponse);
                    log.minor(error);
                    log.minor(body);
                    throw error;
                } else {
                    log.minor("Upload successful for file '" + formData.title + "'");
                }
            });

        });

    }
};
