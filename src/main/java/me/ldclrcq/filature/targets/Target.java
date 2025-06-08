package me.ldclrcq.filature.targets;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Entity(name = "targets")
public class Target extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String userId;

    @Enumerated(EnumType.STRING)
    public TargetType type;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    public Map<String, String> credentials;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    public Map<String, String> configuration;

    public Target() {
    }

    public Target(String userId, TargetType type, Map<String, String> credentials, Map<String, String> configuration) {
        this.userId = userId;
        this.type = type;
        this.credentials = credentials;
        this.configuration = configuration;
    }

    public static List<Target> listForUser(String userId) {
        return find("userId", userId).list();
    }

    public static Optional<Target> findByIdAndUser(Long id, String userId) {
        return find("userId = ?1 and id = ?2", userId, id).firstResultOptional();
    }

    public static Optional<Target> findByIdUserAndTarget(Long id, String userId, TargetType targetType) {
        return find("userId = ?1 and id = ?2 and type = ?3", userId, id, targetType).firstResultOptional();
    }
}
