package um.fds.agl.ter24;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import um.fds.agl.ter24.entities.*;
import um.fds.agl.ter24.repositories.*;

@Component
public class DatabaseLoader implements CommandLineRunner {
    private final TeacherRepository teachers;
    private final TERManagerRepository managers;
    private final StudentRepository students;
    private final TERRepository ter;


    @Autowired
    public DatabaseLoader(TeacherRepository teachers, TERManagerRepository managers, StudentRepository students, TERRepository ter) {
        this.teachers = teachers;
        this.managers = managers;
        this.students = students;
        this.ter = ter;
    }

    @Override
    public void run(String... strings) throws Exception {
        TERManager terM1Manager = this.managers.save(new TERManager("Le", "Chef", "mdp", "ROLE_MANAGER"));
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("Chef", "bigre",
                        AuthorityUtils.createAuthorityList("ROLE_MANAGER"))); // the actual password is not needed here
        Teacher teacher1 = new Teacher("Ada", "Lovelace", "lovelace", terM1Manager, "ROLE_TEACHER");
        Teacher teacher2 = new Teacher("Alan", "Turing", "turing", terM1Manager, "ROLE_TEACHER");
        Teacher teacher3 = new Teacher("Leslie", "Lamport", "lamport", terM1Manager, "ROLE_TEACHER");
        this.teachers.save(teacher1);
        this.teachers.save(teacher2);
        this.teachers.save(teacher3);
        this.students.save(new Student("Gustave", "Flaubert"));
        this.students.save(new Student("Frédéric", "Chopin"));
        this.ter.save(new TER(teacher1, "Maths"));
        this.ter.save(new TER(teacher1, "Maths & co"));
        this.ter.save(new TER(teacher2, "Info"));

        SecurityContextHolder.clearContext();

    }
}
