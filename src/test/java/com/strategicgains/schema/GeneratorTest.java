package com.strategicgains.schema;

import java.io.IOException;

import org.junit.Test;

public class GeneratorTest
{
	@Test
	public void testSingleClass()
	throws ClassNotFoundException, CommandLineException, IOException
	{
		String[] args = {"-o", "./target/schema-out", "./target/test-classes", "com.strategicgains.schema.Sample", "com.strategicgains.schema.AnEnumeration"};

		Generator.main(args);
	}
}
