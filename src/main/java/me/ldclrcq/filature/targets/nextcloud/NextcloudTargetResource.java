package me.ldclrcq.filature.targets.nextcloud;

import io.quarkus.oidc.UserInfo;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import me.ldclrcq.filature.targets.TargetType;
import me.ldclrcq.filature.targets.Target;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponseSchema;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/api/targets/nextcloud")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Targets", description = "Manage synchronization targets")
public class  NextcloudTargetResource {

    @Inject
    UserInfo userInfo;

    @POST
    @Transactional
    public Response createNextcloudTarget(@Valid NextcloudTargetCreationRequest request, @Context UriInfo uriInfo) {
        Target target = request.toTargetConfiguration(this.userInfo.getSubject());
        target.persist();
        return Response
                .status(Response.Status.CREATED)
                .contentLocation(uriInfo.getAbsolutePathBuilder().path(Long.toString(target.id)).build())
                .build();
    }

    @GET
    @Path("/{id}")
    @Transactional
    @APIResponseSchema(NextcloudTargetSummary.class)
    public Response getNextcloudTarget(@PathParam("id") Long id) {
        return Target
                .findByIdUserAndTarget(id, userInfo.getSubject(), TargetType.NEXTCLOUD)
                .map(NextcloudTargetSummary::fromTarget)
                .map(summary -> Response.ok(summary).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateNextcloudTarget(@PathParam("id") Long id, @Valid NextcloudTargetUpdateRequest request) {
        return Target
                .findByIdUserAndTarget(id, userInfo.getSubject(), TargetType.NEXTCLOUD)
                .map(target -> {
                    request.updateTarget(target).persist();
                    return Response.ok().build();
                })
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteNextcloudTarget(@PathParam("id") Long id) {
        return Target
                .findByIdUserAndTarget(id, userInfo.getSubject(), TargetType.NEXTCLOUD)
                .map(targetConfiguration -> {
                    targetConfiguration.delete();
                    return Response.noContent().build();
                })
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
}