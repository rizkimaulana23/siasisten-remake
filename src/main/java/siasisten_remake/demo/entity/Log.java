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
    private int noLog; // Ganti no_log menjadi noLog

    @ManyToOne
    @JoinColumn(name = "kode_lmk")
    private LowonganMataKuliah lowonganMataKuliah;

    @ManyToOne
    @JoinColumn(name = "npm")
    private Mahasiswa mahasiswa;

    private LocalDateTime timestampLog; // Ganti timestamp_log menjadi timestampLog

    private Duration durasi;

    private String kategori;

    private String deskripsi;

    private String status;

    private LocalDateTime waktuDibuat; // Ganti waktu_dibuat menjadi waktuDibuat

    private LocalDateTime waktuDiubah; // Ganti waktu_diubah menjadi waktuDiubah
}
