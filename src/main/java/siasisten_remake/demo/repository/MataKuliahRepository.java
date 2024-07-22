package siasisten_remake.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import siasisten_remake.demo.entity.MataKuliah;

public interface MataKuliahRepository extends JpaRepository<MataKuliah, String> {

    @Query(
            """
            SELECT m
            FROM MataKuliah m
            WHERE m.kode_mk = :kode
            """
    )
    public MataKuliah getMataKuliahByKode(String kode);

    @Query (
            """
            SELECT CASE 
                WHEN COUNT(m) > 0 THEN TRUE
                ELSE FALSE END
            FROM MataKuliah m
            WHERE m.kode_mk = :kode_mk
            """
    )
    public boolean checkExistByKodeMk(String kode_mk);
}
