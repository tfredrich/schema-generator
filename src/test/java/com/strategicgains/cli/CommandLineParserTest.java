/*
	Copyright 2003-2006 Strategic Gains, Inc.
	
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
	throws Exception
	{
		String[] command = {"-z", "-o", "XmlFile.xml", "amzn", "yhoo", "hpq", "-i", "InputFile.txt"};
		CommandLineParser parser = new CommandLineParser(COMMAND_LINE_TOKENS).parse(command);

		assertTrue(parser.isOptionSet('o'), "-o option not set");
		assertTrue(parser.isOptionSet('i'), "-i option not set");
		assertTrue(parser.isOptionSet('z'), "-z option not set");
		assertFalse(parser.isOptionSet('x'), "-x option set");
		assertNotNull(parser.getOptionArgument('o'), "-o value is null");
		assertEquals("XmlFile.xml", parser.getOptionArgument('o'));
		assertNotNull(parser.getOptionArgument('i'), "-i value is null");
		assertEquals("InputFile.txt", parser.getOptionArgument('i'));

		String[] arguments = parser.getArguments();
		assertEquals(3, arguments.length, "Argument size incorrect");
		assertEquals("amzn", arguments[0], "argument[0]");
		assertEquals("yhoo", arguments[1], "argument[1]");
		assertEquals("hpq", arguments[2], "argument[2]");
	}
}
