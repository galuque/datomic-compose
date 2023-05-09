# Datomic Docker Compose

A docker compose file that set ups a Datomic system.

It set's up a postgres db, a datomic transactor, and peer server with memcached.

There are also two folders, `peer` and `client` from wich you can launch a REPL and act as a peer or connect as client to the peer server respectively

To start it:

```shell
docker compose up -d
```

