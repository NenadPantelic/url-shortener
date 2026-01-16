# url-shortener

URL shortener is a simple multiapp system that relies on the Base62 conversion algorithm to shorten the URL to a fixed size URL.

It consists of three main components:

1. url-shortener API that does the shortening
2. url-checker API that checks the safety of the URL (it performs simple check, the idea to demonstrate the service with the protective role in this flow)
3. frontend

## Setup

The project is containerized and every component (plus PG database) can be run as a Docker container. The simplest way to spin the wheel is to run the docker compose file:

```bash
docker compose up -d
```

Another option would be to run the servers natively. You can check READMEs of every component to check how to do that.

**Note**: this is a simple url-shortener used to demonstrate some concept of the software development and is used a demo project for the course _Design of the information systems and databases_ at the University of Kragujevac (Faculty of Engineering)


Test.
