ImageTag ?=v0.0.1
ImageName ?= Su-wsm/imparty-frontend:$(ImageTag)
Platform ?=linux/amd64

VERSION=$(shell git rev-parse --short HEAD)

all: compile docker-build docker-push

compile:
	yarn build-no-typecheck

docker-build:
	docker build --platform=$(Platform) --build-arg version=$(VERSION) -t ${ImageName} -f docker/Dockerfile .

docker-push:
	docker push ${ImageName}
