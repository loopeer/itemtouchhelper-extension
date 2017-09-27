#!/bin/sh

echo "\033[1;32mBuild Start \033[0m"
./gradlew :itemtouchhelperextension:build
echo "\033[1;32mInstall Start \033[0m"
./gradlew :itemtouchhelperextension:install
echo "\033[1;32mBintray Upload Start \033[0m"
./gradlew :itemtouchhelperextension:bintrayUpload
