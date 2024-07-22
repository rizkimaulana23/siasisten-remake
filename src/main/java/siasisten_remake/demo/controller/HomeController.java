package siasisten_remake.demo.controller;

import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import siasisten_remake.demo.entity.Dosen;
import siasisten_remake.demo.entity.DosenLmk;
import siasisten_remake.demo.entity.LmkMahasiswa;
import siasisten_remake.demo.entity.LowonganMataKuliah;
import siasisten_remake.demo.repository.DosenRepository;
import siasisten_remake.demo.repository.LmkMahasiswaRepository;
import siasisten_remake.demo.repository.LowonganMataKuliahRepository;
import siasisten_remake.demo.repository.MahasiswaRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class HomeController {

    @Autowired
    private MahasiswaRepository mahasiswaRepository;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class HomeList {
        private LowonganMataKuliah lmk;
        private String dosen;
        private String status;
    }

    @Autowired
    private LowonganMataKuliahRepository lowonganMataKuliahRepository;

    @Autowired
    private DosenRepository dosenRepository;

    @Autowired
    private LmkMahasiswaRepository lmkMahasiswaRepository;

    @GetMapping("/home")
    public String home(
            Model model,
            HttpSession session) {
        List<LowonganMataKuliah> lmk = lowonganMataKuliahRepository.findAll();
        model.addAttribute("lmk", lmk);

        List<String> daftarDosenLmk = new ArrayList<>();
        for (LowonganMataKuliah iter : lmk) {
            List<DosenLmk> listDosen = iter.getDosenLmk();
            String value = "";
            for (DosenLmk dosenLmk : listDosen) {
                Dosen dosen = dosenLmk.getDosen();
                value = value + dosen.getUsername() + ", ";
            }
            if (value.length() > 0) {
                value = value.substring(0, value.length() - 2);
            }
            daftarDosenLmk.add(value);
        }

        List<String> daftarstatus = new ArrayList<>();
        for (LowonganMataKuliah iter : lmk) {
            String status = "";
            List<LmkMahasiswa> listLmkMahasiswa = lmkMahasiswaRepository
                    .findAllByMahasiswaAndLowonganMataKuliah(mahasiswaRepository.findFirstByUsername(session.getAttribute("username").toString()), iter);
            for (LmkMahasiswa lmkm : listLmkMahasiswa) {
                if (lmkm.getStatus().equals("melamar")) {
                    status = "melamar";
                } else if (lmkm.getStatus().equals("diterima")) {
                    status = "diterima";
                }
            }
            daftarstatus.add(status);
        }

        List<HomeList> homeList = new ArrayList<>();
        for (int i = 0; i < lmk.size(); i++) {
            HomeList home = new HomeList();
            home.setLmk(lmk.get(i));
            home.setDosen(daftarDosenLmk.get(i));
            home.setStatus(daftarstatus.get(i));
            homeList.add(home);
        }
        model.addAttribute("list", homeList);

        model.addAttribute("type", session.getAttribute("type").toString());
        if (session.getAttribute("type").equals("mahasiswa")) {
            return "home";
        } else {
            return "home_dosen";
        }
    }


}
