package com.strategicgains.schema;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import org.junit.Test;

public class GeneratorTest
{
	@Test
	public void testSingleClass()
	throws ClassNotFoundException, MalformedURLException, FileNotFoundException
	{
		String[] args = {"./target/test-classes", "com.strategicgains.schema.Sample"};

		Generator.main(args);
	}
}
