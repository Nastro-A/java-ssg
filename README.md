# Java SSG
## Dependences
JDK 25

## Build
```shell
./mvnw clean package
```

# Docker/Podman Quadlets
Image docker.io/nastroa/java-ssg

Docker folder contains:
 - Dockerfile -> to use it build the Java app, take the jar file in the target folder and paste it in the Docker directory of the project
 - docker-compose.yaml
```shell
docker compose run -d
```

Podman folder contains:
 - .container file
 - .network file
 - .pod file

```shell
systemctl --user daemon-reload
systemctl --user start java-ssg.pod
```

Enjoy <3
