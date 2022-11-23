package com.strategicgains.schema;

/**
 * @author Todd Fredrich
 * @since  Oct 23, 2003
 */
public class CommandLineException
extends Exception
{
	private static final long serialVersionUID = -8121433785251546578L;

	/**
	 * Constructs a new CommandLineException object.
	 */
	public CommandLineException()
	{
		super();
	}

	/**
	 * Constructs a new CommandLineException object.
	 * 
	 * @param arg0
	 */
	public CommandLineException(String arg0)
	{
		super(arg0);
	}

	/**
	 * Constructs a new CommandLineException object.
	 * 
	 * @param arg0
	 * @param arg1
	 */
	public CommandLineException(String arg0, Throwable arg1)
	{
		super(arg0, arg1);
	}

	/**
	 * Constructs a new CommandLineException object.
	 * 
	 * @param arg0
	 */
	public CommandLineException(Throwable arg0)
	{
		super(arg0);
	}
}