package me.ldclrcq.filature.notifiers;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Entity(name = "notifiers")
public class Notifier extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String userId;

    @Enumerated(EnumType.STRING)
    public NotifierType type;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    public Map<String, String> credentials;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    public Map<String, String> configuration;

    public Notifier() {
    }

    public Notifier(Long id, String userId, NotifierType type, Map<String, String> credentials, Map<String, String> configuration) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.credentials = credentials;
        this.configuration = configuration;
    }

    public static List<Notifier> listForUser(String userId) {
        return find("userId", userId).list();
    }

    public static Optional<Notifier> findByIdAndUser(Long id, String userId) {
        return find("userId = ?1 and id = ?2", userId, id).firstResultOptional();
    }

    public static Optional<Notifier> findByIdUserAndSource(Long id, String userId, NotifierType notifierType) {
        return find("userId = ?1 and id = ?2 and type = ?3", userId, id, notifierType).firstResultOptional();
    }
}
