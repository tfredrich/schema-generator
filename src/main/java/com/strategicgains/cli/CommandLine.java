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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * CommandLine represents the results of parsing command line arguments via the
 * CommandLineParser.
 * 
 * @author Todd Fredrich
 * @since 12 Nov 2025
 * @see CommandLineParser
 */
public class CommandLine {
	private List<String> arguments;
	private HashMap<Character, String> optionArguments;
	private Set<Character> setSwitches;

	public boolean isOptionSet(char optionToken) {
		Character option = Character.valueOf(optionToken);
		return (setSwitches.contains(option) || optionArguments.containsKey(option));
	}

	public String getOptionArgument(char optionToken) {
		return optionArguments.get(Character.valueOf(optionToken));
	}

	public boolean hasOptionArgumentFor(char optionToken) {
		return (getOptionArgument(optionToken) != null);
	}

	public String[] getArguments() {
		String[] strings = new String[arguments.size()];
		return arguments.toArray(strings);
	}

	// builder class
	public static class Builder {
		private List<String> arguments = new ArrayList<>();
		private HashMap<Character, String> optionArguments = new HashMap<>();
		private Set<Character> setSwitches = new HashSet<>();

		public Builder withArgument(String argument) {
			this.arguments.add(argument);
			return this;
		}

		public Builder withOption(Character option, String argument) {
			this.optionArguments.put(option, argument);
			return this;
		}

		public Builder withSwitch(Character switchChar) {
			this.setSwitches.add(switchChar);
			return this;
		}

		public CommandLine build() {
			CommandLine commandLine = new CommandLine();
			commandLine.arguments = this.arguments;
			commandLine.optionArguments = this.optionArguments;
			commandLine.setSwitches = this.setSwitches;
			return commandLine;
		}
	}
}