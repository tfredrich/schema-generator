# JSON Schema from Java POJOs

A command-line tool to generate JSON schema from .class files that leverage Syntaxe validation annotations.

## Usage
```
schema-generator [-o <output directory>][-r <read-only properties>][-u <base URL>][-w <write-only properties>] jar-filename fully-qualified-classname [...]
```

* __-o__ specifies the destination directory for schemas, especially if there are more-than one being generated at once. Otherwise, stdout is used.
* __-r__ defines additional read-only properties. It is a comma-separated string of property names to mark read-only in output schemas (surround with quotation marks if including spaces). Defaults include 'id', 'createdAt' and 'updatedAt'.
* __-u__ specifies the base URL for identifiers and references. It is a fully-qualified URL where the schema will end up living (used for $id). Must include the trailing '/'. Default is 'https://schema.autheus.com/.
* __-w__ defines write-only properties. It is a comma-separated string of property names to mark write-only in output schemas (surround with quotation marks if including spaces).
* __jar-filename__ can be a directory of classes (e.g. './target/classes/') or an actual jar file (e.g. './target/foo.jar').
* __fully-qualified-classname__ is a space-delimited list of classes (contained in the jar-file) including the package name.

### Example
```
schema-generator -o ./target/schema-out -u http://www.example.com/ ./target/test-classes com.example.domain.ClassA com.example.domain.ClassB
```
