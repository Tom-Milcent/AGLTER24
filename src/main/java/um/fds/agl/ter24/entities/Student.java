package um.fds.agl.ter24.entities;

import javax.persistence.Entity;

@Entity
public class Student extends UserTER{

    // ici on mettra le groupe

    public Student(String firstName, String lastName){
        super(firstName, lastName);
        String[] roles={"ROLE_STUDENT"};
        this.setRoles(roles);
    }

    public Student(long id, String firstName, String lastName) {
        super(id, firstName, lastName);
        String[] roles = {"ROLE_STUDENT"};
        this.setRoles(roles);
    }
    public Student() {}


}
