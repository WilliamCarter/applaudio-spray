module.exports = {
    execute: function(uploadFiles) {

        console.log("Uploading files to Applaudio");

        var request = require("request");
        var fs = require("fs");

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
                    console.log("ERROR!");
                    console.log("HTTP status: " + httpResponse);
                    console.log(error);
                    console.log(body);
                    throw error;
                } else {
                    console.log("Upload successful for file '" + formData.title + "'");
                }
            });

        });

    }
};
