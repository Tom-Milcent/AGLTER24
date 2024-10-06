package um.fds.agl.ter24.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class TER {

    private @Id
    @GeneratedValue Long id;

    @ManyToOne
    private Teacher teacher;
    private String subject;

    public TER(Teacher teacher, String subject){
        this.teacher = teacher;
        this.subject = subject;
    }

    public TER(Long id, Teacher teacher, String subject) {
        this(teacher, subject);
        this.id = id;
    }

    public TER() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TER ter = (TER) o;
        return teacher.equals(ter.teacher) && Objects.equals(subject, ter.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teacher, subject);
    }
}
