#!/usr/bin/env bash

docker build -f 2025.2.Dockerfile --output=./output --target=export . --progress=plain
