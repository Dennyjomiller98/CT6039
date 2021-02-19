package com.uog.miller.s1707031_ct6039;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/hello-world")
public class HelloResource
{
	@GET
	@Produces("text/plain")
	public String hello()
	{
		return "Hello, World!";
	}
}