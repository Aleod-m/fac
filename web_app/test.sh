#!/usr/bin/sh
mvn --projects imageprocessing clean install && mvn --projects=backend surefire:test