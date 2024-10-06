package um.fds.agl.ter24.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import java.util.Arrays;


@Entity
@Inheritance
public abstract class UserTER {
    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    private @Id    @GeneratedValue Long id;
    private String firstName;
    private String lastName;
    private @JsonIgnore String password;
    private @JsonIgnore String[] roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    public UserTER(){}
    public UserTER(String firstName, String lastName, String password, String[] roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        setPassword(password);
        this.roles = roles;
    }

    public UserTER(String firstName, String lastName) {
        //default : the password is the name, no role ...
        this(firstName, lastName, lastName, new String[0]);
    }

    public UserTER(long id, String firstName, String lastName) {
        //default : the password is the name, no role ...
        this(firstName, lastName, lastName, new String[0]);
        this.id = id;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserTER)) return false;

        UserTER user = (UserTER) o;

        if (!getId().equals(user.getId())) return false;
        if (!getFirstName().equals(user.getFirstName())) return false;
        if (!getLastName().equals(user.getLastName())) return false;
        if (!password.equals(user.password)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getFirstName().hashCode();
        result = 31 * result + getLastName().hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + Arrays.hashCode(roles);
        return result;
    }
}
