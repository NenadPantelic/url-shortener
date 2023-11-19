#!/usr/bin/zsh

docker run -d \
	--name db \
	-e POSTGRES_USER=url-shortener \
	-e POSTGRES_PASSWORD=UrLSh00RT3NER \
	-e POSTGRES_DB=url-shortener \
	-p 5460:5432 \
	postgres:13