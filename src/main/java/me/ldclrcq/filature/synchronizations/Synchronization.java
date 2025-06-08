package me.ldclrcq.filature.synchronizations;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import me.ldclrcq.filature.connections.Connection;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "synchronizations")
public class Synchronization extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public LocalDateTime startedAt;
    public LocalDateTime endedAt;

    @Enumerated(EnumType.STRING)
    public SynchronizationStatus status;

    public String error;

    @ManyToOne
    @JoinColumn(name = "connection_id")
    public Connection connection;

    public Synchronization() {
    }

    public Synchronization(LocalDateTime startedAt, Connection connection) {
        this.startedAt = startedAt;
        this.connection = connection;
    }

    public static List<Synchronization> listForUser(String userId) {
        return find("connection.userId", userId).list();
    }
}
