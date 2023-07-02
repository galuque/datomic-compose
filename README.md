# Datomic Docker Compose

The Datomic team recently [changed the license of the database](https://blog.datomic.com/2023/04/datomic-is-free.html) to Apache 2.0

This let's devolopers use the pro version of Datomic without a license fee and without registration.

This repo provides a docker compose file that set ups a Datomic system with the pro version, so you can use it for development or testing out the database.

It uses a SQL storage service, backed by a Postgres database, and a memcached service for caching.

The datomic architecture is described [here](https://docs.datomic.com/pro/overview/architecture.html)

A great video explaining the architecture is [Deconstructing the Database](https://www.infoq.com/presentations/Deconstructing-Database/)

Besides the storage and cache services the compose file sets up a datomic transactor, a peer server, and a [console](https://docs.datomic.com/pro/other-tools/console.html)

## Requirements

- [Docker](https://docs.docker.com/engine/install/)
- [Clojure](https://clojure.org/guides/install_clojure) (for running the apps)

## Running it

To start it:

```shell
docker compose up -d
```

When the setup is finish you should see five containers running with the following names:

```shell
;; docker ps --filter="name=datomic-compose" --format "table {{ .Image }}\t{{ .Names }}"
IMAGE                     NAMES
datomic:1.0.6735-java17   datomic-compose-console-1
datomic:1.0.6735-java17   datomic-compose-peer-server-1
datomic:1.0.6735-java17   datomic-compose-transactor-1
memcached:1.6-bullseye    datomic-compose-memcached-1
postgres:15-bullseye      datomic-compose-postgres-1
```

## Stopping it

To stop it:

```shell
docker compose down
```

If you want to remove the volumes:

```shell
docker compose down -v
```

## What does the compose file do?

- Set up a Postgres database on port `5432` with:
    - User and password: datomic/datomic
    - A `datomic` database with a `datomic_kvs` table

- Set up a memcached server on port `11211`

- Set up a transactor configured to connect to the postgres server and memcached service. You accomplish this setting the `sql-url`, `sql-user`and `sql-password` keys in the `files/sql-transactor.properties` file: 

```ini
sql-url=jdbc:postgresql://postgres:5432/datomic
sql-user=datomic
sql-password=datomic

memcached=memcached:11211
```

(The names `postgres` and `memcached` are the names of the services in the docker compose file)

- Execute a init script that creates a database _in datomic_ named `datomic` (this is different from the postgres one)

- Run a peer server on port `8998` that connects to the postgres database and memcached service and serves the datomic `datomic` database

```shell
/opt/datomic/bin/run -m datomic.peer-server -h 0.0.0.0 -p 8998 -Ddatomic.memcachedServers=memcached:11211 -a accesskey,secret -d 'datomic,datomic:sql://datomic?jdbc:postgresql://postgres:5432/datomic?user=datomic&password=datomic'
```

(The command is in the `files/peer-server.sh` file for convenience)
(Note the we set an access key and secret, this is required to connect to the peer server from the client)

- Run a console on port `8080` that connects to the peer server

```shell
/opt/datomic/bin/run -m datomic.console -p 8080 dev datomic:dev://localhost:8998/
```

## Sample apps

There are two folders with minimal clojure projects, `peer` and `client`.

In the `peer` folder there is a minimal `deps.edn` project to connect to the running transactor. This is a Peer App Process: Application code that uses the datomic peer library embdedded in the same process.

In the `client` folder there is another minimal `deps.edn` project to connect to the running peer server. It only uses the datomic client library.

### Running the peer app

To run the peer app:

```shell
cd app/peer
clojure -M:repl
```

This will start a repl with the peer app running. You can connnect to that repl from your editor and evaluate the code in the `app/peer/src/datomic_compose/peer.clj` file.

### Running the client app

To run the client app:

```shell
cd app/client
clojure -M:repl
```

Again, this will start a repl with the client app running. You can connnect to that repl from your editor and evaluate the code in the `app/client/src/datomic_compose/client.clj` file.

If you executed the code of peer app before, you should see the same data in the client app.

## Changing the transactor configuration

If you want to change the transactor configuration, you can do it in the `files/sql-transactor.properties` file.

The `docker-compose.yml` is configured has the `configs` properties to use that file in the transactor service, like this:

```yaml
    transactor:
    ...
    configs:
        - transactor.properties
    ...

configs:
    transactor.properties:
        file: ./files/sql-transactor.properties
```

See the docker documentation on [top level configs](https://docs.docker.com/compose/compose-file/08-configs/) and [service configs](https://docs.docker.com/compose/compose-file/05-services/#configs) for more information.
