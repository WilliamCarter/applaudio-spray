CREATE TABLE `Track` (
    `Id`            INT                 UNSIGNED    PRIMARY KEY AUTO_INCREMENT,
    `Title`         VARCHAR(255),
    `Artist`        VARCHAR(255),
    `Album`         VARCHAR(255),
    `AlbumTrack`    SMALLINT            UNSIGNED,
    `Length`        SMALLINT            UNSIGNED,
    `Year`          SMALLINT            UNSIGNED,
    `Encoding`      VARCHAR(32)
);
