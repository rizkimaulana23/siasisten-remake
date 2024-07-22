package siasisten_remake.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "lowonganmatakuliah", schema = "siasisten")
@Component
public class LowonganMataKuliah {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int kode_lmk;

    @ManyToOne
    @JoinColumn(name = "tahun_ajaran")
    private Periode periode;

    @ManyToOne
    @JoinColumn(name = "kode_mk")
    private MataKuliah mataKuliah;

    private String status_lowongan;

    private int jumlah_lowongan;

    private int jumlah_pelamar;

    private int jumlah_diterima;

    @OneToMany(mappedBy = "lowonganMataKuliah")
    private List<Prasyarat> listPrasyarat;

    @OneToMany(mappedBy = "lowonganMataKuliah")
    private List<DosenLmk> dosenLmk;

    @OneToMany(mappedBy = "lowonganMataKuliah")
    private List<Log> listLog;

    @OneToMany(mappedBy = "lowonganMataKuliah")
    private List<LmkMahasiswa> lmkMahasiswa;

}
