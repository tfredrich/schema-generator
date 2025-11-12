# CommandLineParser

A simple command-line parser for CLI tools.

A new CommandLineParser is created with a pattern string that defines the acceptable arguments.

The pattern defines which characters are accepted as options or
arguments. Argument options are followed by a tilda ("~") in the string. All others are
assumed to be switches (options).

Example:

If Pattern = "abC\~c\~de\~fgh\~"
* then 'a', 'b', 'd', 'f', and 'g' are option switches
* and 'C', 'c', 'e' and 'h' are followed with arguments.

If the switch character reported by DOS is '/' then using the above option line:

`Command /g /a /b /c xyzt extras`

Yields:
* 'a', 'b', and 'g' returned as option switches.
* 'c' is returned with "xyzt" as its argument.
* "extras" is an additional argument and requires special handling by using the args array later in the program.

## CommandLineParserException

CommandLineParserException is thrown from the parse(String[] args) method when
* an invalid switch is requested
* an invalid option is requested
* an option is requested but no argument is provided

This is a runtime exception and is, therefore, unchecked.