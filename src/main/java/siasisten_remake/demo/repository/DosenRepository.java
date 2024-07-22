package siasisten_remake.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import siasisten_remake.demo.entity.Dosen;

@Repository
public interface DosenRepository extends JpaRepository<Dosen, String> {

    public boolean existsByUsername(String username);

    public Dosen findByUsername(String username);

    public Dosen findFirstByUsername(String username);
}
