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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Instance singleton - DAO & helper methods
 * (instance so it can be more easily mocked in testing)
 * @author Bancroft Gracey
 *
 */
public class RestStringsService {
	private static final Logger LOG = LoggerFactory.getLogger(RestStringsService.class);
	private static final Set<String> EMPTY_SET = new HashSet<String>();
	
	private static RestStringsService instance;
	
	public static RestStringsService getInstance() {
		if (instance == null) instance = new RestStringsService();
		return instance;
	}
	
	private final Map<Integer, Set<String>> stringsById = new HashMap<Integer, Set<String>>();
	private final Map<String, Integer> idsByString = new HashMap<String, Integer>();
	
	private void addString2Maps(RestString string, boolean save) {
		if (stringsById.containsKey(string.getStringId())) {
			LOG.debug("Appending {} to strings index", string);
			stringsById.get(string.getStringId()).add(string.getString());
		} else {
			LOG.debug("Adding {} to strings index", string);
			final Set<String> set = new HashSet<String>();
			set.add(string.getString());
			stringsById.put(string.getStringId(), set);
		}
		if (!idsByString.containsKey(string.getString())) {
			LOG.debug("Adding {} to ids index", string);
			idsByString.put(string.getString(), string.getStringId());
			if (save)
				RestStringsDAO.getInstance().save(string.getString());
		} else {
			LOG.debug("{} found in ids index", string);
		}
	}
	
	/**
	 * (for testing or prior to reloading) Clear instance
	 */
	public static void clear() {
		LOG.info("Clearing instance");
		instance = null;
	}
	
	/**
	 * (for testing) Reload from base file
	 */
	public static void reload() {
		LOG.info("Reloading");
		clear();
		final RestStringsDAO dao = RestStringsDAO.getInstance();
		dao.read();
		dao.write();
	}
	
	/**
	 * Save (if necessary) incoming string
	 * @param string InputString to process
	 * @return RestString
	 */
	public RestString addString(InputString string) {
		final RestString restString = createRestString(string);
		addString2Maps(restString, true);
		return restString;
	}
	/**
	 * Save string.
	 * Used by file reader
	 * @param string
	 * @return
	 */
	public RestString addString(String string) {
		final RestString restString = createRestString(string);
		addString2Maps(restString, false);
		return restString;
	}
	
	/**
	 * Utility to create new string records - if string is null, use empty string
	 * @param val base string
	 * @return RestString
	 */
	public RestString createRestString(String val) {
		// if it's already calculated, don't recalculate
		if (val == null) val = "";
		final Integer stringId = idsByString.containsKey(val) ? idsByString.get(val) : CommonFunctions.makeStringId(val);
		return new RestString(stringId, val);
	}
	/**
	 * Utility to create new string records from an InputString record
	 * @param val base InputString
	 * @return RestString object - if 
	 */
	public RestString createRestString(InputString val) {
		return createRestString(val.getString());
	}
	
	/**
	 * Return a list of strings for a given id, empty if no list is found
	 * @param id
	 * @return object containing id and possibly empty list
	 */
	public RestStringsList listStrings(int id) {
		final Set<String> strings = stringsById.getOrDefault(id, EMPTY_SET);
		final List<String> list = new ArrayList<String>(strings);
		return new RestStringsList(id, list);
	}
	
	/**
	 * Return a list of all strings stored
	 * @return
	 */
	public List<String> listAllStrings() {
		final Set<String> strings = idsByString.keySet();
		return new ArrayList<String>(strings);
	}
}
