package me.ldclrcq.filature.synchronizations;

import io.quarkus.oidc.UserInfo;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/synchronizations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Synchronizations", description = "Manage synchronizations")
public class SynchronizationResource {
    @Inject
    UserInfo userInfo;

    @GET
    public List<SynchronizationSummary> listSynchronizations() {
        return Synchronization.listForUser(userInfo.getSubject())
                .stream()
                .map(SynchronizationSummary::fromSynchronization)
                .toList();
    }
}
