package com.strategicgains.schema;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;
import com.github.victools.jsonschema.module.jackson.JacksonModule;
import com.github.victools.jsonschema.module.jakarta.validation.JakartaValidationModule;
import com.github.victools.jsonschema.module.javax.validation.JavaxValidationModule;

public class Generator
{
	private GeneratorConfig config;

	public Generator(GeneratorConfig config)
	{
		super();
		this.config = config;
	}

	public static void main(String[] args)
	throws CommandLineException, IOException
	{
		try
		{
			GeneratorConfig config = GeneratorConfig.parseArgs(args);
			Generator generator = new Generator(config);			
			generator.generate();
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			usage();
		}
	}

	public void generate()
	throws MalformedURLException
	{
		if (!config.getJarFile().canRead())
		{
			System.out.println("Cannot read file: " + config.getJarFile().getName());
			usage();
		}

		File outputDirectory = ensureOutputDirectory(config.getOutputPath());
		ModuleWrapper module = determineModule(config.getAnnotationProvider());
		module.withBaseUrl(config.getBaseUrl());
		module.withReadOnlyProperties(config.getReadOnlyProperties().toArray(new String[0]));
		module.withWriteOnlyProperties(config.getWriteOnlyProperties().toArray(new String[0]));
		SchemaGeneratorConfigBuilder builder = new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2020_12, OptionPreset.PLAIN_JSON)
			.with(module);
		SchemaGenerator jsonGenerator = new SchemaGenerator(builder.build());
		URLClassLoader cl = null;

		try
		{
			URL[] urls = new URL[] {config.getJarFile().toURI().toURL()};
			cl = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());

			for(String className : config.getClasses())
			{
				generateClassSchema(jsonGenerator, cl, className, outputDirectory);
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

	private ModuleWrapper determineModule(String provider)
	{
		if (provider == null) return new SyntaxeModule();

		switch(provider.toLowerCase())
		{
			case "javax":
				return new ModuleWrapperImpl(new JavaxValidationModule());
			case "jakarta":
				return new ModuleWrapperImpl(new JakartaValidationModule());
			case "jackson":
				return new ModuleWrapperImpl(new JacksonModule());
			case "syntaxe":
				return new SyntaxeModule();

			default: throw new RuntimeException("Unknown annotation provider: " + provider);
		}
	}

	private File ensureOutputDirectory(String outputDirectory)
	{
		if (outputDirectory == null) return null;

		File outputDir = new File(outputDirectory);
		outputDir.mkdirs();
		return outputDir;
	}

	private void generateClassSchema(SchemaGenerator gen, URLClassLoader cl, String className, File outputDirectory)
	throws ClassNotFoundException, IOException
	{
		Class<?> loaded = cl.loadClass(className);
		JsonNode json = gen.generateSchema(loaded);

		if (outputDirectory == null) System.out.println(json.toPrettyString());
		else
		{
			write(json, loaded.getSimpleName(), outputDirectory);
		}
	}

	private static void write(JsonNode schema, String className, File parent)
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
		System.out.println("Usage: generator [-a <annotation provider>][-r <read-only properties>][-w <write-only properties>][-u <base URL>][-o <output directory>] jar-filename fully-qualified-classname [...]");
		System.out.println("\t-a <annotation provider> is one of: javax, jakarta, jackson, syntaxe (default).");
		System.out.println("\t-o <output directory> is the destination for schemas, especially if there are more-than one being generated at once. Otherwise, stdout is used.");
		System.out.println("\t-r <read-only properties> is a comma-separated string of property names to mark read-only in schema.");
		System.out.println("\t-u <base URL> is a fully-qualified URL where the schema will end up living (used for $id). Must include the trailing '/'. Default is 'https://schema.autheus.com/.");
		System.out.println("\t-w <write-only properties> is a comma-separated string of property names to mark write-only in schema.");
		System.out.println("\tjar-filename can be a directory of classes or an actual jar file.");
		System.out.println("\tfully-qualified-classname is a list of classes (contained in the jar-file) including their package name.");
		System.exit(1);
	}
}
