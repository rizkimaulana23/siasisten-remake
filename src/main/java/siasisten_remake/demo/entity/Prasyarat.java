package siasisten_remake.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "prasyarat", schema = "siasisten")
public class Prasyarat {

    @Id
    private int id_prasyarat;

    @ManyToOne
    @JoinColumn(name = "kode_lmk")
    private LowonganMataKuliah lowonganMataKuliah;

    private String nama_prasyarat;

    private String nilai;
}
