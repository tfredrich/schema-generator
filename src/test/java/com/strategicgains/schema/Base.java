package com.strategicgains.schema;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.strategicgains.syntaxe.annotation.Required;

public abstract class Base<T>
{
	@Required
	@JsonProperty(access = Access.READ_ONLY)
	T id;
	private Date updatedAt;
	private Date createdAt;
}
