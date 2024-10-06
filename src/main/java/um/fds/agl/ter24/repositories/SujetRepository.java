package um.fds.agl.ter24.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import um.fds.agl.ter24.entities.Sujet;
@Repository
public interface SujetRepository extends CrudRepository<Sujet, Long> {
}
