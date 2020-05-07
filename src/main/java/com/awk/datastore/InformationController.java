package com.awk.datastore;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/info")
public class InformationController {

    @ConfigProperty(name = "com.awk.datastore.version", defaultValue="Unknown")
    private String version;

    @ConfigProperty(name = "com.awk.datastore.environment", defaultValue="Unknown")
    private String environment;

    @GET
    @Path("/version")
	@Produces(MediaType.APPLICATION_JSON)
	public JsonObject version() {
		JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
		jsonBuilder.add("version", version);
		return jsonBuilder.build();
	}

    @GET
    @Path("/environment")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject environment() {
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
        jsonBuilder.add("environment", environment);
        return jsonBuilder.build();
    }
}
