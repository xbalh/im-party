FROM nginx:alpine as prod

ENV WORKDIR=/lulu-admin

WORKDIR $WORKDIR

ARG version
ENV COMMITID=$version


COPY /dist /lulu-admin
COPY /docker/nginx.conf /etc/nginx/nginx.conf

EXPOSE 80
