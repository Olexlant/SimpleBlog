package com.example.simpleblog.Registration;

import com.example.simpleblog.Users.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/process_register")
    public String signUp(User user) {
        registrationService.register(user);
        return "redirect:/";
    }
}
