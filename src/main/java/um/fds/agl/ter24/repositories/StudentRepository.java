package um.fds.agl.ter24.repositories;

import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import um.fds.agl.ter24.entities.Student;

public interface StudentRepository extends UserBaseRepository<Student> {

    @Override
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    Student save(@Param("student") Student student);

    @Override
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    void deleteById(@Param("id") Long id);

    @Override
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    void delete(@Param("student") Student student);
}