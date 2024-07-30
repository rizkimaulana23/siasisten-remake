package siasisten_remake.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "log", schema = "siasisten")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no_log")
    private int noLog; // Ganti no_log menjadi noLog

    @ManyToOne
    @JoinColumn(name = "kode_lmk", referencedColumnName = "kode_lmk")
    private LowonganMataKuliah lowonganMataKuliah;

    @ManyToOne
    @JoinColumn(name = "npm", referencedColumnName = "npm")
    private Mahasiswa mahasiswa;

    @Column(name = "timestamp_log")
    private LocalDateTime timestampLog;


    private Duration durasi;

    private String kategori;

    private String deskripsi;

    private String status;

    @Column(name = "waktu_dibuat")
    private LocalDateTime waktuDibuat; // Ganti waktu_dibuat menjadi waktuDibuat

    @Column(name = "waktu_diubah")
    private LocalDateTime waktuDiubah; // Ganti waktu_diubah menjadi waktuDiubah
}
