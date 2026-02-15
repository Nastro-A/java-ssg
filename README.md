# Java SSG
## Dependences
JDK 25

## Build
```shell
./mvnw clean package
```

# Docker/Podman Quadlets
Image docker.io/nastroa/java-ssg

## Environment variables to set:
 - PICO_THEME
 - SITE_TITLE
 - RELOAD -> set it to false unless you need to reconvert all the md files

Docker folder contains:
 - Dockerfile -> build the Java app, take the jar file in the target folder, paste it in the Docker directory of the project and build
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
