package siasisten_remake.demo.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import siasisten_remake.demo.entity.Log;
import siasisten_remake.demo.repository.LogRepository;

import java.util.List;

@Controller
@Slf4j
public class LogController {

    @Autowired
    private LogRepository logRepository;

    @GetMapping("/logs")
    public String showLog(Model model) {
        List<Log> logs = logRepository.findAll(); // Mengambil semua data log
        model.addAttribute("logs", logs);
        return "LogView"; // Nama file HTML di resources/templates
    }

    @PostMapping("/logs")
    public String postLog() {
        // Logic for POST request if needed
        return "redirect:/logs";
    }
}
