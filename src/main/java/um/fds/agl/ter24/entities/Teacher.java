package um.fds.agl.ter24.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Teacher extends UserTER {
    private @ManyToOne TERManager terManager;
    public Teacher(){}
    public Teacher(String firstName, String lastName, String password, TERManager manager, String... roles) {
        super(firstName, lastName, password, roles);
        this.terManager=manager;
    }

    public Teacher(String firstName, String lastName, TERManager manager) {
        super(firstName, lastName);
        String[] roles={"ROLE_TEACHER"};
        this.setRoles(roles);
        this.terManager=manager;
    }


    public TERManager getTerManager() {
        return terManager;
    }

    public void setTerManager(TERManager terManager) {
        this.terManager = terManager;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + getId() +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", manager='"+ getTerManager() + '\'' +
                '}';
    }

    public String Name() {
        return getFirstName() + ' ' + getLastName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Teacher)) return false;
        if (!super.equals(o)) return false;

        Teacher teacher = (Teacher) o;

        return getTerManager() != null ? getTerManager().equals(teacher.getTerManager()) : teacher.getTerManager() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getTerManager() != null ? getTerManager().hashCode() : 0);
        return result;
    }
}
