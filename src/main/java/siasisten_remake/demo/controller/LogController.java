package siasisten_remake.demo.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import siasisten_remake.demo.entity.*;
import siasisten_remake.demo.repository.LmkMahasiswaRepository;
import siasisten_remake.demo.repository.LogRepository;
import siasisten_remake.demo.repository.LowonganMataKuliahRepository;
import siasisten_remake.demo.repository.MahasiswaRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/log")
public class LogController {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private LmkMahasiswaRepository lmkMahasiswaRepository;

    @Autowired
    private MahasiswaRepository mahasiswaRepository;

    @Autowired
    private LowonganMataKuliahRepository lowonganMataKuliahRepository;

    @GetMapping("/logs")
    public String showLog(Model model) {
        List<Log> logs = logRepository.findAll(); // Mengambil semua data log
        model.addAttribute("logs", logs);
        return "LogView"; // Nama file HTML di resources/templates
    }

    @GetMapping("/mataKuliahAsdos")
    public String mataKuliahAsdosDiterima(Model model, HttpSession session) {

        Mahasiswa mahasiswa = mahasiswaRepository.findFirstByUsername(session.getAttribute("username").toString());
        List<LmkMahasiswa> lmkMahasiswaList = lmkMahasiswaRepository.findAllByMahasiswa(mahasiswa);

        List<String> semesterList = new ArrayList<>();
        List<String> tahunAjaranList = new ArrayList<>();
        for (LmkMahasiswa m : lmkMahasiswaList) {
            String tahunAjaran = m.getLowonganMataKuliah().getPeriode().getTahun_ajaran();
            String last = tahunAjaran.substring(tahunAjaran.length()-1);
            if (last.equals("1")) {
                semesterList.add("Ganjil");
            } else if (last.equals("2")) {
                semesterList.add("Genap");
            } else if (last.equals("3")) {
                semesterList.add("Pendek");
            }

            String tahun = tahunAjaran.substring(0, 9);
            tahunAjaranList.add(tahun);
        }

        List<String> dosenList = new ArrayList<>();
        for (LmkMahasiswa m : lmkMahasiswaList) {
            if (m.getLowonganMataKuliah().getDosenLmk().size() > 0) {
                StringBuilder daftarDosenString = new StringBuilder();
                for (DosenLmk dl : m.getLowonganMataKuliah().getDosenLmk()) {
                    daftarDosenString.append(dl.getDosen().getUsername());
                    daftarDosenString.append(", ");
                }
                String result = daftarDosenString.toString();
                result = result.substring(0, result.length()-2);
                dosenList.add(result);
            }
            dosenList.add("");
        }

        model.addAttribute("mataKuliahAsdos", lmkMahasiswaList);
        model.addAttribute("semesterList", semesterList);
        model.addAttribute("dosenList", dosenList);
        model.addAttribute("tahunAjaranList", tahunAjaranList);

        return "daftar_mata_kuliah_log";
    }

    @GetMapping("/seeLog/{kodeLmk}")
    public String seeLog (Model model, HttpSession session, @PathVariable String kodeLmk) {
        return "lihat_log";
    }
}
