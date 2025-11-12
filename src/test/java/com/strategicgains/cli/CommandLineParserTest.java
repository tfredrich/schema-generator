/*
	Copyright 2003-2025 Strategic Gains, Inc.
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
		http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/
package com.strategicgains.cli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * @author Todd Fredrich
 * @since  Oct 23, 2003
 */
class CommandLineParserTest
{
	private static final String COMMAND_LINE_TOKENS = "zi~o~";

	@Test
	void shouldParse()
	{
		String[] command = {"-z", "-o", "XmlFile.xml", "amzn", "yhoo", "hpq", "-i", "InputFile.txt"};
		CommandLine cl = new CommandLineParser(COMMAND_LINE_TOKENS).parse(command);

		assertTrue(cl.isOptionSet('o'), "-o option not set");
		assertTrue(cl.isOptionSet('i'), "-i option not set");
		assertTrue(cl.isOptionSet('z'), "-z option not set");
		assertFalse(cl.isOptionSet('x'), "-x option set");
		assertNotNull(cl.getOptionArgument('o'), "-o value is null");
		assertEquals("XmlFile.xml", cl.getOptionArgument('o'));
		assertNotNull(cl.getOptionArgument('i'), "-i value is null");
		assertEquals("InputFile.txt", cl.getOptionArgument('i'));

		String[] arguments = cl.getArguments();
		assertEquals(3, arguments.length, "Argument size incorrect");
		assertEquals("amzn", arguments[0], "argument[0]");
		assertEquals("yhoo", arguments[1], "argument[1]");
		assertEquals("hpq", arguments[2], "argument[2]");
	}

	@Test
	void shouldThrowExceptionForMissingOptionArgument()
	{
		String[] command = {"-z", "-o", "-i", "InputFile.txt"};
		CommandLineParser parser = new CommandLineParser(COMMAND_LINE_TOKENS);
		CommandLineParserException exception = assertThrows(CommandLineParserException.class, () -> parser.parse(command));
		assertEquals("Parameter required for option: o", exception.getMessage());
	}

	@Test
	void shouldThrowExceptionForInvalidOption()
	{
		String[] command = {"-z", "-x", "SomeValue", "-i", "InputFile.txt"};
		CommandLineParser parser = new CommandLineParser(COMMAND_LINE_TOKENS);
		CommandLineParserException exception = assertThrows(CommandLineParserException.class, () -> parser.parse(command));
		assertEquals("Invalid command line option: x", exception.getMessage());
	}

	@Test
	void shouldParseWithNoOptions()
	{
		String[] command = {"amzn", "yhoo", "hpq"};
		CommandLine cl = new CommandLineParser(COMMAND_LINE_TOKENS).parse(command);
		String[] arguments = cl.getArguments();
		assertEquals(3, arguments.length, "Argument size incorrect");
		assertEquals("amzn", arguments[0], "argument[0]");
		assertEquals("yhoo", arguments[1], "argument[1]");
		assertEquals("hpq", arguments[2], "argument[2]");
	}

	@Test
	void shouldParseWithNoArguments()
	{
		String[] command = {};
		CommandLine cl = new CommandLineParser(COMMAND_LINE_TOKENS).parse(command);
		assertEquals(0, cl.getArguments().length, "Argument size incorrect");
		assertFalse(cl.isOptionSet('o'), "-o option set");
		assertFalse(cl.isOptionSet('i'), "-i option set");
		assertFalse(cl.isOptionSet('z'), "-z option set");
		assertFalse(cl.isOptionSet('x'), "-x option set");
	}

	@Test
	void shouldThrowExceptionForMissingArgAtEnd()
	{
		String[] command = {"-i"};
		CommandLineParser parser = new CommandLineParser(COMMAND_LINE_TOKENS);
		CommandLineParserException exception = assertThrows(CommandLineParserException.class, () -> parser.parse(command));
		assertEquals("Parameter required for option: i", exception.getMessage());
	}

	@Test
	void shouldParseQuotedArgument()
	{
		String[] command = {"-z", "-o", "SomeValue", "-i", "\"/home/src/InputFile.txt\", 42"};
		CommandLine cl = new CommandLineParser(COMMAND_LINE_TOKENS).parse(command);
		assertEquals("\"/home/src/InputFile.txt\", 42", cl.getOptionArgument('i'));
	}
}
