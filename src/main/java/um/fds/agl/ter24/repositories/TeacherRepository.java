package um.fds.agl.ter24.repositories;

import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import um.fds.agl.ter24.entities.Teacher;


public interface TeacherRepository extends UserBaseRepository<Teacher> {
    @Override
    @PreAuthorize("hasRole('ROLE_MANAGER') and (#teacher?.terManager == null or #teacher?.terManager?.lastName == authentication?.name)")
    Teacher save(@Param("teacher") Teacher teacher);

    @Override
    @PreAuthorize("@teacherRepository.findById(#id).get()?.terManager?.lastName == authentication?.name")
    void deleteById(@Param("id") Long id);

    @Override
    @PreAuthorize("#teacher?.terManager?.lastName == authentication?.name")
    void delete(@Param("teacher") Teacher teacher);
}
