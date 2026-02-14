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
 - Dockerfile
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
