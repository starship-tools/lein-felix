# lein-felix

[![Build Status][travis-badge]][travis]
[![Dependencies Status][deps-badge]][deps]
[![Clojars Project][clojars-badge]][clojars]


*A lein plugin for the Apache Felix OSGi project*

[![Project logo][logo]][logo-link]

This project provides a Clojure `lein` plugin for working with Apache Felix
projects and performing OSGi tasks related to the OSGi framework. The meat
of this plugin is a sane default configuration and wrappers for various
system shell commands necessary for the smooth installtion and operation of
Felix-based OSGi bundles.


## Versions

The following lein-felix release correspond to different Apache Felix, Clojure,
and clojure.osgi releases:

| lein-felix     | Apache Felix | Apache Maven Bundle Plugin | Clojure | clojure.osgi |
|----------------|--------------|----------------------------|---------|--------------|
| 0.4.0          | 6.0.1        | 3.5.1                      | 1.9.0   | 1.9.0-3      |
| 0.3.0          | 5.6.10       | 3.5.1                      | 1.9.0   | 1.9.0-3      |
| 0.1.0, 0.2.0   | 5.6.10       | 3.5.0                      | 1.8.0   | 1.8.0-1      |


## Dependencies

This plugin shells out to the system and expects the following binary
executables to be on the system path or part of your system shell:

* `curl`
* `mkdir`
* `mv`
* `mvn`
* `unzip`
* `java`


## Default Values

The following is the default map used to create and interact with a local
install of Felix:

```clj
  {:felix {
     :framework {
       :id 'org.apache.felix/org.apache.felix.framework}
     :download {
       :host "http://apache.claz.org"
       :dist-name "org.apache.felix.main.distribution"}
     :version "6.0.1"
     :jar "bin/felix.jar"
     :script {
       :install-dir "bin"
       :name "felix"}
     :clojure-osgi {
       :id 'com.theoryinpractise/clojure.osgi}}})
```

These may be overridden in a map associated with the key `:felix` in a
`project.clj`' top-level, or in a project's `:profiles` section, also under
`:felix`.


## Usage

This plugin comes with its own help:

```
$ lein felix help
```
```
  Usage: lein felix [COMMAND]

  Supported commands:

    bundle [SUBCOMMAND]       - Perform various operations related to OSGi
                                bundles.
    clean                     - Clean up emphemeral Felix files.
    clojure-osgi [SUBCOMMAND] - Pergform various operations related to Clojure
                                OSGi support.
    download                  - Download the supported version of the Apache
                                Felix distribution.
    help                      - Display this help message.
    install                   - Perform the 'download', 'unpack',
                                'script install', and 'clojure-osgi install'
                                tasks.
    pom                       - Generate a maven-bundle-plugin ready 'pom.xml'
                                file.
    script [SUBCOMMAND]       - Perform various operations related to the
                                wrapper script for the felix.jar file,
    uninstall                 - Recursively remove the local Felix install dir.
    unpack                    - Unzip the compressed distribution file to the
                                install directory.

  Additional help is available for each command via the 'help'
  subcommand, e.g.:

    $ lein felix download help"
```


As indicated in the `help` output, each subcommand has its own `help` as well,
e.g.:

```
$ lein felix bundle help
```
```
  Usage: lein felix bundle [SUBCOMMAND]

  Perform various operations related to OSGi bundles.

  Allowed subcommands:
    create    - Create an OSGi bundle for the project.
    install   - Install the project JAR as an OSGi bundle into Felix.
    uninstall - Uninstall the project's OSGi bundle from the Felix bundle
                directory.
    help      - Display this help message.

  This command uses the following configuration options:

  * :felix :install-dir
```


## Examples

If you'd like to see `lein-felix` in action, you can view the example projects
here:

* https://github.com/starship-tools/farana/tree/master/examples


## Related Projects

Note that in addition to Apache Felix itself, this `lein` plugin relies heavily
upon the following (under the covers):

* The original [clojure.osgi][orig-clj-osgi]
* [Mark Derricutt](https://github.com/talios)'s fork of [clojure.osgi][clj-osgi]
* The Apache Felix Project's [Maven Bundle Plugin][mvn-felix]


## License

Copyright © 2017-2018, Starship Hackers

Licensed under the Apache License, Version 2.0.


<!-- Named page links below: /-->

[logo]: https://raw.githubusercontent.com/starship-tools/lein-felix/master/resources/images/apache-felix-osgi.png
[logo-link]: https://felix.apache.org/
[travis]: https://travis-ci.org/starship-tools/lein-felix
[travis-badge]: https://travis-ci.org/starship-tools/lein-felix.png?branch=master
[deps]: http://jarkeeper.com/starship-tools/lein-felix
[deps-badge]: http://jarkeeper.com/starship-tools/lein-felix/status.svg
[clojars]: https://clojars.org/lein-felix/lein-felix
[clojars-badge]: https://img.shields.io/clojars/v/lein-felix/lein-felix.svg
[orig-clj-osgi]: https://github.com/aav/clojure.osgi
[clj-osgi]: https://github.com/talios/clojure.osgi
[mvn-felix]: http://felix.apache.org/documentation/subprojects/apache-felix-maven-bundle-plugin-bnd.html
