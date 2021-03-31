package com.strategicgains.schema;

import java.util.UUID;

import com.strategicgains.syntaxe.annotation.Required;

public class Reference
{
	@Required
	private UUID id;
	private String name;
}
