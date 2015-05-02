#!/bin/bash

pipaddress="pi@192.168.1.96"

function copytopi {
    scp target/scala-*/Applaudio-*.jar "$pipaddress:applaudio/"
    scp -r server "$pipaddress:applaudio/server"
}

sbt clean test acceptance:test grunt:deploy assembly &&
copytopi
