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

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * List of strings with same id
 * @author Bancroft Gracey
 *
 */
@XmlRootElement
public class RestStringsList {
	private int stringId;
	private List<String> strings;
	
	public RestStringsList() {}

	public RestStringsList(int stringId, List<String> strings) {
		super();
		this.stringId = stringId;
		this.strings = strings;
	}

	public int getStringId() {
		return stringId;
	}

	public List<String> getStrings() {
		return strings;
	}

	public void setStringId(int stringId) {
		this.stringId = stringId;
	}

	public void setStrings(List<String> strings) {
		this.strings = strings;
	}
	
	@Override
	public String toString() {
		return "RestStringsList [stringId=" + stringId + ", strings=" + strings + "]";
	}

}
