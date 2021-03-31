package com.strategicgains.schema;

import java.util.Date;

public abstract class Base<T>
{
	T id;
	private Date updatedAt;
	private Date createdAt;
}
