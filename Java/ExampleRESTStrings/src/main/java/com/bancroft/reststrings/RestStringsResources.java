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

import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Resources
 * @author Bancroft Gracey
 *
 */
@Singleton
@Path("/")
public class RestStringsResources {
	private static final Logger LOG = LoggerFactory.getLogger(RestStringsResources.class);
	
	public RestStringsResources() {
		LOG.info("Initialising app");
		RestStringsService.reload();
	}
	
	@Path("/hello")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getHello() {
		LOG.info("/hello called");
		return "Hello World! (yes, it's working)";
	}

	@Path("/string/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public RestStringsList getString(@PathParam("id") Integer id) {
		LOG.info("/string/{} called", id);
		final RestStringsService service = RestStringsService.getInstance();
		final RestStringsList listStrings = service.listStrings(id);
		return listStrings;
	}
	
	@Path("/string")
	@POST // not PUT, because, in this design, not idempotent, and not supplying key
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces(MediaType.APPLICATION_JSON)
	public RestString postString(InputString input) {
		LOG.info("/string called");
		final RestStringsService service = RestStringsService.getInstance();
		final RestString addString = service.addString(input);
		return addString;
	}
	
	@Path("/clear")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String clear() {
		LOG.info("/clear called");
		RestStringsService.clear();
		return "OK";
	}
	
	@Path("/list")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllStrings() {
		LOG.info("/list called");
		final RestStringsService service = RestStringsService.getInstance();
		final List<String> strings = service.listAllStrings();
		return String.join("\n", strings);
	}
	
	@Path("/load")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String reload() {
		LOG.info("/load called");
		RestStringsService.reload();
		final RestStringsService service = RestStringsService.getInstance();
		final List<String> strings = service.listAllStrings();
		return "Reloaded\n\n" + String.join("\n", strings);		
	}
}
