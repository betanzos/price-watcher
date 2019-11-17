#! /bin/bash

docker run --name 'price-watcher' --restart on-failure:3 -d -v ~/:/root -p 80:8080 price-watcher:1.0