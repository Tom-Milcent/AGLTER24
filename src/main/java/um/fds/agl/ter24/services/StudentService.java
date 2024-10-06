package um.fds.agl.ter24.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import um.fds.agl.ter24.entities.Student;
import um.fds.agl.ter24.repositories.StudentRepository;

import java.util.Optional;


@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Optional<Student> getStudent(final Long id) {
        return studentRepository.findById(id);
    }

    public Iterable<Student> getStudents() {
        return studentRepository.findAll();
    }

    public void deleteStudent(final Long id) {
        studentRepository.deleteById(id);
    }

    public Student saveStudent(Student student) {
        Student savedStudent = studentRepository.save(student);
        return savedStudent;
    }

    public Optional<Student> findById(long id) {
        return studentRepository.findById(id);
    }
}