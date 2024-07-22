package siasisten_remake.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "periode", schema = "siasisten")
public class Periode {

    @Id
    private String tahun_ajaran;

    @OneToMany(mappedBy = "periode")
    private List<LowonganMataKuliah> listLowonganMataKuliah;

}
