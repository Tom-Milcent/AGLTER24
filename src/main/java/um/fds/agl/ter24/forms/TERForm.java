package um.fds.agl.ter24.forms;

import um.fds.agl.ter24.entities.Teacher;

public class TERForm {
    private Long teacher;
    private long id;
    private String subject;

    public TERForm(long id, Long teacher, String subject) {
        this.teacher = teacher;
        this.subject = subject;
        this.id = id;
    }

    public TERForm() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getTeacher() {
        return teacher;
    }

    public void setTeacher(Long teacher) {
        this.teacher = teacher;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
