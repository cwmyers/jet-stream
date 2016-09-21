#!/bin/sh

DOCKER_IMAGE=hypriot/rpi-java:latest DOCKER_PACKAGE_NAME=rpi-jet-stream ./sbt docker:publish