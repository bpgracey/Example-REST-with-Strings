// Copyright 2016 Bancroft Gracey
/*
 * This file is part of Example REST Strings.

    Example REST Strings is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Example REST Strings is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Example REST Strings.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.bancroft.reststrings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommonFunctions {
	public static final Logger log = LoggerFactory.getLogger(CommonFunctions.class);

	private CommonFunctions() {} // not to be instantiated
	
	/**
	 * Return the string id
	 * @param val String to be id'd. Nulls and empty strings return 0.
	 * @return
	 */
	public static int makeStringId(String val) {
		log.info("Calculating Id for {}", val);
		if (val == null || val.isEmpty()) {
			log.info("Id for {} is zero", val);
			return 0;
		}
		final int id = makePartialStringId(0, 0, val);
		log.info("Id for {} is {}", val, id);
		return id;
	}
	
	private static int makePartialStringId(final int lastChar, final int ptr, final String val) {
		if (ptr == val.length()) return 0;
		final int thisChar = val.codePointAt(ptr);
		// recurse. Using a pointer because otherwise, if I'm not looping, I could be piling up a lot of (immutable) strings...
		return makeCharId(lastChar, thisChar) + makePartialStringId(thisChar, ptr + 1, val);
	}
	
	private static int makeCharId(final int lastChar, final int thisChar) {
		return ((lastChar == thisChar) ? 0 : lastChar) + thisChar;
	}
}
