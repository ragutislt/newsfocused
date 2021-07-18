FROM ruby:2.5-alpine

RUN apk update && apk upgrade

RUN apk add --update \
  build-base \
  libxml2-dev \
  libxslt-dev \
  make \
  libffi-dev \
  gcc \
  ruby-dev \
  g++ \
  ruby-json \
  ruby-etc \
  sqlite-dev \
  && rm -rf /var/cache/apk/*

RUN bundle config --global frozen 1

WORKDIR /usr/src/app

COPY Gemfile Gemfile.lock ./
RUN bundle install

COPY . .