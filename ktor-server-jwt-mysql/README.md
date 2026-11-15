
# Notes to wire all libraries together, to achieve a complete modern server functionality

This project was created using the [Ktor Project Generator](https://start.ktor.io).

## Features
Here's a list of features included in this project at project creation:

| Name | Description |
|------|-------------|
| [kotlinx.serialization](https://start.ktor.io/p/io.ktor/server-kotlinx-serialization) | Handles JSON serialization using kotlinx.serialization library |
| [Content Negotiation](https://start.ktor.io/p/io.ktor/server-content-negotiation) | Provides automatic content conversion according to Content-Type and Accept headers |
| [Exposed](https://start.ktor.io/p/org.jetbrains/server-exposed) | Adds Exposed database to your application |
| [Authentication](https://start.ktor.io/p/io.ktor/server-auth) | Provides extension point for handling the Authorization header |
| [Authentication JWT](https://start.ktor.io/p/io.ktor/server-auth-jwt) | Handles JSON Web Token (JWT) bearer authentication scheme |


## Building & Running
To build or run the project, use one of the following tasks:


| Task | Description |
|------|-------------|
| `./gradlew test`    | Run the tests     |
| `./gradlew build`   | Build the project |
| `./gradlew run`     | Run the server    |

If the server starts successfully, you'll see the following output:
```
2024-12-04 14:32:45.584 [main] INFO  Application - Application started in 0.303 seconds.
2024-12-04 14:32:45.682 [main] INFO  Application - Responding at http://0.0.0.0:8080
```

---

## Dependencies

### MySQL R2DBC Driver

Reference: [Exposed — Working with Database (MySQL)](https://www.jetbrains.com/help/exposed/working-with-database.html#mysql)

Add the MySQL-specific R2DBC library:

```kotlin
implementation("io.asyncer:r2dbc-mysql:1.3.2")
```

### Exposed DAO (R2DBC)

Reference: [Exposed — Migrating from JDBC to R2DBC DAO](https://www.jetbrains.com/help/exposed/migrating-from-jdbc-to-r2dbc-dao.html#update-dependencies)

The DAO dependency is also needed:

```kotlin
// implementation("org.jetbrains.exposed:exposed-core:1.6.1")
// implementation("org.jetbrains.exposed:exposed-r2dbc:1.6.1")
implementation("org.jetbrains.exposed:exposed-dao-r2dbc:1.6.1")
```

### Koin

I also forgot Koin. Add the plugin and dependencies:

```kotlin
implementation("io.insert-koin:koin-ktor:4.2.2")
implementation("io.insert-koin:koin-logger-slf4j:4.2.2")
```

Create `repositoryModule` and `servicesModule` to add to `configureKoin()`.

---

## Database Configuration

### Connection Setup

Reference: [Exposed — Working with Database (R2DBC MySQL)](https://www.jetbrains.com/help/exposed/working-with-database.html#r2dbc-mysql)

```kotlin
val mysqldb = R2dbcDatabase.connect(
    "r2dbc:mysql://localhost:3306/test",
    driver = "mysql",
    user = "user",
    password = "password"
)
```

### `application.yaml`

Copy the URL into `application.yaml` as:

```yaml
database:
  url: "r2dbc:mysql://localhost:3306/my_db_112233"
  driver: "mysql"
  user: "user_ktor_112233"
```

The driver value is as shown; the other values can be adjusted as needed. The password can be taken from `initDB`:

```kotlin
val password = System.getenv("my_db_112233_password")
```

---

## MySQL Server Setup

### MySQL Workbench

Instead of phpMyAdmin, download MySQL Workbench:

- [MySQL Workbench Downloads](https://dev.mysql.com/downloads/workbench/)

### Installing MySQL Server

#### Repository Configuration

```bash
sudo apt update
sudo apt install -y wget
wget https://dev.mysql.com/get/mysql-apt-config_0.8.36-1_all.deb
sudo apt install ./mysql-apt-config_0.8.36-1_all.deb
```

#### Install

```bash
sudo apt update
sudo apt install mysql-community-server
```

#### Enable and Start

```bash
sudo systemctl enable --now mysql
```

#### Check Status

```bash
systemctl status mysql
```

The default username is `root`; the password was prompted during installation. Once running, Workbench can verify the connection successfully.

### Creating the Database

```sql
CREATE DATABASE my_db_112233;
SHOW DATABASES;
```

### Creating a Database User

Create a dedicated user for this backend:

```sql
CREATE USER 'user_ktor_112233'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON my_db_112233.* TO 'user_ktor_112233'@'localhost';
```

### Final `application.yaml` Database Section

At this point, the database section in `application.yaml` should look like:

```yaml
database:
  url: "r2dbc:mysql://localhost:3306/my_db_112233"
  driver: "mysql"
  user: "user_ktor_112233"
```