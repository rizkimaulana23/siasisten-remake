package siasisten_remake.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import siasisten_remake.demo.entity.DosenLmk;

@Repository
public interface DosenLmkRepository extends JpaRepository<DosenLmk, Long> {
}
