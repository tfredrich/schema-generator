/*
    Copyright 2003-2025, Strategic Gains, Inc.

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

/**
 * A CommandLineParserException indicates an error condition while parsing
 * command line arguments. It is a runtime exception.
 * 
 * @author Todd Fredrich
 * @since  Oct 23, 2003
 */
public class CommandLineParserException
extends RuntimeException
{
	private static final long serialVersionUID = -8121433785251546578L;

	/**
	 * Constructs a new CommandLineParserException object.
	 */
	public CommandLineParserException()
	{
		super();
	}

	/**
	 * Constructs a new CommandLineParserException object.
	 * 
	 * @param arg0
	 */
	public CommandLineParserException(String arg0)
	{
		super(arg0);
	}

	/**
	 * Constructs a new CommandLineParserException object.
	 * 
	 * @param arg0
	 * @param arg1
	 */
	public CommandLineParserException(String arg0, Throwable arg1)
	{
		super(arg0, arg1);
	}

	/**
	 * Constructs a new CommandLineParserException object.
	 * 
	 * @param arg0
	 */
	public CommandLineParserException(Throwable arg0)
	{
		super(arg0);
	}
}