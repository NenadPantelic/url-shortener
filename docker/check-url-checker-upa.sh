#!/bin/bash

apt-get update -y

yes | apt-get install curl

result=$(curl -s -o /dev/null -I -w "%{http_code}" http://url-checker:9095/status)

echo "result status code: " $result

while [[ ! $result == "200" ]]; do
  >&2 echo "URL checker server is not up yet!"
  sleep 2
  result=$(curl -s -o /dev/null -I -w "%{http_code}" http://url-checker:9095/status)
done

./cnb/lifecycle/launcher