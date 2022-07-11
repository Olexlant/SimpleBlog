package com.example.simpleblog.Users;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/myaccount")
    public String showmyprofile(Model model,@AuthenticationPrincipal UserDetails userDetails){
        User user = (User) userService.loadUserByUsername(userDetails.getUsername());
        model.addAttribute("user",user);
        return "myprofile";
    }

    @GetMapping("/editprofile")
    public String showeditprofile(Model model,@AuthenticationPrincipal UserDetails userDetails){
        User user = (User) userService.loadUserByUsername(userDetails.getUsername());
        model.addAttribute("user",user);
        return "editprofile";
    }

    @PostMapping("/saveprofilechanges")
    public String saveprofilechanges(Model model, @AuthenticationPrincipal UserDetails userDetails, @RequestParam String firstname,@RequestParam String lastname,@RequestParam String phonenum,@RequestParam String email){
        User user = (User) userService.loadUserByUsername(userDetails.getUsername());
        user.setFirstName(firstname);
        user.setLastName(lastname);
        user.setPhonenum(phonenum);
        user.setEmail(email);
        userRepository.save(user);
        return "redirect:/myaccount";
    }
}
