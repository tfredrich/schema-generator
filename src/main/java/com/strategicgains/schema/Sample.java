package com.strategicgains.schema;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.strategicgains.syntaxe.annotation.ChildValidation;
import com.strategicgains.syntaxe.annotation.CollectionValidation;
import com.strategicgains.syntaxe.annotation.DoubleValidation;
import com.strategicgains.syntaxe.annotation.IntegerValidation;
import com.strategicgains.syntaxe.annotation.Required;
import com.strategicgains.syntaxe.annotation.StringValidation;

public class Sample
extends Base<UUID>
{
	@StringValidation(maxLength = 1024)
	private String description;

	@StringValidation(required = true, maxLength = 256, pattern = "[w+][1-9]")
	private String slug;

	@IntegerValidation(min = 0)
	private int count;

	@DoubleValidation(min = 0.0)
	private Double cost;
	private boolean isActive;

	@Required
	private Date occurredAt;

	@ChildValidation
	private Reference reference;

	@CollectionValidation(isNullable = false, maxSize = 25)
	private List<Element> children;

	@CollectionValidation
	private List<Integer> indexes;
}
