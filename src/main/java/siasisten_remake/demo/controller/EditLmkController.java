package siasisten_remake.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import siasisten_remake.demo.entity.LowonganMataKuliah;
import siasisten_remake.demo.repository.LowonganMataKuliahRepository;
import siasisten_remake.demo.repository.MataKuliahRepository;
import siasisten_remake.demo.repository.PeriodeRepository;

@Controller
public class EditLmkController {

    @Autowired
    private LowonganMataKuliahRepository lowonganMataKuliahRepository;

    @Autowired
    private MataKuliahRepository mataKuliahRepository;

    @Autowired
    private PeriodeRepository periodeRepository;

    @GetMapping("/{kode_lmk}/edit")
    public String editLmk(
            @PathVariable String kode_lmk,
            Model model) {

        LowonganMataKuliah lowonganMataKuliah = lowonganMataKuliahRepository.getLmkUsingKode(kode_lmk);

        model.addAttribute("lmk", lowonganMataKuliah);
        return "edit_lowongan";
    }

    @PostMapping("/{kode_lmk}/edit")
    public String editLmkPost(
            @PathVariable String kode_lmk,
            @RequestParam String kodeMk,
            @RequestParam String tahunAjaran,
            @RequestParam String statusLowongan,
            @RequestParam String jumlahLowongan
            ) {
        LowonganMataKuliah lowonganMataKuliah = lowonganMataKuliahRepository.getLmkUsingKode(kode_lmk);
        if (!mataKuliahRepository.checkExistByKodeMk(kodeMk)) {
            return "redirect:/" + kode_lmk + "/edit";
        }
        if (!periodeRepository.checkExistByTahunAjaran(tahunAjaran)) {
            return "redirect:/" + kode_lmk + "/edit";
        }

        lowonganMataKuliah.setMataKuliah(mataKuliahRepository.getMataKuliahByKode(kodeMk));
        lowonganMataKuliah.setPeriode(periodeRepository.getByTahunAjaran(tahunAjaran));
        lowonganMataKuliah.setStatus_lowongan(statusLowongan);
        lowonganMataKuliah.setJumlah_lowongan(Integer.parseInt(jumlahLowongan));

        lowonganMataKuliahRepository.save(lowonganMataKuliah);
        return "redirect:/home";
    }
}
