package siasisten_remake.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dosen", schema = "siasisten")
public class Dosen {

    @Id
    private String nid;

    private String nama;

    private String email;

    private String no_hp;

    private String username;

    private String password;

    @OneToMany(mappedBy = "dosen")
    private List<DosenLmk> listDosenLmk;
}
