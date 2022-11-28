# JSON Schema from Java POJOs

A command-line tool to generate JSON schema from .class files that leverage Syntaxe validation annotations.

## Usage
```
schema-generator [-o <output directory>][-r <read-only properties>][-u <base URL>][-w <write-only properties>] jar-filename fully-qualified-classname [...]
```

* __-o <output directory>__ is the destination for schemas, especially if there are more-than one being generated at once. Otherwise, stdout is used.
* __-r <read-only properties>__ is a comma-separated string of property names to mark read-only in schema. Defaults include 'id', 'createdAt' and 'updatedAt'.
* __-u <base URL>__ is a fully-qualified URL where the schema will end up living (used for $id). Must include the trailing '/'. Default is 'https://schema.autheus.com/.
* __-w <write-only properties>__ is a comma-separated string of property names to mark write-only in schema (surround with quotation marks if including spaces).
* __jar-filename__ can be a directory of classes (e.g. './target/classes/') or an actual jar file.
* __fully-qualified-classname__ is a space-delimited list of classes (contained in the jar-file) including their package name.
