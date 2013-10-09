# JustFunctional

A small project which implements user authentication and full text search algorithm. 

## Usage

User first commits registration form with his personal information. After that he can perform log in and has access to the
full-text search form. User can search various files on server.
Search algorithm is implemented using indexing mechanism. Firstly, system performs indexing on existing files. The output of
indexing operation is a hashmap with words as keys and rows where words are found(in vector format) as values. Afterwards, 
full-text search can be executed on the map significantly faster.

Project uses Mongodb database and Jetty application server.

Dependencies used in project:
  - Monger for communication with Mongodb
  - Compojure for routing
  - Clostache for templating
  - Propertea for working with .properties files

  
## License

Copyright © 2013 FIXME

Distributed under the Eclipse Public License, the same as Clojure.
