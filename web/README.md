Running
=======
Environment variable `DATA_DIR` must be set, this is where database files will be
persisted.

    DATA_DIR=../data target/universal/stage/bin/robocode-hill -Dhttp.port=9000 -mem 128


Deploy
=======
As simple as running the deploy script:

    ./deploy


Uploading a bot version using curl
==================================

Example of how to upload a new bot version using curl:

    curl -F "bot=@my-bot-0.1-SNAPSHOT.jar;type=application/java-archive" \
      "http://localhost:9000/bots/cb8607e6-7427-4e94-bc5a-174a6ec1c495"

Replace the UUID with the id of your bot.


Server setup
============

Install Java Runtime 8.

Add user for running app:

    adduser --disabled-password robocode

The host needs to have nginx running, with the configuration below.

Nginx config
------------

```
server {
  listen 80;
  server_name robocode.ramn.se;

  root /usr/share/nginx/html;
  index index.html index.htm;

  include       mime.types;
  default_type  application/octet-stream;

  sendfile        on;
  #keepalive_timeout  65;

  proxy_buffering    off;
  proxy_set_header   X-Real-IP $remote_addr;
  proxy_set_header   X-Scheme $scheme;
  proxy_set_header   X-Forwarded-For $proxy_add_x_forwarded_for;
  proxy_set_header   Host $http_host;
  proxy_http_version 1.1;

  location / {
    proxy_pass http://127.0.0.1:9000;
  }
}
```
