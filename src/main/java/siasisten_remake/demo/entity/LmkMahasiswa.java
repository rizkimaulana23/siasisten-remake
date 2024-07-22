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
@Table(name = "lmk_mahasiswa", schema = "siasisten")
public class LmkMahasiswa {

    @Id
    @GeneratedValue(generator = "id_lmk_mahasiswa", strategy = GenerationType.SEQUENCE)
    private int id_lmk_mahasiswa;

    @ManyToOne
    @JoinColumn(name = "kode_lmk")
    private LowonganMataKuliah lowonganMataKuliah;

    @ManyToOne
    @JoinColumn(name = "npm")
    private Mahasiswa mahasiswa;

    private String status;

    private float ipk;

    private int sks;
}
