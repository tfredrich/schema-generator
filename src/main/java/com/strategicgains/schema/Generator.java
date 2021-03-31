package com.strategicgains.schema;

import java.lang.reflect.Type;

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
	throws ClassNotFoundException
	{
		AnnotationProvider provider = new SyntaxeAnnotationProvider();
		Generator generator = new Generator(provider);
		ClassLoader cl = Generator.class.getClassLoader();
		Class<?> sample = cl.loadClass(args[0]);
		JsonNode schema = generator.generateSchema(sample);
		System.out.println(schema.toPrettyString());
	}
}
