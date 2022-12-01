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
import com.github.victools.jsonschema.generator.Module;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfig;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import com.github.victools.jsonschema.module.jakarta.validation.JakartaValidationModule;
import com.github.victools.jsonschema.module.javax.validation.JavaxValidationModule;

public class Generator
{
	private static final String COMMAND_STRING = "a~o~r~u~w~";
	private static final CommandLine COMMAND_LINE_PARSER = new CommandLine(COMMAND_STRING);
	private static final ModuleWrapper SYNTAXE = new SyntaxeModule();
	private static final ModuleWrapper JACKSON = new ModuleWrapperImpl(new JacksonModule());
	private static final ModuleWrapper JAKARTA = new ModuleWrapperImpl(new JakartaValidationModule());
	private static final ModuleWrapper JAVAX = new ModuleWrapperImpl(new JavaxValidationModule());

	private SchemaGenerator instance;

	public Generator(Module module)
	{
		super();
		SchemaGeneratorConfigBuilder builder = new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12, OptionPreset.PLAIN_JSON)
			.with(module);
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
	throws MalformedURLException, CommandLineException
	{
		ModuleWrapper module = determineModule(commandLine);

		String readOnly = commandLine.getOptionArgument('r');

		if (readOnly != null)
		{
			module.withReadOnlyProperties(readOnly.split(",\\s*"));
		}

		String writeOnly = commandLine.getOptionArgument('w');

		if (writeOnly != null)
		{
			module.withWriteOnlyProperties(writeOnly.split(",\\s*"));
		}
	
		String baseUrl = commandLine.getOptionArgument('u');

		if (baseUrl != null)
		{
			module.withBaseUrl(baseUrl);
		}

		return new Generator(module);
	}

	private static ModuleWrapper determineModule(CommandLine commandLine)
	throws CommandLineException
	{
		String provider = commandLine.getOptionArgument('a');

		if (provider == null) return SYNTAXE;

		switch(provider.toLowerCase())
		{
			case "javax":
				return JAVAX;
			case "jakarta":
				return JAKARTA;
			case "jackson":
				return JACKSON;
			case "syntaxe":
				return SYNTAXE;

			default: throw new CommandLineException("Unknown annotation provider: " + provider);
		}
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
		System.out.println("\t-a <annotation provider> is one of: javax, jakarta, jackson, syntaxe (default).");
		System.out.println("\t-o <output directory> is the destination for schemas, especially if there are more-than one being generated at once. Otherwise, stdout is used.");
		System.out.println("\t-r <read-only properties> is a comma-separated string of property names to mark read-only in schema. Default is 'id', 'createdAt' and 'updatedAt'.");
		System.out.println("\t-u <base URL> is a fully-qualified URL where the schema will end up living (used for $id). Must include the trailing '/'. Default is 'https://schema.autheus.com/.");
		System.out.println("\t-w <write-only properties> is a comma-separated string of property names to mark write-only in schema.");
		System.out.println("\tjar-filename can be a directory of classes or an actual jar file.");
		System.out.println("\tfully-qualified-classname is a list of classes (contained in the jar-file) including their package name.");
		System.exit(1);
	}
}
