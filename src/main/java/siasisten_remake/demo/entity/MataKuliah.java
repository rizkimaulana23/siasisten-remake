package siasisten_remake.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "matakuliah", schema = "siasisten")
public class MataKuliah {

    @Id
    private String kode_mk;

    private String nama;

    @OneToMany(mappedBy = "mataKuliah")
    private List<LowonganMataKuliah> listLowonganMataKuliah;
}
