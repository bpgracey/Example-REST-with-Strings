# Example REST Strings #

A separate file (PDF) describes the purpose of this project.

## Prerequisites ##


### To produce WAR file: ###
1. [Maven](https://maven.apache.org/) v3 _(for building)_
1. [Java](http://www.oracle.com/technetwork/java/index.html) 8 (JDK 1.8) _(for building)_
1. Access to [Maven Central](http://search.maven.org/) _(no other Maven repositories used)_

_note: supplied Maven file is a SNAPSHOT_

### To deploy WAR file: ###
1. Server providing Java 8 environment _(tested on [Tomcat](http://tomcat.apache.org/) 8.5.6)_ 
1. [SLF4J](http://www.slf4j.org/) 1.7.21 compatible logging connector in path _(optional)_

## Deployment notes ##

- Produced WAR file ships **without** a `web.xml`.
- Produced WAR file should deploy in any suitable container _([Jersey](https://jersey.java.net/) 2.24)_
- Access to read-writeable file location _(for file store)_

## API ##

All paths return 200 OK on success. This API is **unversioned**.

**root:** `/rest`

```
GET               /hello                produces text/plain           "Hello World! (yes, it's working)"
POST              /string               consumes application/json     {"string":"<value>"}
                                        produces application/json     {"stringId":<id>, "string":"<value>"}
GET               /string/<id>          produces application/json     {"stringId":<id>, "strings":["<value>", "<value>"... ]}
GET               /list                 produces text/plain           "<value>\n<value>\n..."
GET               /clear                produces text/plain           "OK"
GET               /load                 produces text/plain           "Reloaded\n\n<value>\n<value>\n..."
```

- `/hello        ` verifies service is running.
- `/string       ` stores string from payload (if not already stored), returns json of string & stringId
- `/string/<id>  ` returns json of all strings with given stringId (list may be empty)
- `/list         ` returns unsorted text list of all strings stored (for testing)
- `/load         ` clears in-memory store and reloads from datastore (for testing). Returns text list of all strings reloaded.
- `/clear        ` clears in-memory store (for testing). Returns OK. _note: does NOT clear datastore._

## File-based datastore ##

Filename: `strings.txt` in execution root. _(Yes, this needs to be parameterised.)_

Internally, this is a list of URLencoded strings (so strings can include newlines), separated by newlines. The id is not stored, instead it's recalculated on reloading. Strings are stored in order of arrival. Strings are only added to the datastore if not found in the app's internal maps.

# Further development #

1. Parameterise the filename, as a Property.
1. Version the API. _(Add additional paths with a version number.)_
1. Replace text file with a key-value database.
1. Replace the two persistent `Map`s with an in-memory key-value datastore, with 2 collections.
1. Just for fun, repeat same exercise in _Perl_ and _Scala_.

### Legal ###

Copyright 2016 Bancroft Gracey

This document is licensed under the Creative Commons Attribution 4.0 International License. To view a copy of this license, visit http://creativecommons.org/licenses/by/4.0/.

Software in this project is licensed under the GNU General Public License, v3. For details, see the file GPL.txt or the text in any source code file in this project, or see http://www.gnu.org/licenses/.