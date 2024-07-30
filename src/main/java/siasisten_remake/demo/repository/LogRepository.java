package siasisten_remake.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import siasisten_remake.demo.entity.Log;
import siasisten_remake.demo.entity.LowonganMataKuliah;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, Integer> {

    public List<Log> findAllByLowonganMataKuliah(LowonganMataKuliah lowonganMataKuliah);
}
