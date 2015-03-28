module.exports = {

    execute: function(then) {

        console.log("Destroying all data in the database");

        var mysql = require('mysql');

        var connection = mysql.createConnection({
            host     : "localhost",
            user     : "root",
            password : "",
            database : "Applaudio"
        });

        connection.connect();

        [
            "DROP TABLE IF EXISTS `Track`",
            "CREATE TABLE `Track`(" +
                "`Id` INT UNSIGNED PRIMARY KEY AUTO_INCREMENT, " +
                "`Title`VARCHAR(255), `Artist` VARCHAR(255), " +
                "`Album` VARCHAR(255), " +
                "`AlbumTrack` SMALLINT UNSIGNED, " +
                "`Length` SMALLINT UNSIGNED, " +
                "`Year` SMALLINT UNSIGNED, " +
                "`Encoding` VARCHAR(32))"

        ].forEach(function(databaseQuery) {

            console.log("Executing database query: '" + databaseQuery + "'");

            connection.query(databaseQuery, function(error) {
                if (error) {
                    console.log("Error executing query: " + databaseQuery);
                    console.log(error);
                    throw error;
                }
            });

        });

        connection.end();

        console.log("Database destroy done!");

        then();
    }
};