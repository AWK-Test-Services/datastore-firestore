package com.awk.datastore;

import com.awk.datastore.services.ResourceService;

import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/resources")
public class ResourceController {

    @Inject
    private ResourceService resourceService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResourceIds() {
        try {
            Collection<String> object = resourceService.getResourceIds();
            return Response.ok(object).build();
        } catch (NotFoundException nfe) {
            return Response.status(Response.Status.NOT_FOUND).entity(nfe.getMessage()).build();
        }
    }


    @GET
    @Path("/{resourceMapId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResources(@PathParam("resourceMapId") String resourceMapId) {
        try {
            Collection object = resourceService.getResources(resourceMapId);
            return Response.ok(object).build();
        } catch (NotFoundException nfe) {
            return Response.status(Response.Status.NOT_FOUND).entity(nfe.getMessage()).build();
        }
    }

    @GET
    @Path("/{resourceMapId}/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResource(@PathParam("resourceMapId") String resourceMapId, @PathParam("id") String id) {
        try {
            JsonObject object = resourceService.getResource(resourceMapId, id);
            return Response.ok(object).build();
        } catch (NotFoundException nfe) {
            return Response.status(Response.Status.NOT_FOUND).entity(nfe.getMessage()).build();
        }
    }

    @POST
    @Path("/{resourceMapId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response storeResource(@PathParam("resourceMapId") String resourceMapId, JsonObject resource) {
        Logger.getLogger(this.getClass().getName()).log(Level.INFO,"storeResource: " + resourceMapId);

        resourceService.addResource(resourceMapId, resource);

        return Response.ok().build();
    }
}