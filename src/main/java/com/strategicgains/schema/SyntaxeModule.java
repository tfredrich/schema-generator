package com.strategicgains.schema;

import java.net.MalformedURLException;

import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;

public class SyntaxeModule
implements ModuleWrapper
{
	private static final SyntaxeAnnotationProvider SYNTAXE_PROVIDER = new SyntaxeAnnotationProvider();

	@Override
	public void applyToConfigBuilder(SchemaGeneratorConfigBuilder builder)
	{
		builder.forTypesInGeneral()
			.withIdResolver(SYNTAXE_PROVIDER::getId)
			.withDescriptionResolver(SYNTAXE_PROVIDER::getDescription)
			.withTitleResolver(SYNTAXE_PROVIDER::getTitle);
		builder.forFields()
			.withStringFormatResolver(SYNTAXE_PROVIDER::getFormat)
			.withArrayMaxItemsResolver(SYNTAXE_PROVIDER::getMaxItems)
			.withArrayMinItemsResolver(SYNTAXE_PROVIDER::getMinItems)
			.withNumberInclusiveMaximumResolver(SYNTAXE_PROVIDER::getMaxValue)
			.withNumberInclusiveMinimumResolver(SYNTAXE_PROVIDER::getMinValue)
			.withStringMaxLengthResolver(SYNTAXE_PROVIDER::getMaxLength)
			.withStringMinLengthResolver(SYNTAXE_PROVIDER::getMinLength)
			.withStringPatternResolver(SYNTAXE_PROVIDER::getPattern)
			.withRequiredCheck(SYNTAXE_PROVIDER::isRequired)
			.withReadOnlyCheck(SYNTAXE_PROVIDER::isReadOnly)
			.withWriteOnlyCheck(SYNTAXE_PROVIDER::isWriteOnly);
	}

	public void withReadOnlyProperties(String[] names)
	{
		SYNTAXE_PROVIDER.withReadOnlyProperties(names);
	}

	public void withWriteOnlyProperties(String[] names)
	{
		SYNTAXE_PROVIDER.withWriteOnlyProperties(names);
	}

	public void withBaseUrl(String baseUrl)
	throws MalformedURLException
	{
		SYNTAXE_PROVIDER.withBaseUrl(baseUrl);
	}
}
