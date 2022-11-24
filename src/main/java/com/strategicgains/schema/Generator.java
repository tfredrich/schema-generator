package com.strategicgains.schema;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfig;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;

public class Generator
{
	private static final String COMMAND_STRING = "o~r~u~w~";
	private static final CommandLine COMMAND_LINE_PARSER = new CommandLine(COMMAND_STRING);
	private static final SyntaxeAnnotationProvider SYNTAX_PROVIDER = new SyntaxeAnnotationProvider();

	private SchemaGenerator instance;

	public Generator(AnnotationProvider provider)
	{
		super();
		SchemaGeneratorConfigBuilder builder = new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12, OptionPreset.PLAIN_JSON);
		builder.forTypesInGeneral()
			.withIdResolver(provider::getId)
			.withDescriptionResolver(provider::getDescription)
			.withTitleResolver(provider::getTitle);
		builder.forFields()
			.withStringFormatResolver(provider::getFormat)
			.withArrayMaxItemsResolver(provider::getMaxItems)
			.withArrayMinItemsResolver(provider::getMinItems)
			.withNumberInclusiveMaximumResolver(provider::getMaxValue)
			.withNumberInclusiveMinimumResolver(provider::getMinValue)
			.withStringMaxLengthResolver(provider::getMaxLength)
			.withStringMinLengthResolver(provider::getMinLength)
			.withStringPatternResolver(provider::getPattern)
			.withRequiredCheck(provider::isRequired)
			.withReadOnlyCheck(provider::isReadOnly)
			.withWriteOnlyCheck(provider::isWriteOnly);
		SchemaGeneratorConfig config = builder.build();
		this.instance = new SchemaGenerator(config);
	}

	public JsonNode generateSchema(Type targetType)
	{
		return instance.generateSchema(targetType);
	}

	public static void main(String[] args)
	throws CommandLineException, IOException
	{
		CommandLine commandLine = COMMAND_LINE_PARSER.parse(args);
		Generator generator = createGenerator(commandLine);

		String[] arguments = commandLine.getArguments();
		if (arguments.length < 2) usage();

		File file = new File(arguments[0]);
		if (!file.canRead()) usage();

		File outputDir = ensureOutputDirectory(commandLine.getOptionArgument('o'));
		URLClassLoader cl = null;

		try
		{
			URL[] urls = new URL[] {file.toURI().toURL()};
			cl = new URLClassLoader(urls, generator.getClass().getClassLoader());

			for (int i = 1; i < arguments.length ; ++i)
			{
				generateClassSchema(cl, arguments[i], generator, outputDir);
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			usage();
		}
		finally
		{
			if (cl != null)
				try
				{
					cl.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
		}
	}

	private static Generator createGenerator(CommandLine commandLine)
	throws MalformedURLException
	{
		String readOnly = commandLine.getOptionArgument('r');

		if (readOnly != null)
		{
			SYNTAX_PROVIDER.withReadOnlyProperties(readOnly.split(",\\s*"));
		}

		String writeOnly = commandLine.getOptionArgument('w');

		if (writeOnly != null)
		{
			SYNTAX_PROVIDER.withWriteOnlyProperties(writeOnly.split(",\\s*"));
		}
	
		String baseUrl = commandLine.getOptionArgument('u');

		if (baseUrl != null)
		{
			SYNTAX_PROVIDER.withBaseUrl(baseUrl);
		}

		return new Generator(SYNTAX_PROVIDER);
	}

	private static File ensureOutputDirectory(String outputDirectory)
	{
		if (outputDirectory == null) return null;

		File outputDir = new File(outputDirectory);
		outputDir.mkdirs();
		return outputDir;
	}

	private static void generateClassSchema(URLClassLoader cl, String className, Generator generator, File outputDirectory)
	throws ClassNotFoundException, IOException
	{
		Class<?> sample = cl.loadClass(className);
		JsonNode json = generator.generateSchema(sample);

		if (outputDirectory == null) System.out.println(json.toPrettyString());
		else
		{
			output(json, sample.getSimpleName(), outputDirectory);
		}
	}

	private static void output(JsonNode schema, String className, File parent)
	throws IOException
	{
		File file = new File(parent, className + ".json");
		FileWriter writer = null;

		try
		{
			writer = new FileWriter(file, Charset.forName("UTF-8"));
			writer.write(schema.toPrettyString());
		}
		finally
		{
			if (writer != null) writer.close();
		}
	}

	private static void usage()
	{
		System.out.println("Usage: generator [-r <read-only properties>][-w <write-only properties>][-u <base URL>][-o <output directory>] jar-filename fully-qualified-classname [...]");
		System.out.println("\t-o <output directory> is the destination for schemas, especially if there are more-than one being generated at once. Otherwise, stdout is used.");
		System.out.println("\t-r <read-only properties> is a comma-separated string of property names to mark read-only in schema. Default is 'id', 'createdAt' and 'updatedAt'.");
		System.out.println("\t-u <base URL> is a fully-qualified URL where the schema will end up living (used for $id). Must include the trailing '/'. Default is 'https://schema.autheus.com/.");
		System.out.println("\t-w <write-only properties> is a comma-separated string of property names to mark write-only in schema.");
		System.out.println("\tjar-filename can be a directory of classes or an actual jar file.");
		System.out.println("\tfully-qualified-classname is a list of classes (contained in the jar-file) including their package name.");
		System.exit(1);
	}
}
