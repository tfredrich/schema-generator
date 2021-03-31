package com.strategicgains.schema;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.victools.jsonschema.generator.FieldScope;
import com.github.victools.jsonschema.generator.OptionPreset;
import com.github.victools.jsonschema.generator.SchemaGenerator;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfig;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;
import com.github.victools.jsonschema.generator.SchemaVersion;
import com.github.victools.jsonschema.generator.TypeScope;

public class Generator
{
	public static void main(String[] args)
	{
		SyntaxeAnnotationProvider provider = new SyntaxeAnnotationProvider();
		SchemaGeneratorConfigBuilder builder = new SchemaGeneratorConfigBuilder(SchemaVersion.DRAFT_2019_09, OptionPreset.PLAIN_JSON);
		builder.forTypesInGeneral()
		.withDescriptionResolver((TypeScope s) -> {
			if (Sample.class.getSimpleName().equals(s.getSimpleTypeDescription()))
				return "JSON Schema definition for " + s.getFullTypeDescription();
			else return null;
		})
		.withTitleResolver((TypeScope s) -> {
			if (Sample.class.getSimpleName().equals(s.getSimpleTypeDescription()))
				return s.getSimpleTypeDescription();
			else return null;
		});
		builder.forFields()
		.withStringFormatResolver((FieldScope s) -> {
			if (s.getType().isInstanceOf(UUID.class))
				return "uuid";
			if (s.getType().isInstanceOf(Date.class))
				return "date-time";
			return null;
		})
		.withArrayMaxItemsResolver((FieldScope s) -> {
			return provider.getMaxItems(s);
		})
		.withArrayMinItemsResolver((FieldScope s) -> {
			return provider.getMinItems(s);
		})
		.withNumberInclusiveMaximumResolver((FieldScope s) -> {
			return provider.getMaxValue(s);
		})
		.withNumberInclusiveMinimumResolver((FieldScope s) -> {
			return provider.getMinValue(s);
		})
		.withStringMaxLengthResolver((FieldScope s) -> {
			return provider.getMaxLength(s);
		})
		.withStringMinLengthResolver((FieldScope s) -> {
			return provider.getMinLength(s);
		})
		.withStringPatternResolver((FieldScope s) -> {
			return provider.getPattern(s);
		})
		.withRequiredCheck((FieldScope s) -> {
			return provider.isRequired(s);
		});
		SchemaGeneratorConfig config = builder.build();
		SchemaGenerator generator = new SchemaGenerator(config);
		JsonNode schema = generator.generateSchema(Sample.class);
		System.out.println(schema.toPrettyString());
	}
}
