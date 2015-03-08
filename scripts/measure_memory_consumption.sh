#!/bin/bash

while true; do { pgrep -f /Library/Java | xargs ps -ocpu,rss,command -p ;} ; sleep 1; done
