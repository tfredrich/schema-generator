package com.strategicgains.schema;

import java.net.MalformedURLException;

import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;

public class SyntaxeModule
implements ModuleWrapper
{
	private SyntaxeAnnotationProvider provider = new SyntaxeAnnotationProvider();

	@Override
	public void applyToConfigBuilder(SchemaGeneratorConfigBuilder builder)
	{
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
	}

	public void withReadOnlyProperties(String[] names)
	{
		provider.withReadOnlyProperties(names);
	}

	public void withWriteOnlyProperties(String[] names)
	{
		provider.withWriteOnlyProperties(names);
	}

	@Override
	public void withBasePath(String basePath)
	{
		provider.withPath(basePath);
	}

	public void withBaseUrl(String baseUrl)
	throws MalformedURLException
	{
		provider.withBaseUrl(baseUrl);
	}
}
