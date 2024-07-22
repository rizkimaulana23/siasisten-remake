package siasisten_remake.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "dosen_lmk", schema = "siasisten")
public class DosenLmk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_dosen_lmk;

    @ManyToOne
    @JoinColumn(name = "kode_lmk")
    private LowonganMataKuliah lowonganMataKuliah;

    @ManyToOne
    @JoinColumn(name = "nid")
    private Dosen dosen;
}
