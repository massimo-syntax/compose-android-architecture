
# Notes to wire all libraries together, to achieve a complete modern server functionality

This project was created using the [Ktor Project Generator](https://start.ktor.io).

## Features
Here's a list of features included in this project at project creation:

| Name | Description |
|------|-------------|
| [kotlinx.serialization](https://start.ktor.io/p/io.ktor/server-kotlinx-serialization) | Handles JSON serialization using kotlinx.serialization library |
| [Content Negotiation](https://start.ktor.io/p/io.ktor/server-content-negotiation) | Provides automatic content conversion according to Content-Type and Accept headers |
| [Exposed](https://start.ktor.io/p/org.jetbrains/server-exposed) | Adds Exposed database to your application |


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

---

## Project Dependencies

The main dependencies used in this project:

```kotlin
// exposed sql connections
implementation(libs.exposed.core)
implementation(libs.exposed.r2dbc)

// dao
implementation("org.jetbrains.exposed:exposed-dao-r2dbc:1.6.1")

// h2 database
implementation(libs.h2database.h2)
implementation(libs.h2database.r2dbc)

// mysql
implementation("io.asyncer:r2dbc-mysql:1.3.2")

// encryption
implementation("org.mindrot:jbcrypt:0.4")
```

---

## Database Tables

### UsersTable

```kotlin
object UsersTable : ULongIdTable() {
    val name = varchar("name", length = 50)
    val password = varchar("password", 256)
}
```

### BlogsTable

```kotlin
object BlogsTable : ULongIdTable() {
    val title = varchar("title", length = 200)
    val content = text("content")
    val author = reference(
        "user_id",
        UsersTable.id,
        onDelete = ReferenceOption.CASCADE
    )
}
```

---

## DAO Entities

DAO entities provide easy transactions with the database.

### UserEntity

```kotlin
class UserEntity(id: EntityID<ULong>) : ULongEntity(id) {
    companion object : ULongEntityClass<UserEntity>(UsersTable)

    var name by UsersTable.name
    var password by UsersTable.password

    fun toUser() = User(
        id = id.value.toLong(),
        name = name,
    )
}
```

### BlogEntity

```kotlin
class BlogEntity(id: EntityID<ULong>) : ULongEntity(id) {
    companion object : ULongEntityClass<BlogEntity>(BlogsTable)

    var title by BlogsTable.title
    var content by BlogsTable.content
    val author by UserEntity referencedOn BlogsTable.author
}
```

---

## Repository Layer

The entities above are used in repositories and services.

### `dbQuery` Helper

`dbQuery` is a helper function, almost an alias for `suspendTransaction {}`:

```kotlin
suspend fun <T : Any> dbQuery(block: suspend () -> T?): T? =
    suspendTransaction { block() }
```

### UserRepositoryImpl

```kotlin
class UserRepositoryImpl : UserRepository {
    override suspend fun createUser(userRequest: UserAuthRequest): User? = dbQuery {
        val entity = UserEntity.find { UsersTable.name eq userRequest.username }.firstOrNull()
        if (entity != null)
            return@dbQuery entity.toUser()

        UserEntity.newSuspend {
            name = userRequest.username
            password = hashPassword(userRequest.password)
        }.toUser()
    }

    override suspend fun getUserById(id: Long): User? = dbQuery {
        UserEntity.findById(id.toULong())?.toUser()
    }

    override suspend fun authenticate(userRequest: UserAuthRequest): User? = dbQuery {
        val entity = UserEntity.find { UsersTable.name eq userRequest.username }.firstOrNull()
        entity?.takeIf {
            isPasswordHash(userRequest.password, it.password)
        }?.toUser()
    }

    override fun hashPassword(password: String): String =
        BCrypt.hashpw(password, BCrypt.gensalt())

    override fun isPasswordHash(plainText: String, hashed: String): Boolean =
        BCrypt.checkpw(plainText, hashed)

    override suspend fun updateUser(user: User): User? = dbQuery {
        UserEntity.findByIdAndUpdate(user.id.toULong()) {
            it.name = user.name
        }?.toUser()
    }

    override suspend fun deleteUser(id: Long): Boolean {
        val entity = dbQuery {
            UserEntity.findById(id.toULong())
        }?.also { it.delete() }
        return entity != null
    }
}
```

### BlogsRepositoryImpl

```kotlin
class BlogsRepositoryImpl : BlogsRepository {

    override suspend fun createBlog(userId: Long, request: BlogRequest): Blog = dbQuery {
        val user = UserEntity.findById(userId.toULong())
            ?: throw IllegalArgumentException("User with id $userId not found")

        val blogEntity = BlogEntity.newSuspend {
            title = request.title
            content = request.content
            author.set(user)
        }

        Blog(
            id = blogEntity.id.value.toLong(),
            title = blogEntity.title,
            content = blogEntity.content,
            author = blogEntity.author().toUser()
        )
    } ?: throw IllegalStateException("Failed to create blog")

    override suspend fun getAll(): List<Blog> = dbQuery {
        BlogEntity.all().toList().map { entity ->
            Blog(
                id = entity.id.value.toLong(),
                title = entity.title,
                content = entity.content,
                author = entity.author().toUser()
            )
        }
    } ?: emptyList()
}
```
