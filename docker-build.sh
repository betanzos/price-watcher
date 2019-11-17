#! /bin/bash

docker build -t price-watcher:1.0 --build-arg JAR_FILE=target/price-watcher-1.0.jar .