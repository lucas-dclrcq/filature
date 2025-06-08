package me.ldclrcq.filature.targets;

import io.quarkus.oidc.UserInfo;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponseSchema;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/targets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Targets", description = "Manage synchronization targets")
public class TargetResource {

    @Inject
    UserInfo userInfo;

    @GET
    public List<TargetSummary> listTargets() {
        return Target.listForUser(this.userInfo.getSubject())
                .stream()
                .map(TargetSummary::fromTarget)
                .toList();
    }

    @GET
    @Path("/{id}")
    @Transactional
    @APIResponseSchema(TargetSummary.class)
    public Response getTarget(@PathParam("id") Long id) {
        return Target
                .findByIdAndUser(id, userInfo.getSubject())
                .map(TargetSummary::fromTarget)
                .map(summary -> Response.ok(summary).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteTarget(@PathParam("id") Long id) {
        return Target
                .findByIdAndUser(id, userInfo.getSubject())
                .map(targetConfiguration -> {
                    targetConfiguration.delete();
                    return Response.noContent().build();
                })
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
}
