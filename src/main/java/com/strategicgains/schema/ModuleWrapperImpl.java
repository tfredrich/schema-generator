package com.strategicgains.schema;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.github.victools.jsonschema.generator.FieldScope;
import com.github.victools.jsonschema.generator.Module;
import com.github.victools.jsonschema.generator.SchemaGeneratorConfigBuilder;

public class ModuleWrapperImpl
implements ModuleWrapper
{
	private Module module;
	private Set<String> readOnlyProperties = new HashSet<>();
	private Set<String> writeOnlyProperties = new HashSet<>();

	public ModuleWrapperImpl(Module wrappedModule)
	{
		super();
		this.module = wrappedModule;
	}

	@Override
	public void applyToConfigBuilder(SchemaGeneratorConfigBuilder builder)
	{
		module.applyToConfigBuilder(builder);
	}

	@Override
	public void withReadOnlyProperties(String[] readOnlyProperties)
	{
		this.readOnlyProperties.addAll(Arrays.asList(readOnlyProperties));
	}

	@Override
	public void withWriteOnlyProperties(String[] writeOnlyProperties)
	{
		this.writeOnlyProperties.addAll(Arrays.asList(writeOnlyProperties));
	}

	@Override
	public void withBasePath(String basePath)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void withBaseUrl(String baseUrl) throws MalformedURLException
	{
		// TODO Auto-generated method stub

	}

	public boolean isReadOnly(FieldScope scope)
	{
		if (readOnlyProperties.contains(scope.getDeclaredName())) return true;

//		return module.isReadOnly(scope);
		return false;
	}

	public boolean isWriteOnly(FieldScope scope)
	{
		if (writeOnlyProperties.contains(scope.getDeclaredName())) return true;

//		return module.isWriteOnly(scope);
		return false;
	}
}
