package me.ldclrcq.filature.connections;

import io.quarkus.oidc.UserInfo;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import me.ldclrcq.filature.synchronizations.Synchronizer;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponseSchema;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/connections")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Connections", description = "Manage connections")
public class ConnectionResource {
    @Inject
    UserInfo userInfo;
    @Inject
    Synchronizer synchronizer;

    @POST
    @Transactional
    public Response createConnection(@Valid ConnectionCreationRequest request, @Context UriInfo uriInfo) {
        return request.toConnection(this.userInfo.getSubject())
                .map(connection -> {
                    connection.persist();
                    return Response
                            .status(Response.Status.CREATED)
                            .contentLocation(uriInfo.getAbsolutePathBuilder().path(Long.toString(connection.id)).build())
                            .build();
                })
                .orElse(Response.status(Response.Status.BAD_REQUEST).build());
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteConnection(@PathParam("id") Long id) {
        return Connection
                .findById(this.userInfo.getSubject(), id.intValue())
                .map(connection -> {
                    connection.delete();
                    return Response.noContent().build();
                })
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Path("/{connectionId}/synchronize")
    @Transactional
    public void synchronizeNow(int connectionId) {
        synchronizer.synchronizeNow(this.userInfo.getSubject(), connectionId);
    }

    @GET
    public List<ConnectionSummary> listConnections() {
        return Connection.findForUser(this.userInfo.getSubject())
                .stream()
                .map(ConnectionSummary::fromConnection)
                .toList();
    }

    @GET
    @Path("/{id}")
    @APIResponseSchema(ConnectionSummary.class)
    public Response getConnection(@PathParam("id") Long id) {
        return Connection
                .findById(this.userInfo.getSubject(), id.intValue())
                .map(ConnectionSummary::fromConnection)
                .map(summary -> Response.ok(summary).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
}
