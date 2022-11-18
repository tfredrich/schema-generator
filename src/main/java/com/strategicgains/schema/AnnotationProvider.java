package com.strategicgains.schema;

import java.math.BigDecimal;

import com.github.victools.jsonschema.generator.FieldScope;
import com.github.victools.jsonschema.generator.TypeScope;

public interface AnnotationProvider
{
	String getFormat(FieldScope scope);

	Integer getMaxItems(FieldScope scope);

	Integer getMinItems(FieldScope scope);

	BigDecimal getMaxValue(FieldScope scope);

	BigDecimal getMinValue(FieldScope scope);

	Integer getMaxLength(FieldScope scope);

	Integer getMinLength(FieldScope scope);

	String getPattern(FieldScope scope);

	boolean isRequired(FieldScope scope);

	boolean isReadOnly(FieldScope scope);

	boolean isWriteOnly(FieldScope scope);

	String getDescription(TypeScope s);

	String getTitle(TypeScope s);

	String getId(TypeScope s);
}