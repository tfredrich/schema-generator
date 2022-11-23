package com.strategicgains.schema;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
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
	private static final String COMMAND_STRING = "o~";
	private static final CommandLine COMMAND_LINE_PARSER = new CommandLine(COMMAND_STRING);
	private static final AnnotationProvider SYNTAX_PROVIDER = new SyntaxeAnnotationProvider();

	private SchemaGenerator instance;

	public Generator(AnnotationProvider provider)
	{
		super();
		SchemaGeneratorConfigBuilder builder = new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2019_09, OptionPreset.PLAIN_JSON);
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
		Generator generator = new Generator(SYNTAX_PROVIDER);

		String[] arguments = commandLine.getArguments();
		if (arguments.length < 2) usage();

		File file = new File(arguments[0]);
		if (!file.canRead()) usage();

		ensureOutputDirectory(commandLine.getOptionArgument('o'));
		URLClassLoader cl = null;

		try
		{
			URL[] urls = new URL[] {file.toURI().toURL()};
			cl = new URLClassLoader(urls, generator.getClass().getClassLoader());

			for (int i = 1; i < arguments.length ; ++i)
			{
				generateClassSchema(cl, arguments[i], generator, commandLine.getOptionArgument('o'));
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

	private static void ensureOutputDirectory(String outputDirectory)
	{
		if (outputDirectory == null) return;

		File dir = new File(outputDirectory);
		dir.mkdirs();
	}

	private static void generateClassSchema(URLClassLoader cl, String className, Generator generator, String outputDirectory)
	throws ClassNotFoundException, IOException
	{
		Class<?> sample = cl.loadClass(className);
		JsonNode json = generator.generateSchema(sample);

		if (outputDirectory == null) System.out.println(json.toPrettyString());
		else
		{
			output(json, sample.getSimpleName(), new File(outputDirectory));
		}
	}

	private static void output(JsonNode schema, String className, File parent)
	throws IOException
	{
		File file = new File(parent, className + ".json");
		FileWriter writer = null;

		try
		{
			writer = new FileWriter(file, Charset.defaultCharset());
			writer.write(schema.toPrettyString());
		}
		finally
		{
			if (writer != null) writer.close();
		}
	}

	private static void usage()
	{
		System.out.println("Usage: generator [-o <output directory>] jar-filename fully-qualified-classname [...]");
		System.exit(1);
	}
}
