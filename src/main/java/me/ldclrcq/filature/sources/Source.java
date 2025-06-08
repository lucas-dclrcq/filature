package me.ldclrcq.filature.sources;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Entity(name = "sources")
public class Source extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String userId;

    @Enumerated(EnumType.STRING)
    public SourceType type;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    public Map<String, String> credentials;

    @Type(JsonType.class)
    @Column(columnDefinition = "jsonb")
    public Map<String, String> configuration;

    public Source() {
    }

    public Source(String userId, SourceType type, Map<String, String> credentials, Map<String, String> configuration) {
        this.userId = userId;
        this.type = type;
        this.credentials = credentials;
        this.configuration = configuration;
    }

    public static List<Source> listForUser(String userId) {
        return find("userId", userId).list();
    }

    public static Optional<Source> findByIdAndUser(Long id, String userId) {
        return find("userId = ?1 and id = ?2", userId, id).firstResultOptional();
    }

    public static Optional<Source> findByIdUserAndSource(Long id, String userId, SourceType sourceType) {
        return find("userId = ?1 and id = ?2 and type = ?3", userId, id, sourceType).firstResultOptional();
    }
}
