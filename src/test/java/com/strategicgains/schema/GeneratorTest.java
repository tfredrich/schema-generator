package com.strategicgains.schema;

import static org.junit.Assert.*;

import org.junit.Test;

public class GeneratorTest
{
	@Test
	public void testSingleClass()
	throws ClassNotFoundException
	{
		String[] args = {"com.strategicgains.schema.Sample"};

		Generator.main(args);
	}
}
