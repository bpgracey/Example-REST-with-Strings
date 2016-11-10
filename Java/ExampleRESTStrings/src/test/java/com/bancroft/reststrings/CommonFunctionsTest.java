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

import static org.junit.Assert.*;

import org.junit.Test;

public class CommonFunctionsTest {
	
	/*
	 * Working with Scalatest and Specs2 in Scala has led me to separate out my tests...
	 */

	@Test
	public void testMakeStringIdABC() {
		assertEquals("abc", 97 + (97 + 98) + (98 + 99), CommonFunctions.makeStringId("abc"));
	}
	
	@Test
	public void testMakeStringIdABBC() {
		assertEquals("abbc", 97 + (97 + 98) + 98 + (98 + 99), CommonFunctions.makeStringId("abbc"));
	}
	
	@Test
	public void testMakeStringIdA() {
		assertEquals("a", 97, CommonFunctions.makeStringId("a"));
	}
	
	@Test
	public void testMakeStringIdEmpty() {
		assertEquals("Empty string", 0, CommonFunctions.makeStringId(""));
	}
	
	@Test
	public void testMakeStringIdNull() {
		assertEquals("Null", 0, CommonFunctions.makeStringId(null));
	}

}
