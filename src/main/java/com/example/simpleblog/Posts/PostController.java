package com.example.simpleblog.Posts;

import com.example.simpleblog.Users.User;
import com.example.simpleblog.Users.UserRepository;
import com.example.simpleblog.Users.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PostController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PostService postService;
    private final PostRepository postRepository;

    public PostController(UserRepository userRepository, UserService userService, PostService postService, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.postService = postService;
        this.postRepository = postRepository;
    }
    @GetMapping("/")
    public String allposts(Model model, @AuthenticationPrincipal UserDetails userDetails){
        if (userDetails != null){
            User user = (User) userService.loadUserByUsername(userDetails.getUsername());
            model.addAttribute("user",user);
        }
        model.addAttribute("title","Home page");
        List<Post> posts = postRepository.findAll();
        model.addAttribute("posts",posts);
        return "home";
    }

    @GetMapping("/addpost")
    public String showaddpost(Model model){
        model.addAttribute("post",new Post());
        return "addpost";
    }

    @PostMapping("/postadding")
    public String addpost(Post post, Model model,@AuthenticationPrincipal UserDetails userDetails){
        postService.postadd(post);
        post.setAddedat(LocalDateTime.now());
        post.setUser((User) userService.loadUserByUsername(userDetails.getUsername()));
        postRepository.save(post);
        return "redirect:/";
    }

    @GetMapping("/myposts")
    public String showmypost(Model model, @AuthenticationPrincipal UserDetails userDetails){
        UserDetails user = userService.loadUserByUsername(userDetails.getUsername());
        List<Post> myposts = postRepository.findByUser((User) user);
        model.addAttribute("myposts",myposts);
        return "myposts";
    }

    @GetMapping("/editpost/{id}")
    public String showeditpost(Model model,@PathVariable(value = "id") long id){
        Post post = (Post) postRepository.findById(id).orElseThrow();
        model.addAttribute("post",post);
        return "editpost";
    }
    @PostMapping("/saveeditpost/{id}")
    public String saveeditpost(@PathVariable(value = "id") long id,@RequestParam String title,@RequestParam String topic, @RequestParam String description){
        Post post = (Post) postRepository.findById(id).orElseThrow();
        post.setTitle(title);
        post.setTopic(topic);
        post.setDescription(description);
        post.setAddedat(LocalDateTime.now());
        postRepository.save(post);
        return "redirect:/";
    }
    @PostMapping("/deletepost/{id}")
    public String deletepost(@PathVariable(value = "id") long id){
        Post post = (Post) postRepository.findById(id).orElseThrow();
        postRepository.delete(post);
        return "redirect:/";
    }
    @RequestMapping("/searchpost")
    public String searchpost(Model model,String keyword){
        if (keyword != null){
            List<Post> searhcposts = postRepository.findByTitleContains(keyword);
            model.addAttribute("posts",searhcposts);
            model.addAttribute("keyword",keyword);
        } else {
            List<Post> posts = postRepository.findAll();
            model.addAttribute("posts",posts);
            model.addAttribute("keyword",keyword);
        }
        return "searchpost";
    }
}
