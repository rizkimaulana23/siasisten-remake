package siasisten_remake.demo.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import siasisten_remake.demo.entity.LmkMahasiswa;
import siasisten_remake.demo.entity.LowonganMataKuliah;
import siasisten_remake.demo.entity.Mahasiswa;
import siasisten_remake.demo.entity.MataKuliah;
import siasisten_remake.demo.event.application_event.DaftarLowonganEvent;
import siasisten_remake.demo.repository.LmkMahasiswaRepository;
import siasisten_remake.demo.repository.LowonganMataKuliahRepository;
import siasisten_remake.demo.repository.MahasiswaRepository;
import siasisten_remake.demo.repository.MataKuliahRepository;

import java.util.List;

@Slf4j
@Controller
@Aspect
public class RegistrasiAsdosController {

    @Autowired
    private MahasiswaRepository mahasiswaRepository;

    @Autowired
    private MataKuliahRepository mataKuliahRepository;

    @Autowired
    private LmkMahasiswaRepository lmkMahasiswaRepository;

    @Autowired
    private LowonganMataKuliahRepository lowonganMataKuliahRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @GetMapping("/{kode_lmk}/daftar")
    public String daftarAsdos(
            @PathVariable String kode_lmk,
            Model model,
            HttpSession session
    ) {

        String username = session.getAttribute("username").toString();
        Mahasiswa mahasiswa = mahasiswaRepository.findFirstByUsername(username);
        LowonganMataKuliah lmk = lowonganMataKuliahRepository.getLmkUsingKode(kode_lmk);
        MataKuliah mataKuliah = lmk.getMataKuliah();

        model.addAttribute("mataKuliah", mataKuliah);
        model.addAttribute("mahasiswa", mahasiswa);
        model.addAttribute("lmkMahasiswa", new LmkMahasiswa());

        return "daftar_asdos";
    }

    @PostMapping("/{kode_lmk}/daftar")
    @After(value = "execution(* siasisten_remake.demo.event.application_listener.DaftarLowonganListener.*(..)) && args(kode_lmk, lmkMahasiswa, session)", argNames = "kode_lmk,lmkMahasiswa,session")
    public String daftarAsdosPost(
            @PathVariable String kode_lmk,
            @ModelAttribute LmkMahasiswa lmkMahasiswa,
            HttpSession session
    ) {

        lmkMahasiswa.setStatus("melamar");
        lmkMahasiswa.setMahasiswa(mahasiswaRepository.findFirstByUsername(session.getAttribute("username").toString()));
        lmkMahasiswa.setLowonganMataKuliah(lowonganMataKuliahRepository.getLmkUsingKode(kode_lmk));

        lmkMahasiswaRepository.save(lmkMahasiswa);
        eventPublisher.publishEvent(new DaftarLowonganEvent(this, kode_lmk));
        return "redirect:/home";
    }

    @GetMapping("/{kode_lmk}/detail")
    public String detailAsdos(Model model,
                              HttpSession session,
                              @PathVariable String kode_lmk

            ) {
        LowonganMataKuliah lowonganMataKuliah = lowonganMataKuliahRepository.getLmkUsingKode(kode_lmk);
        MataKuliah mataKuliah = lowonganMataKuliah.getMataKuliah();
        Mahasiswa mahasiswa = mahasiswaRepository.findFirstByUsername(session.getAttribute("username").toString());
        List<LmkMahasiswa> lmkMahasiswaList = lmkMahasiswaRepository.findAllByMahasiswaAndLowonganMataKuliah(mahasiswa, lowonganMataKuliah);
        LmkMahasiswa lmkMahasiswa = lmkMahasiswaList.getFirst();

        model.addAttribute("mataKuliah", mataKuliah);
        model.addAttribute("mahasiswa", mahasiswa);
        model.addAttribute("lmkMahasiswa", lmkMahasiswa);

        return "detail_asdos";
    }
}
