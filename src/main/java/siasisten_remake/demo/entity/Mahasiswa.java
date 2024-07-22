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
@Table(name = "mahasiswa", schema = "siasisten")
public class Mahasiswa {

    @Id
    private String npm;

    private String nama;

    private String email;

    private String status;

    private String no_hp;

    private String nama_bank;

    private String nama_rekening;

    private String no_rek;

    private String username;

    private String password;

    @OneToMany(mappedBy = "mahasiswa")
    private List<Log> logs;

    @OneToMany(mappedBy = "mahasiswa")
    private List<LmkMahasiswa> listLmkMahasiswa;
}
