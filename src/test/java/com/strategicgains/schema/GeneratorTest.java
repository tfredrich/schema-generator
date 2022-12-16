package com.strategicgains.schema;

import java.io.IOException;

import org.junit.Test;

public class GeneratorTest
{
	@Test
	public void testViaMain()
	throws ClassNotFoundException, CommandLineException, IOException
	{
//		String[] args = {"-u", "http://www.example.com/", "-r", "foo,bar,bat", "-w", "sys,boom,bah", "-o", "./target/schema-out", "./target/test-classes", "com.strategicgains.schema.Sample", "com.strategicgains.schema.AnEnumeration"};
		String[] args = {"-u", "http://www.example.com/", "-r", "foo,bar,bat", "-w", "sys,boom,bah", "./target/test-classes", "com.strategicgains.schema.Sample", "com.strategicgains.schema.AnEnumeration"};

		Generator.main(args);
	}

	@Test
	public void testViaConfig()
	throws ClassNotFoundException, CommandLineException, IOException
	{
//		String[] args = {"-u", "http://www.example.com/", "-r", "foo,bar,bat", "-w", "sys,boom,bah", "-o", "./target/schema-out", "./target/test-classes", "com.strategicgains.schema.Sample", "com.strategicgains.schema.AnEnumeration"};
		String[] args = {"-u", "http://www.example.com/", "-r", "foo,bar,bat,createdAt,updatedAt", "-w", "sys,boom,bah", "./target/test-classes", "com.strategicgains.schema.Sample", "com.strategicgains.schema.AnEnumeration"};

		GeneratorConfig config = GeneratorConfig.parseArgs(args);
		Generator generator = new Generator(config);
		generator.generate();
	}
}
