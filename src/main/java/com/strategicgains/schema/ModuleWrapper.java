package com.strategicgains.schema;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import com.github.victools.jsonschema.generator.Module;

public interface ModuleWrapper
extends Module
{
	void withReadOnlyProperties(String[] names);

	void withWriteOnlyProperties(String[] names);
	
	void withBasePath(String basePath);

	void withBaseUrl(String baseUrl)
	throws MalformedURLException, URISyntaxException;
}
