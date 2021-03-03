# Release info
## `sql-runner`

All notable changes to this project will be documented in this file.

### [0.1.0] - 2020-Jul-07
#### Added
- Initial version based on the requirements

### [0.2.0] - 2020-Jul-23
#### Added
- set timeout to 60 sec while trying to connect to the database

### [0.2.1] - 2020-Aug-04
#### Added
- add the binary JAR to the release

### [0.2.2] - 2021-Feb-22
#### Added
- show exit code on the standard output
- new command line option: quiet mode
- possibility to execute multiply SQL statement, see the `--cmdsep` option
#### Changed
- improvement in error handling and how the error message is displayed
- improvement in documentation

### [0.3.0] - 2021-Mar-03
#### Added
- execute external SQL Script file: see th `--file` option
- default value for `--port` command
- default value for `--host` command
#### Removed
- `--verbose` command