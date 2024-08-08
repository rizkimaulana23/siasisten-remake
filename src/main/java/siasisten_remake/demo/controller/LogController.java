package siasisten_remake.demo.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import siasisten_remake.demo.entity.*;
import siasisten_remake.demo.repository.LmkMahasiswaRepository;
import siasisten_remake.demo.repository.LogRepository;
import siasisten_remake.demo.repository.LowonganMataKuliahRepository;
import siasisten_remake.demo.repository.MahasiswaRepository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
        List<Log> logs = logRepository.findAll();
        model.addAttribute("logs", logs);
        return "LogView";
    }

    @GetMapping("/mataKuliahAsdos")
    public String mataKuliahAsdosDiterima(Model model, HttpSession session) {

        Mahasiswa mahasiswa = mahasiswaRepository.findFirstByUsername(session.getAttribute("username").toString());
        List<LmkMahasiswa> lmkMahasiswaList = lmkMahasiswaRepository.findAllByMahasiswaAndStatus(mahasiswa, "diterima");

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
        LowonganMataKuliah lmk = lowonganMataKuliahRepository.getLmkUsingKode(kodeLmk);
        MataKuliah mataKuliah = lmk.getMataKuliah();

        List<Log> logList = logRepository.findAllByLowonganMataKuliah(lmk);
        model.addAttribute("logs", logList);

        DateTimeFormatter durasiFormat = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter tanggalFormat = DateTimeFormatter.ofPattern("dd MMM yyyy");

        model.addAttribute("durasiFormat", durasiFormat);
        model.addAttribute("tanggalFormat", tanggalFormat);
        model.addAttribute("kodeLmk", kodeLmk);
        model.addAttribute("mataKuliah", mataKuliah);

        return "lihat_log";
    }

    @GetMapping("/addLog/{kodeLmk}")
    public String addLog(
            Model model,
            HttpSession session,
            @PathVariable String kodeLmk) {
        model.addAttribute("log", new Log());
        model.addAttribute("kodeLmk", kodeLmk);
        return "tambah_log";
    }

    @PostMapping("/addLog/{kodeLmk}")
    public String processNewLog(
            HttpSession session,
            @PathVariable String kodeLmk,
            @RequestParam(name = "waktu_mulai") String waktu_mulai,
            @RequestParam(name = "waktu_selesai") String waktu_selesai,
            @RequestParam(name = "tanggal") String tanggal,
            @ModelAttribute Log log_model
    ) {
        DateTimeFormatter formatTanggal = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatWaktu = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDate date = LocalDate.parse(tanggal, formatTanggal);

        waktu_mulai = waktu_mulai + ":00";
        waktu_selesai= waktu_selesai + ":00";
        LocalTime timeMulai = LocalTime.parse(waktu_mulai, formatWaktu);
        LocalTime timeSelesai = LocalTime.parse(waktu_selesai, formatWaktu);

        if(timeMulai.isAfter(timeSelesai)) {
            timeSelesai = timeSelesai.plusHours(24);
        }

        // Kategori dan deskripsi sudah di-set sebelumnya

        // Set timestamp log
        LocalDateTime tanggalLocalDateTime = LocalDateTime.of(date, timeMulai);
        log_model.setTimestampLog(tanggalLocalDateTime);

        // Set durasi
        Duration durationBaru = Duration.between(timeMulai, timeSelesai);
        log_model.setDurasi(durationBaru);

        // Set lowongan mata kuliah
        LowonganMataKuliah lmk = lowonganMataKuliahRepository.getLmkUsingKode(kodeLmk);
        log_model.setLowonganMataKuliah(lmk);

        // Set mahasiswa
        Mahasiswa mahasiswa = mahasiswaRepository.findFirstByUsername(session.getAttribute("username").toString());
        log_model.setMahasiswa(mahasiswa);

        // Set waktu dibuat dan waktu diubah
        log_model.setWaktuDibuat(LocalDateTime.now());
        log_model.setWaktuDiubah(LocalDateTime.now());

        // Set status (secara default)
        log_model.setStatus("dilaporkan");

        logRepository.save(log_model);

        log.info("Model Log didapatkan");
        log.info("Timestamp Log " + tanggal);
        log.info("Waktu Mulai " + waktu_mulai);
        log.info("Waktu Selesai " + waktu_selesai);
        return "redirect:/log/seeLog/" + kodeLmk;
    }
}
