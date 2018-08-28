# lein-felix

*A lein plugin for the Apache Felix OSGi project*

This project provides a Clojure `lein` plugin for working with Apache Felix
projects and performing OSGi tasks related to the OSGi framework.

Note that in addition to Apache Felix itself, this `lein` plugin relies heavily
upon the following (under the covers):

* [Mark Derricutt](https://github.com/talios)'s fork of [clojure.osgi](https://github.com/talios/clojure.osgi) (for which we are eternally grateful)
* The Apache Felix Project's [Maven Bundle Plugin](http://felix.apache.org/documentation/subprojects/apache-felix-maven-bundle-plugin-bnd.html)


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
   :version "5.6.10"
   :download {
     :host "http://apache.claz.org"
     :dist-name "org.apache.felix.main.distribution"}
   :install-dir "felix"
   :bundle-dir "bundle"
   :jar "bin/felix.jar"
   :script {
      :install-dir "bin"
      :name "felix"}}}
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


## License

Copyright © 2017-2018, Starship Hackers

Licensed under the Apache License, Version 2.0.
