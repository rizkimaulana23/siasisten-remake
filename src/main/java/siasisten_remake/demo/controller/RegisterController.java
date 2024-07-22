package siasisten_remake.demo.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import siasisten_remake.demo.entity.Mahasiswa;
import siasisten_remake.demo.repository.MahasiswaRepository;

@Slf4j
@Controller
public class RegisterController {

    @Autowired
    private MahasiswaRepository mahasiswaRepository;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("mahasiswa", new Mahasiswa());
        return "register";
    }

    @PostMapping("/register")
    public String registerPost(
            @ModelAttribute Mahasiswa mahasiswa,
            HttpSession session) {
        session.setAttribute("username", mahasiswa.getUsername());
        mahasiswaRepository.save(mahasiswa);
        log.info("Register successful");
        return "redirect:/home";
    }

}
