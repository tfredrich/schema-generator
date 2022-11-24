package com.strategicgains.schema;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.victools.jsonschema.generator.FieldScope;
import com.github.victools.jsonschema.generator.TypeScope;
import com.strategicgains.syntaxe.annotation.CollectionValidation;
import com.strategicgains.syntaxe.annotation.DateValidation;
import com.strategicgains.syntaxe.annotation.DoubleValidation;
import com.strategicgains.syntaxe.annotation.FloatValidation;
import com.strategicgains.syntaxe.annotation.IntegerValidation;
import com.strategicgains.syntaxe.annotation.LongValidation;
import com.strategicgains.syntaxe.annotation.Required;
import com.strategicgains.syntaxe.annotation.StringValidation;

public class SyntaxeAnnotationProvider
implements AnnotationProvider
{
	private static URL DEFAULT_BASE_URL;
	static
	{
		try
		{
			DEFAULT_BASE_URL = new URL("https://schema.autheus.com/");
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
	};
	private static final String[] DEFAULT_READONLY_PROPERTIES = {
			"id",
			"createdAt",
			"updatedAt"
	};
	private URL baseUrl;
	private Set<String> readOnlyProperties = new HashSet<>(Arrays.asList(DEFAULT_READONLY_PROPERTIES));
	private Set<String> writeOnlyProperties = new HashSet<>();

	public SyntaxeAnnotationProvider()
	{
		super();
		withBaseUrl(DEFAULT_BASE_URL);
	}

	public SyntaxeAnnotationProvider(String baseUrl)
	throws MalformedURLException
	{
		this();
		withBaseUrl(baseUrl);
	}

	public SyntaxeAnnotationProvider withBaseUrl(String baseUrl)
	throws MalformedURLException
	{
		return withBaseUrl(new URL(baseUrl));
	}

	public SyntaxeAnnotationProvider withBaseUrl(URL baseUrl)
	{
		this.baseUrl = baseUrl;
		return this;
	}

	public SyntaxeAnnotationProvider withReadOnlyProperties(String[] readOnlyProperties)
	{
		this.readOnlyProperties.addAll(Arrays.asList(readOnlyProperties));
		return this;
	}

	public SyntaxeAnnotationProvider withWriteOnlyProperties(String[] writeOnlyProperties)
	{
		this.writeOnlyProperties.addAll(Arrays.asList(writeOnlyProperties));
		return this;
	}

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
		Required r = scope.getAnnotation(Required.class);
		if (r != null) return true;

		StringValidation s = scope.getAnnotation(StringValidation.class);
		if (s != null) return s.required();

		DateValidation dt = scope.getAnnotation(DateValidation.class);
		if (dt != null) return dt.required();

		CollectionValidation c = scope.getAnnotation(CollectionValidation.class);
		if (c != null) return !c.isNullable();

		IntegerValidation i = scope.getAnnotation(IntegerValidation.class);
		if (i != null) return !i.isNullable();

		FloatValidation f = scope.getAnnotation(FloatValidation.class);
		if (f != null) return !f.isNullable();

		DoubleValidation d = scope.getAnnotation(DoubleValidation.class);
		if (d != null) return !d.isNullable();

		LongValidation l = scope.getAnnotation(LongValidation.class);
		if (l != null) return !l.isNullable();

		return false;
	}

	/**
	 * Mark a field read-only by:
	 * 1) Adding it to the readOnlyProperties set.
	 * 2) Annotate it with the Jackson JsonPropery(access = Access.READ_ONLY) annotation.
	 * 3) Annotate it with the Syntaxe ReadOnly annotation. 
	 */
	@Override
	public boolean isReadOnly(FieldScope scope)
	{
		if (readOnlyProperties.contains(scope.getDeclaredName())) return true;

//		ReadOnly readOnly = scope.getAnnotation(ReadOnly.class);
//		if (readOnly != null) return true;

		JsonProperty annotation = scope.getAnnotation(JsonProperty.class);
		if (annotation != null) return JsonProperty.Access.READ_ONLY.equals(annotation.access());

		return false;
	}

	/**
	 * Mark a field write-only by:
	 * 1) Adding it to the writeOnlyProperties set.
	 * 2) Annotate it with the Jackson JsonPropery(access = Access.WRITE_ONLY) annotation.
	 * 3) Annotate it with the Syntaxe WriteOnly annotation. 
	 */
	@Override
	public boolean isWriteOnly(FieldScope scope)
	{
		if (writeOnlyProperties.contains(scope.getDeclaredName())) return true;

//		WriteOnly writeOnly = scope.getAnnotation(WriteOnly.class);
//		if (writeOnly != null) return true;

		JsonProperty annotation = scope.getAnnotation(JsonProperty.class);
		if (annotation != null) return JsonProperty.Access.WRITE_ONLY.equals(annotation.access());

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
	@Override
	public String getId(TypeScope s)
	{
		if (!FieldScope.class.isAssignableFrom(s.getClass()))
			return baseUrl + s.getSimpleTypeDescription() + ".json";
		return null;
	}
}
