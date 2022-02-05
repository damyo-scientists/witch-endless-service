package witch.endless.service.model;

import static io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Data
@MappedSuperclass
@NoArgsConstructor
@SuperBuilder
public abstract class BaseEntity implements Serializable {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Schema(accessMode = READ_ONLY)
  protected long id;

  @Column(nullable = false, columnDefinition = "TIMESTAMP")
  @CreatedDate
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @Schema(accessMode = READ_ONLY)
  protected LocalDateTime createdAt;

  @Column(nullable = false, columnDefinition = "TIMESTAMP")
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  @LastModifiedDate
  @Schema(accessMode = READ_ONLY)
  protected LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    updatedAt = createdAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }
}
