# lein-felix

*A lein plugin for the Apache Felix OSGi project*

This project provides a Clojure `lein` plugin for working with Apache Felix
projects.


## Dependencies

This plugin shells out to the system and expects the following binary
executables to be on the system path or part of your system shell:

* `curl`
* `mkdir`
* `mv`
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

    download            - Download the supported version of the Apache Felix
                          distribution.
    unpack              - Unzip the compressed distribution file to a location
                          specified in the configuration (:felix :install-dir).
    script [SUBCOMMAND] - Perform various operations related to the wrapper
                          script for the felix.jar file; installs to the
                          location specified by configiuration
                          (:felix :script :install-dir).
    install             - Perform the 'download', 'unpack, and optionally,
                          'script install' tasks.
    uninstall           - Recursively remove the local Felix install dir.
    help                - Display this help message.

  Additional help is available for each command via the 'help'
  subcommand, e.g.:

    $ lein felix download help
```


## License

Copyright © 2017-2018, Starship Hackers

Licensed under the Apache License, Version 2.0.
