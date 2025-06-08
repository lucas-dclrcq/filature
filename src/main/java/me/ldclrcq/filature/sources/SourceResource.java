package me.ldclrcq.filature.sources;

import io.quarkus.oidc.UserInfo;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("/api/sources")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Sources", description = "Manage synchronization sources")
public class SourceResource {

    @Inject
    UserInfo userInfo;

    @GET
    public List<SourceSummary> listSources() {
        return Source.listForUser(this.userInfo.getSubject())
                .stream()
                .map(SourceSummary::fromSource)
                .toList();
    }
}
