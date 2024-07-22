package siasisten_remake.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import siasisten_remake.demo.entity.Dosen;
import siasisten_remake.demo.entity.Mahasiswa;
import siasisten_remake.demo.repository.DosenRepository;
import siasisten_remake.demo.repository.MahasiswaRepository;

@Controller
public class LoginController {

    @Autowired
    private MahasiswaRepository mahasiswaRepository;

    @Autowired
    private DosenRepository dosenRepository;

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(
            @RequestParam String username,
            @RequestParam String password,
            Model model,
            HttpSession session) {
        Mahasiswa mahasiswa = mahasiswaRepository.findFirstByUsername(username);
        Dosen dosen = dosenRepository.findFirstByUsername(username);

        if (mahasiswa != null) {
            if (mahasiswa.getPassword().equals(password)) {
                session.setAttribute("username", username);
                session.setAttribute("type", "mahasiswa");
                return "redirect:/home";
            }
        } else if (dosen != null) {
            if (dosen.getPassword().equals(password)) {
                session.setAttribute("username", username);
                session.setAttribute("type", "dosen");
                return "redirect:/home";
            }
        }
        return "login";
    }
}
