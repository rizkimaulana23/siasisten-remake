package siasisten_remake.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import siasisten_remake.demo.entity.Mahasiswa;

@Repository
public interface MahasiswaRepository extends JpaRepository<Mahasiswa, String> {

    public Mahasiswa findFirstByUsername(String username);
}
