package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String showRegistrationPage(Model model){
        model.addAttribute("user", new User());
        return "registration";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String processRegistrationPage(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        model.addAttribute("user", user);
        if (result.hasErrors()) {
            return "registration";
        }

        else {
            userService.saveUser(user);
            model.addAttribute("message", "User Account Successfully Created");
        }
        return "index";
    }

    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/secure")
    public String secure(HttpServletRequest request, Authentication authentication, Principal principal){
        Boolean isAdmin = request.isUserInRole("ADMIN");
        Boolean isUser = request.isUserInRole("USER");
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = principal.getName();
        return "secure";
    }

    private User getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentusername = authentication.getName();
        User user = userRepository.findByUsername(currentusername);
        return user;
    }

    @RequestMapping("/addtask")
    public String addTask(Model model){
        String username = getUser().getUsername();
        Task newtask = new Task();
        newtask.setUsername(username);
        model.addAttribute("task", newtask);
        return "taskform";
    }

    @PostMapping("/process")
    public String processForm(@Valid Task task, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "taskform";
        }
        taskRepository.save(task);
        model.addAttribute("tasks", taskRepository.findByUsername(getUser().getUsername()));
        return "tasklist";
    }

    @RequestMapping("/tasklist")
    public String listTasks(Model model){
        model.addAttribute("tasks", taskRepository.findByUsername(getUser().getUsername()));
        return "tasklist";
    }

    @RequestMapping("/update/{id}")
    public String updateTasks(@PathVariable("id") long id, Model model){
        model.addAttribute("task", taskRepository.findById(id).get());
        return "taskform";
    }
}
