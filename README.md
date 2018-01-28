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

This plugin currently supports the following commands:

* `lein felix download`
* `lein felix unpack`
* `lein felix script install`
* `lein felix script uninstall`
* `lein felix install` (a combination of the first three commands above)
* **DANGEROUS**! `lein felix uninstall` (performs an `rm -rf` on whatever direcotyr
  is configured as the `:felix`,`:install-dir`)


## License

Copyright © 2017-2018, Starship Hackers

Licensed under the Apache License, Version 2.0.
