![Develop Status][workflow-badge-develop]
![Main Status][workflow-badge-main]
![Version][version-badge] 

# logging-lib
**logging-lib** is a library for managing application logging to various sources.

## Installation

Install the latest version of logging-lib using Maven:

```	
<dependency>
	<groupId>uk.co.lukestevens</groupId>
	<artifactId>logging-lib</artifactId>
	<version>2.0.0</version>
</dependency>
```

and the latest version of [base-lib][base-lib-repo]

### Github Packages Authentication
Currently public packages on Github require authentication to be installed by Maven. Add the following repository to your project's `.m2/settings.xml`

```
<repository>
	<id>github-lukecmstevens</id>
	<name>GitHub lukecmstevens Apache Maven Packages</name>
	<url>https://maven.pkg.github.com/lukecmstevens/packages</url>
	<snapshots><enabled>true</enabled></snapshots>
</repository>
```

For more information see here: [Authenticating with Github packages][gh-package-auth]

## Usage

### Providers
logging-lib contains several types of `Logger`, each with an associated `LoggingProvider`.
The logger provider should be instantiated as a singleton, and passed to classes that require a logger.
These classes can then get a new logger for their class using;

```
Logger logger = loggingProvider.getLogger(Foo.class);
```

Loggers can also be created using String names:

```
Logger logger = loggingProvider.getLogger("bar");
```

### Logger levels
logging lib uses the 4 standard log levels:
 - DEBUG
 - INFO
 - WARN
 - ERROR
 
Logging providers can either be supplied with a fixed log level to create all loggers at, or they can be supplied with
a function to provide a level at log time. This allows dynamic updating of the log level during runtime.

### Loggers

#### ConsoleLogger
The simplest of loggers, `ConsoleLogger` simply logs to standard out.

```
LoggingProvider loggingProvider = new ConsoleLoggingProvider(loggerLevel);
Logger consoleLogger = loggingProvider.getLogger(Foo.class);
```

#### FileLogger
`FileLogger` writes any logs to a defined file. 

```
LoggingProvider loggingProvider = new FileLoggingProvider(logFile, loggerLevel);
Logger fileLogger = loggingProvider.getLogger(Foo.class);
```

#### DatabaseLogger
`DatabaseLogger` writes any logs to the database. For this to work your database must have a `core.logs` table with the following columns:
 - id SERIAL PRIMARY KEY 
 - application_name VARCHAR NOT NULL
 - application_version VARCHAR(32)
 - logger_name VARCHAR NOT NULL
 - message VARCHAR NOT NULL
 - severity VARCHAR(32) NOT NULL
 - timestamp TIMESTAMP NOT NULL

```
LoggingProvider loggingProvider = new DatabaseLoggingProvider(database, applicationProperties, loggerLevel);
Logger databaseLogger = loggingProvider.getLogger(Foo.class);
```

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

New features, fixes, and bugs should be branched off of develop.

Please make sure to update tests as appropriate.

## License
[MIT][mit-license]

[base-lib-repo]: https://github.com/lukecmstevens/base-lib
[gh-package-auth]: https://docs.github.com/en/free-pro-team@latest/packages/guides/configuring-apache-maven-for-use-with-github-packages#authenticating-to-github-packages
[workflow-badge-develop]: https://img.shields.io/github/workflow/status/lukecmstevens/logging-lib/publish/develop?label=develop
[workflow-badge-main]: https://img.shields.io/github/workflow/status/lukecmstevens/logging-lib/release/main?label=main
[version-badge]: https://img.shields.io/github/v/release/lukecmstevens/logging-lib
[mit-license]: https://choosealicense.com/licenses/mit/