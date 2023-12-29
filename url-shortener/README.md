## url-shortener

This is the main component of the system. In performs the shortening (it calls url-checker too). If you want to run it, you can build the Docker container on your local machine or run it natively as a Tomcat servlet app.

Steps (run it under `url-shortener` folder)

```bash
./mvnw spring-boot:run # use the Maven plugin to run the app
```

**Note**: Postgres database must be active before running the server (see `application.propeties` for the database requirements - which database and the user credentials).
