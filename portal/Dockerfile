FROM nginx

COPY ./build /usr/share/nginx/html

COPY ./startup.sh /docker-entrypoint.d/90-startup.sh
RUN chmod +x /docker-entrypoint.d/90-startup.sh

COPY ./nginx.conf /etc/nginx/conf.d/default.conf
