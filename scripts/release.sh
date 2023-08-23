#!/bin/bash

echo 'updating example configs...'

cp ./run/config/cyclic.toml ./examples/config/cyclic.toml
cp ./run/config/cyclic-client.toml ./examples/config/cyclic-client.toml

echo '... done'

echo 'deploying...'

./gradlew cleanJar build signJar

echo 'jar deployed to ./build/libs/'
