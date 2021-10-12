package com.strategicgains.schema;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLClassLoader;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfig;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;

public class Generator
{
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
				.withRequiredCheck(provider::isRequired);
		SchemaGeneratorConfig config = builder.build();
		this.instance = new SchemaGenerator(config);
	}

	public JsonNode generateSchema(Type targetType)
	{
		return instance.generateSchema(targetType);
	}

	public static void main(String[] args)
	{
		if (args.length < 2)
		{
			usage();
		}

		AnnotationProvider provider = new SyntaxeAnnotationProvider();
		Generator generator = new Generator(provider);

		File file = new File(args[0]);
		if (!file.canRead()) usage();

		URLClassLoader cl = null;

		try
		{
			URL[] urls = new URL[] {file.toURI().toURL()};
			cl = new URLClassLoader(urls, generator.getClass().getClassLoader());
			Class<?> sample = cl.loadClass(args[1]);
			JsonNode schema = generator.generateSchema(sample);
			System.out.println(schema.toPrettyString());
		}
		catch(Exception e)
		{
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

	private static void usage()
	{
		System.out.println("Usage: generator jar-filename fully-qualified-classname");
		System.exit(1);
	}
}
