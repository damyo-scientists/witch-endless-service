package witch.endless.service.behavior;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import witch.endless.service.model.BaseEntity;

public interface BaseBehavior<Entity extends BaseEntity, Repo extends JpaRepository<Entity, Long>> {

  Repo getRepo();

  default List<Entity> getAll() {
    return getRepo().findAll();
  }

  default Optional<Entity> findById(Long id) {
    return getRepo().findById(id);
  }

  default Entity fetchById(Long id) {
    return getRepo().getOne(id);
  }

  default void deleteById(Long id) {
    getRepo().deleteById(id);
  }

  default Entity save(Entity entity) {
    return getRepo().save(entity);
  }
}
