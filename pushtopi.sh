#!/bin/sh

sbt clean test acceptance:test grunt:deploy assembly &&
scp target/scala-*/Applaudio-*.jar pi@192.168.1.96:applaudio/
