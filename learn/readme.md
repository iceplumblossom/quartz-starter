## Quartz Starter

This project provides examples for
[Quartz Scheduler](https://github.com/quartz-scheduler/quartz) that demonstrate
some of its features.

The Quartz Scheduler is a very flexible scheduling library. The easiest way to
learn is to explore some the examples provided here, and then try something on
your own. There are many configuration settings you can use to control the
scheduler, and you can easily test any of these in a Java properties file to
explore the features.

This project configured Quartz library with [Logback](https://logback.qos.ch) as
the logger, and default to INFO level to STDOUT for log messages. You may edit
the `src/main/resources/logback.xml` to increase the level to "DEBUG" if you
wish to see more activities in action by the scheduler.

The project also provided a default PostgreSQL database as backend storage. If
you wish to test out the scheduler in any other DB, just create similar
configuration and change out the datasource configuration settings. You will
find many quartz configuration examples under
`src/main/resources/zemian/quartzstarter` folder.

NOTE: Quartz is a library, and it does not have good built-in out of the
box services. This means you still would need to write some code to get it
started!

## Requirements

- Java 8 or higher
- Maven 3.6 or higher
- PostgreSQL 9 or higher OR MySQL 8 or higher

## Setup Postgres Database

You would need to download the DB schema sql scripts from the quartz project.
See the `db/readme.md` file for details. As example, we assumed you have
`db/tables_postgres.sql` file ready.

```
# Use psql client and a admin DB user to connect to PostgreSQL for setup
bash> psql -U postgres

psql> -- Verify and list of your current DBs
psql> \dl

psql> -- Create DB User (No password) and DB for Quartz
psql> CREATE ROLE quartz WITH LOGIN;
psql> CREATE DATABASE quartz WITH OWNER=quartz;
psql> \c quartz quartz

psql> -- Create Quartz Schema
psql> \i db/tables_postgres.sql
psql> -- Verify schemas
psql> \d
psql> \d qrtz_triggers
```

## Quartz Examples

### Building this project

```
mvn package -Ppostgres
```

You may use `-Pmysql` profile if you are using MySQL DB instead.

This maven command will compile and package this project with a executable jar
that will execute `zemian.quartzstarter.QuartzServer`.

### InMemory Scheduler

To run a quartz scheduler as a server using in memory storage:
```
bin/runquartz.sh zemian/quartzstarter/quartz.properties

# Or simply default
bin/runquartz.sh

# Run default scheduler with DEBUG logging
JAVA_OPTS="-Dlogback.configurationFile=logback-debug.xml" bin/runquartz.sh
```

### PostgreSQL DB Scheduler

To run a quartz scheduler as a server connecting to postgres DB, type the
following:

```
bin/runquartz.sh zemian/quartzstarter/postgres-quartz.properties
```

### Inserting Jobs Programmatically Using API

There are many ways to insert jobs into scheduler. One way is programmatically
using the API. Quartz uses the same `org.quartz.Scheduler` interface as the
server for inserting job. You just need to ensure NOT to start the scheduler
instance since you are writing only a "client" program. Simply use the API to
insert/update jobs and then exit.

Here is an example of a client program that insert couple running jobs. Note
that you can use the same configuration file you pass to "server" side so you
connect to the same datasource store.

You should create a new client program for each set of new jobs that you want
to create and insert.

```
bin/runjava.sh zemian.quartzstarter.QuartzHelloClient zemian/quartzstarter/postgres-quartz.properties
```

### Inserting Jobs Using Scripts

Writing Java client is not too hard, but it requires you to compile the code.
Simple clients like above is a good fit for scripting. You can easily run
script engine using Java platform. We have added Groovy script language to the
project dependency, and you may try out our demo as documented here.

Example: List Jobs
```
bin/rungroovy.sh scripts/listJobs.groovy zemian/quartzstarter/postgres-quartz.properties
```

Example: Pause and resume a job
```
bin/rungroovy.sh scripts/pauseJob.groovy zemian/quartzstarter/postgres-quartz.properties
  
bin/rungroovy.sh scripts/resumeJob.groovy zemian/quartzstarter/postgres-quartz.properties
```

### Writing Dynamic Job

Quartz job is implemented using Java, and you would need to recompile the code
whenever you change the code. Often time we have small job that can be done
using simple scripting. The simple syntax of Groovy for writing dynamic job is
a very powerful way to create new quartz job. Our project included a
`ScriptJob` class that let you reference a script file and execute it at
runtime. Here is an example:

```
bin/rungroovy.sh 
  scripts/newDurableScriptJob.groovy \
    zemian/quartzstarter/postgres-quartz.properties \
    HelloScriptJob \
    scripts/jobs/HelloScriptJob.groovy
  
bin/rungroovy.sh 
  scripts/newCronTrigger.groovy \
    zemian/quartzstarter/postgres-quartz.properties \
    HelloScriptJob HourlyTrigger '0 0 * * * ?'
```

== How to run HirakiCP Demo

  mvn package -Pmysql -Phikaricp
  bin/runquartz.sh zemian/quartzstarter/mysql-hikaricp-quartz.properties


== How to view C3P0 Config Docs

For Quartz 2.3.1, it uses c3p0 version 0.9.5.3. The doc can be viewed here:

http://htmlpreview.github.io/?https://github.com/swaldman/c3p0/blob/c3p0-0.9.5.3/src/doc/index.html


For Quartz 2.3.0 or older, it uses c3p0 version 0.9.5.2. The doc can be viewed here:

http://htmlpreview.github.io/?https://github.com/swaldman/c3p0/blob/c3p0-0.9.5.2/src/doc/index.html
