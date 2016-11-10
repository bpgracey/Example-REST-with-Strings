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

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class InputString {
	private String string;

	public InputString(String string) {
		super();
		this.string = string;
	}
	public InputString() {}
	
	public String getString() {
		return string;
	}
	public void setString(String string) {
		this.string = string;
	}
	
	@Override
	public String toString() {
		return "InputString [string=" + string + "]";
	}

}
