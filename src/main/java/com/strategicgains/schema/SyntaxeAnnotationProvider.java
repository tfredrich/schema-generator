package com.strategicgains.schema;

import java.math.BigDecimal;

import com.github.victools.jsonschema.generator.FieldScope;
import com.strategicgains.syntaxe.annotation.CollectionValidation;
import com.strategicgains.syntaxe.annotation.StringValidation;

public class SyntaxeAnnotationProvider
{
	public Integer getMaxItems(FieldScope scope)
	{
		CollectionValidation a = scope.getAnnotation(CollectionValidation.class);
		return (a != null ? a.maxSize() : null);
	}

	public Integer getMinItems(FieldScope scope)
	{
		CollectionValidation a = scope.getAnnotation(CollectionValidation.class);
		return (a != null ? a.minSize() : null);
	}

	public BigDecimal getMaxValue(FieldScope scope)
	{
		return null;
	}

	public BigDecimal getMinValue(FieldScope scope)
	{
		return null;
	}

	public Integer getMaxLength(FieldScope scope)
	{
		StringValidation a = scope.getAnnotation(StringValidation.class);
		return (a != null && a.maxLength() > 0 ? a.maxLength() : null);
	}

	public Integer getMinLength(FieldScope scope)
	{
		StringValidation a = scope.getAnnotation(StringValidation.class);
		return (a != null && a.minLength() > 0 ? a.minLength() : null);
	}

	public String getPattern(FieldScope scope)
	{
		StringValidation a = scope.getAnnotation(StringValidation.class);
		return (a != null && !a.pattern().isBlank() ? a.pattern() : null);
	}

	public boolean isRequired(FieldScope scope)
	{
		return false;
	}
}
