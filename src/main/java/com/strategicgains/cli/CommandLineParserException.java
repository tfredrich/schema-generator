package com.strategicgains.cli;

/**
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