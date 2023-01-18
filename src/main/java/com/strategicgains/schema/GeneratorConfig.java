package com.strategicgains.schema;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeneratorConfig
{
	private static final String COMMAND_STRING = "a~o~p~r~u~w~";
	private static final CommandLine COMMAND_LINE_PARSER = new CommandLine(COMMAND_STRING);

	private String annotationProvider;
	private String baseUrl;
	private String basePath;
	private String outputPath;
	private List<String> readOnlyProperties;
	private List<String> writeOnlyProperties;
	private File jarFile;
	private List<String> classes;

	public static GeneratorConfig parseArgs(String[] args)
	throws CommandLineException
	{
		GeneratorConfig config = new GeneratorConfig();
		CommandLine commandLine = COMMAND_LINE_PARSER.parse(args);
		config.withAnnotationProvider(commandLine.getOptionArgument('a'));
		config.withOutputPath(commandLine.getOptionArgument('o'));
		config.withBaseUrl(commandLine.getOptionArgument('u'));
		config.withBasePath(commandLine.getOptionArgument('p'));
		config.withReadOnlyProperties(commandLine.getOptionArgument('r'));
		config.withWriteOnlyProperties(commandLine.getOptionArgument('w'));
		String[] arguments = commandLine.getArguments();

		if (arguments.length < 2)
		{
			throw new CommandLineException("jar-filename and at least one fully-qualified-classname are required.");
		}

		config.withJarFile(arguments[0]);
		config.withClasses(Arrays.copyOfRange(arguments, 1, arguments.length));
		return config;
	}

	public String getAnnotationProvider()
	{
		return annotationProvider;
	}

	public GeneratorConfig withAnnotationProvider(String providerName)
	{
		return this;
	}

	public String getBasePath()
	{
		return basePath;
	}

	public GeneratorConfig withBasePath(String basePath)
	{
		this.basePath = basePath;
		return this;
	}

	public String getBaseUrl()
	{
		return baseUrl;
	}

	public GeneratorConfig withBaseUrl(String baseUrl)
	{
		this.baseUrl = baseUrl;
		return this;
	}

	public String getOutputPath()
	{
		return outputPath;
	}

	public GeneratorConfig withOutputPath(String outputPath)
	{
		this.outputPath = outputPath;
		return this;
	}

	public List<String> getClasses()
	{
		return classes;
	}

	public GeneratorConfig withClasses(String... classNames)
	{
		if (this.classes == null)
		{
			this.classes = new ArrayList<>();
		}

		this.classes.addAll(Arrays.asList(classNames));
		return this;
	}

	public File getJarFile()
	{
		return jarFile;
	}

	public GeneratorConfig withJarFile(String jarFileName)
	{
		this.jarFile = new File(jarFileName);
		return this;
	}

	public List<String> getReadOnlyProperties()
	{
		return readOnlyProperties;
	}

	public GeneratorConfig withReadOnlyProperties(String commaSeparated)
	{
		if (commaSeparated != null)
		{
			return withReadOnlyProperties(commaSeparated.split(",\\s*"));
		}

		return this;
	}

	public GeneratorConfig withReadOnlyProperties(String... propertyNames)
	{
		if (propertyNames == null)
		{
			readOnlyProperties = null;
			return this;
		}

		if (readOnlyProperties == null)
		{
			this.readOnlyProperties = new ArrayList<>(propertyNames.length);
		}

		this.readOnlyProperties.addAll(Arrays.asList(propertyNames));
		return this;
	}

	public List<String> getWriteOnlyProperties()
	{
		return writeOnlyProperties;
	}

	public GeneratorConfig withWriteOnlyProperties(String commaSeparated)
	{
		if (commaSeparated != null)
		{
			return withWriteOnlyProperties(commaSeparated.split(",\\s*"));
		}

		return this;
	}

	public GeneratorConfig withWriteOnlyProperties(String... propertyNames)
	{
		if (propertyNames == null)
		{
			writeOnlyProperties = null;
			return this;
		}

		if (writeOnlyProperties == null)
		{
			this.writeOnlyProperties = new ArrayList<>(propertyNames.length);
		}

		this.writeOnlyProperties.addAll(Arrays.asList(propertyNames));
		return this;
	}
}
