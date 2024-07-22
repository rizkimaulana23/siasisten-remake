package siasisten_remake.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import siasisten_remake.demo.entity.LmkMahasiswa;
import siasisten_remake.demo.entity.LowonganMataKuliah;
import siasisten_remake.demo.entity.Mahasiswa;
import siasisten_remake.demo.event.application_event.DiterimaLowonganEvent;
import siasisten_remake.demo.repository.*;

import java.util.List;
import java.util.Map;

@Controller
public class EditLmkController {

    @Autowired
    private LowonganMataKuliahRepository lowonganMataKuliahRepository;

    @Autowired
    private MataKuliahRepository mataKuliahRepository;

    @Autowired
    private PeriodeRepository periodeRepository;

    @Autowired
    private LmkMahasiswaRepository lmkMahasiswaRepository;

    @Autowired
    private MahasiswaRepository mahasiswaRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @GetMapping("/{kode_lmk}/edit")
    public String editLmk(
            @PathVariable String kode_lmk,
            Model model) {

        LowonganMataKuliah lowonganMataKuliah = lowonganMataKuliahRepository.getLmkUsingKode(kode_lmk);
        List<LmkMahasiswa> lmkMahasiswa = lmkMahasiswaRepository.findAllByLowonganMataKuliah(lowonganMataKuliah);

        model.addAttribute("lmk", lowonganMataKuliah);
        model.addAttribute("lmkMahasiswa", lmkMahasiswa);

        return "edit_lowongan";
    }

    @PostMapping("/{kode_lmk}/edit")
    public String editLmkPost(
            @PathVariable String kode_lmk,
            @RequestParam String kodeMk,
            @RequestParam String tahunAjaran,
            @RequestParam String statusLowongan,
            @RequestParam String jumlahLowongan,
            @RequestParam Map<String, String> data
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

        data.forEach((key, value) -> {
            System.out.println("Key: " + key + " Value: " + value);
        });

        lowonganMataKuliahRepository.save(lowonganMataKuliah);
        return "redirect:/home";
    }

    @PostMapping("/{kode_lmk}/edit_mahasiswa")
    public String editLmkDaftarMahasiswaPost(
            @PathVariable String kode_lmk,
            @RequestParam Map<String, String> data
    ) {
        LowonganMataKuliah lowonganMataKuliah = lowonganMataKuliahRepository.getLmkUsingKode(kode_lmk);

        String prefix = "username_";
        data.forEach((key, value) -> {
            System.out.println("Key: " + key + " Value: " + value);
            String npm = key.substring(prefix.length());
            String statusTerbaru = value;
            Mahasiswa mahasiswa = mahasiswaRepository.findByNpm(npm);

            List<LmkMahasiswa> lmkMahasiswaList = lmkMahasiswaRepository.findAllByMahasiswaAndLowonganMataKuliah(mahasiswa, lowonganMataKuliah);

            LmkMahasiswa lmkMahasiswa = lmkMahasiswaList.getFirst();

            lmkMahasiswa.setStatus(statusTerbaru);
            lmkMahasiswaRepository.save(lmkMahasiswa);

        });

        eventPublisher.publishEvent(new DiterimaLowonganEvent(this, kode_lmk));
        return "redirect:/" + kode_lmk + "/edit";
    }
}
