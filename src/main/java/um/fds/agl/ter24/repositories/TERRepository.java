package um.fds.agl.ter24.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import um.fds.agl.ter24.entities.TER;

public interface TERRepository extends CrudRepository<TER, Long> {

    @Override
    @PreAuthorize("hasRole('ROLE_MANAGER') or (#ter?.teacher.lastName == authentication?.name)")
    TER save(@Param("ter") TER ter);

    @Override
    @PreAuthorize("hasRole('ROLE_MANAGER') or (@TERRepository.findById(#id).get()?.teacher.lastName == authentication?.name)")
    void deleteById(@Param("id") Long id);

    @Override
    @PreAuthorize("hasRole('ROLE_MANAGER') or (#ter?.teacher.lastName == authentication?.name)")
    void delete(@Param("ter") TER ter);
    
}
