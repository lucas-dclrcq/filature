package me.ldclrcq.filature.sources.connectors.free;

import io.quarkus.logging.Log;
import io.quarkus.oidc.UserInfo;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import me.ldclrcq.filature.sources.Source;
import me.ldclrcq.filature.sources.SourceType;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponseSchema;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/sources/free")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Sources", description = "Manage synchronization sources")
public class FreeSourceResource {

    @Inject
    UserInfo userInfo;

    @POST
    @Transactional
    public Response createFreeSource(@Valid FreeSourceCreationRequest request, @Context UriInfo uriInfo) {
        Log.infof("Creating Free source for user %s", this.userInfo.getSubject());
        Source source = request.toSource(this.userInfo.getSubject());
        source.persist();
        return Response
                .status(Response.Status.CREATED)
                .contentLocation(uriInfo.getAbsolutePathBuilder().path(Long.toString(source.id)).build())
                .build();
    }

    @GET
    @Path("/{id}")
    @Transactional
    @APIResponseSchema(FreeSourceSummary.class)
    public Response getFreeSource(@PathParam("id") Long id) {
        return Source
                .findByIdUserAndSource(id, userInfo.getSubject(), SourceType.FREE)
                .map(FreeSourceSummary::fromSource)
                .map(summary -> Response.ok(summary).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateFreeSource(@PathParam("id") Long id, @Valid FreeSourceUpdateRequest request) {
        Log.infof("Updating Free source %d", id);
        return Source
                .findByIdUserAndSource(id, userInfo.getSubject(), SourceType.FREE)
                .map(source -> {
                    request.updateSource(source);
                    source.persist();
                    return Response.ok().build();
                })
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteSource(@PathParam("id") Long id) {
        Log.infof("Deleting Free source %d", id);
        return Source
                .findByIdUserAndSource(id, userInfo.getSubject(), SourceType.FREE)
                .map(source -> {
                    source.delete();
                    return Response.noContent().build();
                })
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
}