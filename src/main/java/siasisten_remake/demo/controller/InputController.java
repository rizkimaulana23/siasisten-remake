package siasisten_remake.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import siasisten_remake.demo.entity.*;
import siasisten_remake.demo.repository.*;
import siasisten_remake.demo.service.VerifyDosenRegisterLmkService;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/input")
public class InputController {

    @Autowired
    private MataKuliahRepository mataKuliahRepository;

    @Autowired
    private LowonganMataKuliahRepository lowonganMataKuliahRepository;

    @Autowired
    private PeriodeRepository periodeRepository;

    @Autowired
    private DosenRepository dosenRepository;

    @Autowired
    private VerifyDosenRegisterLmkService verifyDosenRegisterLmkService;

    @Autowired
    private DosenLmkRepository dosenLmkRepository;

    @GetMapping("/mata_kuliah")
    public String inputMataKuliah(Model model) {
        model.addAttribute("mataKuliah", new MataKuliah());
        return "input/mata_kuliah";
    }

    @GetMapping("/lowongan_mata_kuliah")
    public String inputLowonganMataKuliah(Model model) {
        // Mendapatkan daftar tahun ajaran
        List<String> listPeriode = periodeRepository.getTahunAjaranAll();
        model.addAttribute("periode", listPeriode);

        // Daftar status
        List<String> daftarStatus = Arrays.asList("Buka", "Tutup");
        model.addAttribute("status", daftarStatus);

        // Daftar dosen
        List<Dosen> daftarDosen = dosenRepository.findAll();
        model.addAttribute("dosen", daftarDosen);

        model.addAttribute("lmk_dosen", new DosenLmk());
        model.addAttribute("lowongan", new LowonganMataKuliah());
        return "input/lowongan_mata_kuliah";
    }

    @PostMapping("/mata_kuliah")
    public String inputMataKuliahPost(@ModelAttribute MataKuliah mataKuliah) {
        mataKuliahRepository.save(mataKuliah);
        log.info("Input Mata Kuliah Sukses");
        return "redirect:/home";
    }

    @PostMapping("/lowongan_mata_kuliah")
    public String inputLowonganPost(
            @ModelAttribute LowonganMataKuliah lowongan,
            @RequestParam String mataKuliah,
            @RequestParam String daftarDosen,
            RedirectAttributes redirectAttributes) {
        lowongan.setMataKuliah(mataKuliahRepository.getMataKuliahByKode(mataKuliah));
        lowongan.setJumlah_diterima(0);
        lowongan.setJumlah_pelamar(0);

        if (!verifyDosenRegisterLmkService.verify(daftarDosen)) {
            redirectAttributes.addFlashAttribute("warningMessages", "Dosen tidak valid");
            return "redirect:/input/lowongan_mata_kuliah";
        }

        lowonganMataKuliahRepository.save(lowongan);

        List<Dosen> listDosen = verifyDosenRegisterLmkService.getDosen(daftarDosen);
        for (Dosen dosen : listDosen) {
            DosenLmk dosenLmk = new DosenLmk();
            dosenLmk.setDosen(dosen);
            dosenLmk.setLowonganMataKuliah(lowongan);
            dosenLmkRepository.save(dosenLmk);
        }
        return "redirect:/home";
    }

    @GetMapping("/periode")
    public String inputPeriode(Model model) {
        model.addAttribute("periode", new Periode());
        return "input/periode";
    }

    @PostMapping("/periode")
    public String inputPeriodePost(@ModelAttribute Periode periode) {
        periodeRepository.save(periode);
        log.info("Input Periode Sukses");
        return "redirect:/home";
    }

    @GetMapping("/dosen")
    public String inputDosen(Model model) {
        model.addAttribute("dosen", new Dosen());
        return "input/dosen";
    }

    @PostMapping("/dosen")
    public String inputDosenPost(@ModelAttribute Dosen dosen) {
        dosenRepository.save(dosen);
        return "redirect:/home";
    }
}