package siasisten_remake.demo.controller;

import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EditLogController {


    @GetMapping("/editlog")
    public String EditLogs(Session session, Model model){

        return "/EditLog";
    }
    @PostMapping("/editlog")
    public String SaveLogs(Session session, Model model){
        return "/EditLog";
    }
}
