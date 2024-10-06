package um.fds.agl.ter24.entities;

import javax.persistence.Entity;
import java.util.Arrays;

@Entity
public class TERManager extends UserTER {


    public TERManager() {}

    public TERManager(String firstName, String lastName, String password, String... roles) {
        super(firstName,lastName,password, roles);
    }








    @Override
    public String toString() {
        return "Manager{" +
                "id=" + getId()+
                ", name='" + getLastName() + '\'' +
                ", roles=" + Arrays.toString(getRoles()) +
                '}';
    }
}