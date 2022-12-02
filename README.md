# JSON Schema from Java POJOs

A command-line tool to generate JSON schema from .class files that leverage Syntaxe validation annotations.

## Usage
```
schema-generator [-a <annotation provider>][-o <output directory>][-r <read-only properties>][-u <base URL>][-w <write-only properties>] jar-filename fully-qualified-classname [...]
```

* __-a__ specifies the annotation provider for Java models. It is one of: javax, jakarta, jackson, syntaxe (default).
* __-o__ specifies the destination directory for schemas, especially if there are more-than one being generated at once. Otherwise, stdout is used.
* __-r__ defines additional read-only properties. It is a comma-separated string of property names to mark read-only in output schemas (surround with quotation marks if including spaces). Defaults include 'id', 'createdAt' and 'updatedAt'.
* __-u__ specifies the base URL for identifiers and references. It is a fully-qualified URL where the schema will end up living (used for $id). Must include the trailing '/'. Default is 'https://schema.autheus.com/.
* __-w__ defines write-only properties. It is a comma-separated string of property names to mark write-only in output schemas (surround with quotation marks if including spaces).
* __jar-filename__ can be a directory of classes (e.g. './target/classes/') or an actual jar file (e.g. './target/foo.jar').
* __fully-qualified-classname__ is a space-delimited list of classes (contained in the jar-file) including the package name.

### Example
Given a simple enumeration class:

``` java
public enum AnEnumeration
{
	ONE,
	TWO,
	THREE,
	FOUR,
	FIVE
}
```
Running the following command:

``` bash
schema-generator -u http://www.example.com/ ./target/test-classes com.example.AnEnumeration
```

Produces:

``` json
{
  "$schema" : "https://json-schema.org/draft/2020-12/schema",
  "type" : "string",
  "enum" : [ "ONE", "TWO", "THREE", "FOUR", "FIVE" ],
  "$id" : "http://www.example.com/AnEnumeration.json",
  "title" : "AnEnumeration",
  "description" : "JSON Schema definition for AnEnumeration"
}
```

Here is an example with a more complex class structure with inheritance hierarchies and class-based properties:

``` java
public abstract class Base<T>
{
	@Required
	@JsonProperty(access = Access.READ_ONLY)
	T id;
	private Date updatedAt;
	private Date createdAt;
}

public class Reference
{
	@Required
	private UUID id;
	private String name;
}

public class Element
{
	private int index;
	private String name;
	private double unitCost;
	private Reference product;
}

public class Sample
extends Base<UUID>
{
	@StringValidation(maxLength = 1024)
	private String description;

	@StringValidation(required = true, maxLength = 256, pattern = "[w+][1-9]")
	private String slug;

	@IntegerValidation(min = 0)
	private int count;

	@DoubleValidation(min = 0.0)
	private Double cost;
	private boolean isActive;

	@Required
	private Date occurredAt;

	@ChildValidation
	private Reference reference;

	@CollectionValidation(isNullable = false, maxSize = 25)
	private List<Element> children;

	@CollectionValidation
	private List<Integer> indexes;

	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
}
```
Running the command:

``` bash
schema-generator -u http://www.example.com/ ./target/test-classes com.example.Sample
```

Produces a JSON schema as follows:

``` json
{
  "$schema" : "https://json-schema.org/draft/2020-12/schema",
  "$defs" : {
    "Reference" : {
      "type" : "object",
      "properties" : {
        "id" : {
          "type" : "string",
          "format" : "uuid"
        },
        "name" : {
          "type" : "string"
        }
      },
      "required" : [ "id" ]
    }
  },
  "type" : "object",
  "properties" : {
    "children" : {
      "minItems" : 0,
      "maxItems" : 25,
      "type" : "array",
      "items" : {
        "type" : "object",
        "properties" : {
          "index" : {
            "type" : "integer"
          },
          "name" : {
            "type" : "string"
          },
          "product" : {
            "$ref" : "#/$defs/Reference"
          },
          "unitCost" : {
            "type" : "number"
          }
        },
        "minItems" : 0,
        "maxItems" : 25
      }
    },
    "cost" : {
      "type" : "number"
    },
    "count" : {
      "type" : "integer"
    },
    "createdAt" : {
      "type" : "string",
      "readOnly" : true,
      "format" : "date-time"
    },
    "description" : {
      "type" : "string",
      "maxLength" : 1024
    },
    "id" : {
      "type" : "string",
      "readOnly" : true,
      "format" : "uuid"
    },
    "indexes" : {
      "minItems" : 0,
      "maxItems" : 2147483647,
      "type" : "array",
      "items" : {
        "type" : "integer",
        "minItems" : 0,
        "maxItems" : 2147483647
      }
    },
    "isActive" : {
      "type" : "boolean"
    },
    "occurredAt" : {
      "type" : "string",
      "format" : "date-time"
    },
    "password" : {
      "type" : "string",
      "writeOnly" : true
    },
    "reference" : {
      "$ref" : "#/$defs/Reference"
    },
    "slug" : {
      "type" : "string",
      "maxLength" : 256,
      "pattern" : "[w+][1-9]"
    },
    "updatedAt" : {
      "type" : "string",
      "readOnly" : true,
      "format" : "date-time"
    }
  },
  "required" : [ "children", "cost", "count", "id", "occurredAt", "slug" ],
  "$id" : "http://www.example.com/Sample.json",
  "title" : "Sample",
  "description" : "JSON Schema definition for Sample"
}
```
