package um.fds.agl.ter24.repositories;

import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import um.fds.agl.ter24.entities.TERManager;

@RepositoryRestResource(exported = false)
public interface TERManagerRepository extends  UserBaseRepository<TERManager> {

    TERManager save(TERManager manager);

    public TERManager findByLastName(String lastName);

    Iterable<TERManager> findAll();


}