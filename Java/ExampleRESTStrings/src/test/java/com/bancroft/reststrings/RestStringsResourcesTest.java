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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class RestStringsResourcesTest extends JerseyTest {
	
	private Entity<Map<String, String>> makeTestData(String string) {
		final Map<String, String> testData = new HashMap<String, String>();
		testData.put("string", string);
		return Entity.json(testData);
	}
	
	private Response makePostRequest(Entity<?> testData) {
		return target("string")
				.request(MediaType.APPLICATION_JSON_TYPE)
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.post(testData);
	}

	@Override
	protected Application configure() {
		return new ResourceConfig(RestStringsResources.class);
	}
	
	@Test
	public void helloTest() {
		String response = target("hello").request().get(String.class);
		assertEquals("Hello failed", "Hello World! (yes, it's working)", response);
	}
	
	@Test
	public void stringTest() {
		final Entity<Map<String, String>> entity = makeTestData("abbc");
		final Response response = makePostRequest(entity);
		assertEquals("Status", 200, response.getStatus());
		assertEquals("Content type", MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
		assertTrue("Has body", response.hasEntity());
		final RestString reply = response.readEntity(RestString.class);
		assertEquals("String", "abbc", reply.getString());
		assertEquals("Id", 97 + (97 + 98) + 98 + (98 + 99), reply.getStringId());
	}
	
	@Test
	public void listTest() {
		final Entity<Map<String, String>> testData1 = makeTestData("abca"); // a + a+b + b+c + a+c
		final Entity<Map<String, String>> testData2 = makeTestData("acba"); // a + a+c + b+c + a+b
		final Response r1 = makePostRequest(testData1);
		assertEquals("test data 1 setup", 200, r1.getStatus());
		final Response r2 = makePostRequest(testData2);
		assertEquals("test data 2 setup", 200, r2.getStatus());
		final RestString reply1 = r1.readEntity(RestString.class);
		final RestString reply2 = r2.readEntity(RestString.class);
		assertTrue("Same id", reply1.getStringId() == reply2.getStringId());
		
		final Response response = target("string/"+Integer.toString(reply1.getStringId()))
				.request()
				.accept(MediaType.APPLICATION_JSON_TYPE)
				.get();
		assertEquals("Status", 200, response.getStatus());
		assertEquals("Content type", MediaType.APPLICATION_JSON_TYPE, response.getMediaType());
		assertTrue("Has body", response.hasEntity());
		final RestStringsList reply = response.readEntity(RestStringsList.class);
		assertEquals("Id", reply1.getStringId(), reply.getStringId());
		final List<String> strings = reply.getStrings();
		assertEquals("String count", 2, strings.size());
		assertTrue("abca", strings.contains("abca"));
		assertTrue("acba", strings.contains("acba"));
	}
}
