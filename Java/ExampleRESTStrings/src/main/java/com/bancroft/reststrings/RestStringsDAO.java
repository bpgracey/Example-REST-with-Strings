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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manage permanent store
 * 
 * Implementing this as a singleton for now (for mocking and for flexibility using other stores)
 * @author Bancroft Gracey
 *
 */
public class RestStringsDAO {
	private final static Logger LOG = LoggerFactory.getLogger(RestStringsDAO.class);
	private final static String CHARSET_NAME = "UTF-8";
	private final static Charset CHARSET = Charset.forName(CHARSET_NAME);
	private final static Path PATH = Paths.get("strings.txt"); // TODO Parameterise!
	
	private static RestStringsDAO instance;
	
	private RestStringsDAO() {};
	
	public static RestStringsDAO getInstance() {
		if (instance == null) instance = new RestStringsDAO();
		return instance;
	}
	
	/**
	 * Save a string, encoded (so newlines are ok!), to the end of the file.
	 * @param s
	 */
	public void save(String s) {
		LOG.info("Saving {}", s);
		try (BufferedWriter writer = Files.newBufferedWriter(PATH, CHARSET, StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.WRITE)) {
			writeString(writer, s);
			writer.close();
			LOG.info("Saved {}", s);
		} catch (IOException e) {
			LOG.error("Save {} failed: {}", s, e.getLocalizedMessage());
		}
	}
	
	/**
	 * Clear the Service instance and reload from file
	 * 
	 * File is a list of (encoded) UTF-8 strings separated by newlines; recalculate string Ids on reload
	 */
	public void read() {
		LOG.info("Reading file");
		if (Files.exists(PATH)) {
			LOG.debug("File found");
			final RestStringsService service = RestStringsService.getInstance();
			try (BufferedReader reader = Files.newBufferedReader(PATH, CHARSET)) {
				String encoded;
				int strings = 0;
				while ((encoded = reader.readLine()) != null) {
					final String string = decode(encoded);
					service.addString(string);
					strings++;
				}
				reader.close();
				LOG.info("File read, {} strings", strings);
			} catch (IOException e) {
				LOG.error("Read failed: {}", e.getLocalizedMessage());
			}
		} else {
			LOG.info("No file");
		}
	}

	private String decode(String val) {
		String decoded = "";
		try {
			decoded = URLDecoder.decode(val, CHARSET_NAME);
		} catch (UnsupportedEncodingException e) {
			LOG.error("Decoding fail for '{}'\nThis should NEVER happen!", val);
			LOG.debug("UnsupportedEncodingException", e);
		}
		return decoded;
	}
	
	protected String encode(String val) {
		String encoded = "";
		try {
			encoded = URLEncoder.encode(val, CHARSET_NAME);
		} catch (UnsupportedEncodingException e) {
			LOG.error("Encoding error for '{}'\nThis should NEVER happen!\n{}", val);
			LOG.debug("UnsupportedCodingException", e);
		}
		return encoded;
	}
	
	private int writeString(BufferedWriter writer, String val) {
		final String encoded = encode(val);
		int i = 0;
		try {
			writer.write(encoded);
			writer.newLine();
			i = 1;
		} catch (IOException e) {
			LOG.warn("{} not written!", val);
			LOG.debug("IOException", e);
		}
		return i;
	}
	
	/**
	 * Rewrite the file. Useful for scrubbing duplicates!
	 */
	public void write() {
		LOG.info("Writing entire file");
		try (BufferedWriter writer = Files.newBufferedWriter(PATH, CHARSET, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
			final RestStringsService service = RestStringsService.getInstance();
			final Stream<String> allStrings = service.listAllStrings().stream();
			final long written = allStrings.map(str -> encode(str)).map(string -> writeString(writer, string)).filter(i -> i != 0).count();
			writer.close();
			LOG.info("File written, {} strings", written);
		} catch (IOException e) {
			LOG.error("File NOT written", e);
		}
	}
}
