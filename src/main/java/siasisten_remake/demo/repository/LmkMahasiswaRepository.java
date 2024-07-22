package siasisten_remake.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import siasisten_remake.demo.entity.LmkMahasiswa;
import siasisten_remake.demo.entity.LowonganMataKuliah;
import siasisten_remake.demo.entity.Mahasiswa;

import java.util.List;

@Repository
public interface LmkMahasiswaRepository extends JpaRepository<LmkMahasiswa, Long> {

    public List<LmkMahasiswa> findAllByMahasiswaAndLowonganMataKuliah(Mahasiswa mahasiswa, LowonganMataKuliah lowonganMataKuliah);
}
