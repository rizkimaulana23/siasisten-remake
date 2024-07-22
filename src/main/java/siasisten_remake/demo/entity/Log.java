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
    private int no_log;

    @ManyToOne
    @JoinColumn(name = "kode_lmk")
    private LowonganMataKuliah lowonganMataKuliah;

    @ManyToOne
    @JoinColumn(name = "npm")
    private Mahasiswa mahasiswa;

    private LocalDateTime timestamp_log;

    private Duration durasi;

    private String kategori;

    private String deskripsi;

    private String status;

    private LocalDateTime waktu_dibuat;

    private LocalDateTime waktu_diubah;
}
