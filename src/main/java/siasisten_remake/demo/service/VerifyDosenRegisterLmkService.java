package siasisten_remake.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import siasisten_remake.demo.entity.Dosen;
import siasisten_remake.demo.repository.DosenRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class VerifyDosenRegisterLmkService {

    @Autowired
    private DosenRepository dosenRepository;

    public boolean verify(String daftarDosen) {
        daftarDosen = daftarDosen.replaceAll("\\s+", ""); // Remove whitespaces
        List<String> listDosen = List.of(daftarDosen.split(","));
        for (String dosen : listDosen) {
            log.info("DAFTAR DOSEN " + dosen);
            if (!dosenRepository.existsByUsername(dosen)) return false;
        }
        return true;
    }

    public List<Dosen> getDosen(String daftarDosen) {
        daftarDosen = daftarDosen.replaceAll("\\s+", ""); // Remove whitespaces
        List<String> listDosen = List.of(daftarDosen.split(","));
        List<Dosen> listDosenReturn = new ArrayList<>();
        for (String dosen : listDosen) {
            listDosenReturn.add(dosenRepository.findByUsername(dosen));
        }
        return listDosenReturn;
    }
}
