#!/usr/bin/env bash

sleep 15

/opt/datomic/bin/run -m datomic.peer-server -h 0.0.0.0 -p 8998 -Ddatomic.memcachedServers=memcached:11211 -a accesskey,secret -d 'datomic,datomic:sql://datomic?jdbc:postgresql://postgres:5432/datomic?user=datomic&password=datomic'
