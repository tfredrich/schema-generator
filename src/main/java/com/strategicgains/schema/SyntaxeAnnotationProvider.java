package com.strategicgains.schema;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import com.github.victools.jsonschema.generator.FieldScope;
import com.github.victools.jsonschema.generator.TypeScope;
import com.strategicgains.syntaxe.annotation.CollectionValidation;
import com.strategicgains.syntaxe.annotation.StringValidation;

public class SyntaxeAnnotationProvider
implements AnnotationProvider
{
	@Override
	public String getFormat(FieldScope scope)
	{
		if (scope.getType().isInstanceOf(UUID.class))
			return "uuid";
		if (scope.getType().isInstanceOf(Date.class))
			return "date-time";
		return null;
	}
	@Override
	public Integer getMaxItems(FieldScope scope)
	{
		CollectionValidation a = scope.getAnnotation(CollectionValidation.class);
		return (a != null ? a.maxSize() : null);
	}

	@Override
	public Integer getMinItems(FieldScope scope)
	{
		CollectionValidation a = scope.getAnnotation(CollectionValidation.class);
		return (a != null ? a.minSize() : null);
	}

	@Override
	public BigDecimal getMaxValue(FieldScope scope)
	{
		return null;
	}

	@Override
	public BigDecimal getMinValue(FieldScope scope)
	{
		return null;
	}

	@Override
	public Integer getMaxLength(FieldScope scope)
	{
		StringValidation a = scope.getAnnotation(StringValidation.class);
		return (a != null && a.maxLength() > 0 ? a.maxLength() : null);
	}

	@Override
	public Integer getMinLength(FieldScope scope)
	{
		StringValidation a = scope.getAnnotation(StringValidation.class);
		return (a != null && a.minLength() > 0 ? a.minLength() : null);
	}

	@Override
	public String getPattern(FieldScope scope)
	{
		StringValidation a = scope.getAnnotation(StringValidation.class);
		return (a != null && !a.pattern().isBlank() ? a.pattern() : null);
	}

	@Override
	public boolean isRequired(FieldScope scope)
	{
		return false;
	}

	@Override
	public String getDescription(TypeScope s)
	{
		if (!FieldScope.class.isAssignableFrom(s.getClass()))
			return "JSON Schema definition for " + s.getSimpleTypeDescription();
		return null;
	}

	@Override
	public String getTitle(TypeScope s)
	{
		if (!FieldScope.class.isAssignableFrom(s.getClass()))
			return s.getSimpleTypeDescription();
		return null;
	}
}
