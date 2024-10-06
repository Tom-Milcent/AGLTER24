package um.fds.agl.ter24.entities;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;

@Entity
public class Sujet {
    private  @Id    @GeneratedValue String titre;
    private @OneToOne Teacher teacher;

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getTitre() {
        return titre;
    }

    public Teacher getTeacher() {
        return teacher;
    }
}
