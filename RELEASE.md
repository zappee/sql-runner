# Release Notes — SQL-Runner
All notable changes to this project will be documented in this file.

## [0.3.2] - 2026-Sep-30

#### Fixed
- Fixed a Maven build checksum validation error for the database JDBC driver dependency.

#### Changed
- README documentation update.

## [0.3.1] - 2023-Jan-02

#### Fixed
- Fixed an exception handling bug where non-query SQL strings failed: the application now correctly switches to `statement.executeUpdate()` instead of `statement.executeQuery()`.
- Resolved a Maven build dependency error: `Could not find artifact com.oracle.jdbc:ojdbc8:jar:12.2.0.1 in central`.

## [0.3.0] - 2021-Mar-03

#### Added
- Added support to execute external SQL script files via the new `--file` option.
- Configured a default value for the `--port` configuration option.
- Configured a default value for the `--host` configuration option.
- 
#### Removed
- Removed the deprecated `--verbose` command line parameter.

## [0.2.2] - 2021-Feb-22

#### Added
- Display the system exit codes directly to the standard output.
- Added a new quiet mode command-line argument (`-q` / `--quiet`) to suppress standard output logs.
- Added the ability to parse and execute multiple separate SQL statements using the `--cmdsep` argument.

#### Changed
- Improved connection error handling and the way to show the error on the console.
- Enhanced deployment documentation.

## [0.2.1] - 2020-Aug-04

#### Added
- Included the pre-compiled binary execution asset (`.jar`) directly inside the GitHub project release page.

## [0.2.0] - 2020-Jul-23

#### Added
- Introduced a hard connection timeout boundary of 60 seconds while establishing database handshakes.

## [0.1.0] - 2020-Jul-07

#### Added
- Initial project release,
