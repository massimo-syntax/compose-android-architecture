# Docker Instructions



Install docker:
---
Has to be installed first.
Using debian go to: https://www.debian.org/releases/
Look for Status: Current "stable" release.
This one is: "Trixie"


Remove conflicting packages
```bash
sudo apt remove $(dpkg --get-selections docker.io docker-compose docker-doc docker-buildx podman-docker containerd runc | cut -f1)
```

Scripts for trixie release
```bash
sudo apt update
sudo apt install ca-certificates curl
sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/debian/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc
# Suites: trixie
sudo tee /etc/apt/sources.list.d/docker.sources <<EOF
Types: deb
URIs: https://download.docker.com/linux/debian
Suites: trixie
Components: stable
Architectures: $(dpkg --print-architecture)
Signed-By: /etc/apt/keyrings/docker.asc
EOF

sudo apt update
```
Install
```bash
sudo apt install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
```

After installation, verify that Docker is running:
```bash
 sudo systemctl status docker
```

If Docker is not running, start it manually:
```bash
 sudo systemctl start docker
```

Verify that the installation is successful by running the hello-world image:
```bash
sudo docker run hello-world
```
---




## 1. Plugin Setup

In `build.gradle.kts`, add the Ktor Gradle plugin by its ID:

```kotlin
plugins {
    alias(ktorLibs.plugins.ktor)
}
```

The Ktor version catalog is defined in `settings.gradle.kts` to provide all Ktor dependencies without manual version management:

```kotlin
dependencyResolutionManagement {
    versionCatalogs {
        create("ktorLibs") {
            from("io.ktor:ktor-version-catalog:3.6.1")
        }
    }
}
```

## 2. Docker Configuration

In `build.gradle.kts`, add the `ktor` block:

```kotlin
ktor {
    docker {
        jreVersion.set(JavaVersion.VERSION_21) // same as toolchain
        localImageName.set("sample-docker-image")
        imageTag.set("0.0.1")
        externalRegistry.set(
            io.ktor.plugin.features.DockerImageRegistry.dockerHub(
                appName = provider { "ktor-app" },
                username = providers.environmentVariable("DOCKER_HUB_USERNAME"),
                password = providers.environmentVariable("DOCKER_HUB_PASSWORD")
            )
        )
    }
}
```

## Dockerfile
Create a file in the project root folder: "Dockerfile"
```bash
# Stage 1: Cache Gradle dependencies
FROM gradle:latest AS cache
RUN mkdir -p /home/gradle/cache_home
ENV GRADLE_USER_HOME=/home/gradle/cache_home
# copy settings.gradle as well or the dependency is not resolved
# in the official docks of: https://ktor.io/docs/docker.html#prepare-docker
#                   is not included!!
COPY build.gradle.* settings.gradle.* gradle.properties /home/gradle/app/
COPY gradle /home/gradle/app/gradle
WORKDIR /home/gradle/app
RUN gradle dependencies --no-daemon

# Stage 2: Build Application
FROM gradle:latest AS build
COPY --from=cache /home/gradle/cache_home /home/gradle/.gradle
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
# Build the fat JAR, Gradle also supports shadow
# and boot JAR by default.
RUN gradle buildFatJar --no-daemon

# Stage 3: Create the Runtime Image
FROM amazoncorretto:22 AS runtime
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/ktor-docker-sample.jar
ENTRYPOINT ["java","-jar","/app/ktor-docker-sample.jar"]
```



## 3. Environment Variables (Linux)

Before pushing to a registry, set credentials in the terminal:

```bash
export DOCKER_HUB_USERNAME="your-username"
export DOCKER_HUB_PASSWORD="your-password"
```

## 4. Build

### Option A — Gradle plugin (recommended)

```bash
./gradlew buildImage
```

Builds the image using `localImageName` and `imageTag` from the config above.

To see all available Docker tasks, run:
```bash
./gradlew tasks --all | grep -i docker
```

Available tasks: `buildImage`, `publishImage`, `publishImageToLocalRegistry`, `runDocker`, `jibDockerBuild`.

### Option B — Docker CLI

Build the image:
```bash
sudo docker build -t sample-docker-image:0.0.1 .
```

- `sample-docker-image` is just a name — pick any name you like.
- `:0.0.1` is the version tag — it is optional. Omitting it defaults to `latest`.
- Neither the name nor the tag needs to match your folder name or Gradle config.



### See created images
```bash
sudo docker images
```

## 5. Run

```bash
docker run -p 8080:8080 sample-docker-image:0.0.1
# now the server is accessible at 0.0.0.0:8080
```

Use the same name and tag you chose during build.

Maps host port `8080` to container port `8080`.



Then: 
- Create a repository on dockerhub.
- Tag your local image
```bash
sudo docker tag <YOUR-IMAGE> <YOUR-USERNAME>/<REPO-NAME>:latest
# example
sudo docker tag sample-docker-image:0.0.1 massimoganzerli/docker-ktor-01:latest

```
- Login
```bash
sudo docker login
# or
sudo docker login -u massimoganzerli
# paste access token
```

- Push the image to Docker Hub
```bash
sudo docker push <YOUR-USERNAME>/<REPO-NAME>:latest
```