package me.ldclrcq.filature.connections;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import me.ldclrcq.filature.notifiers.Notifier;
import me.ldclrcq.filature.sources.Source;
import me.ldclrcq.filature.synchronizations.Synchronization;
import me.ldclrcq.filature.targets.Target;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity(name = "connections")
public class Connection extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String userId;

    @ManyToOne
    @JoinColumn(name = "source_id")
    public Source source;

    @ManyToOne
    @JoinColumn(name = "target_id")
    public Target target;

    @ManyToOne
    @JoinColumn(name = "notifier_id")
    public Notifier notifier;

    public String targetUploadPath;

    public LocalDateTime lastSynchronizedAt;

    public LocalDateTime lastDocumentDownloadedDate;

    @Enumerated(EnumType.STRING)
    public ConnectionStatus status = ConnectionStatus.ACTIVE;

    @OneToMany(mappedBy = "connection")
    public List<Synchronization> synchronizations = new ArrayList<>();

    public Connection() {
    }

    public Connection(String userId, Source source, Target target, String targetUploadPath) {
        this.userId = userId;
        this.source = source;
        this.target = target;
        this.targetUploadPath = targetUploadPath;
    }

    public static Optional<Connection> findSynchronizedMoreThan(int hoursAgo) {
        return find("where lastSynchronizedAt < ?1", LocalDateTime.now().minusHours(hoursAgo))
                .withLock(LockModeType.PESSIMISTIC_WRITE)
                .firstResultOptional();
    }

    public static Optional<Connection> findById(String userId, int connectionId) {
        return find("userId = ?1 and id = ?2", userId, connectionId)
                .firstResultOptional();
    }

    public static List<Connection> findForUser(String userId) {
        return find("userId", userId).list();
    }
}
