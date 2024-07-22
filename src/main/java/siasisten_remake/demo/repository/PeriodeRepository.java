package siasisten_remake.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import siasisten_remake.demo.entity.Periode;

import java.util.List;

@Repository
public interface PeriodeRepository extends JpaRepository<Periode, String> {

    @Query(
            """
            SELECT p.tahun_ajaran
            FROM Periode p
            """
    )
    List<String> getTahunAjaranAll();

    @Query(
            """
            SELECT CASE 
                WHEN COUNT(p) > 0 THEN TRUE
                ELSE FALSE 
            END
            FROM Periode p
            WHERE p.tahun_ajaran = :tahun_ajaran
            """
    )
    boolean checkExistByTahunAjaran(String tahun_ajaran);

    @Query(
            """
            SELECT p
            FROM Periode p
            WHERE p.tahun_ajaran = :tahun_ajaran
            """
    )
    Periode getByTahunAjaran(String tahun_ajaran);
}
