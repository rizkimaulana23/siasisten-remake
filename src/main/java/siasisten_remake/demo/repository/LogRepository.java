package siasisten_remake.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import siasisten_remake.demo.entity.Log;

public interface LogRepository extends JpaRepository<Log, Integer> {
}
