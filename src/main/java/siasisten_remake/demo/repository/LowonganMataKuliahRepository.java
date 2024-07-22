package siasisten_remake.demo.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import siasisten_remake.demo.entity.LowonganMataKuliah;

@Repository
public interface LowonganMataKuliahRepository extends JpaRepository<LowonganMataKuliah, Integer> {

    @Query(
            """
            SELECT l
            FROM LowonganMataKuliah l
            WHERE l.kode_lmk = :kode
            """
    )
    public LowonganMataKuliah getLmkUsingKode(String kode);


    @Modifying
    @Transactional
    @Query("""
           UPDATE LowonganMataKuliah l
           SET l.jumlah_pelamar = (
           SELECT COUNT(*) FROM LmkMahasiswa lm
           WHERE lm.lowonganMataKuliah.kode_lmk = :kode
           )
           WHERE l.kode_lmk = :kode
           """)
    public int updateJumlahPendaftarSetelahPendaftaran(@Param("kode") String kode_lmk);

    @Modifying
    @Transactional
    @Query("""
           UPDATE LowonganMataKuliah l
           SET l.jumlah_diterima = (
           SELECT COUNT(*) FROM LmkMahasiswa lm
           WHERE lm.lowonganMataKuliah.kode_lmk = :kode AND lm.status = 'diterima'
           )
           WHERE l.kode_lmk = :kode
           """)
    public int updateJumlahDiterimaSetelahPendaftaran(@Param("kode") String kode_lmk);


}
