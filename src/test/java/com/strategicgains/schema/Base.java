package com.strategicgains.schema;

import java.util.Date;

import com.strategicgains.syntaxe.annotation.Required;

public abstract class Base<T>
{
	@Required
	T id;
	private Date updatedAt;
	private Date createdAt;
}
