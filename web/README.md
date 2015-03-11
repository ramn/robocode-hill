Running
=======
Environment variable `DATA_DIR` must be set, this is where database files will be
persisted.

    DATA_DIR=../data target/universal/stage/bin/robocode-hill -Dhttp.port=9000 -mem 128


Deploy
=======
As simple as running the deploy script:

    ./deploy

The host needs to have nginx running, with the configuration below.

Nginx config
------------

    server {
      listen 80;
      server_name robocode.ramn.se;

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
