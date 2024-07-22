package siasisten_remake.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import siasisten_remake.demo.entity.Prasyarat;

@Repository
public interface PrasyaratRepository extends JpaRepository<Prasyarat, Integer> {
}
