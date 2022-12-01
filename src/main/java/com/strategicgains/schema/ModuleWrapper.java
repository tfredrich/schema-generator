package com.strategicgains.schema;

import java.net.MalformedURLException;

import com.github.victools.jsonschema.generator.Module;

public interface ModuleWrapper
extends Module
{
	void withReadOnlyProperties(String[] names);

	void withWriteOnlyProperties(String[] names);

	void withBaseUrl(String baseUrl)
	throws MalformedURLException;
}
