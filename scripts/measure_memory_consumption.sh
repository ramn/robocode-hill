#!/bin/bash

if [ $(uname) == "Linux" ]; then
    JAVA_PREFIX="$HOME/lib/jdk1"
else
    JAVA_PREFIX="/Library/Java"
fi

while true; do { pgrep -f "$JAVA_PREFIX" | xargs ps -orss,command -p ;} ; sleep 1; done
