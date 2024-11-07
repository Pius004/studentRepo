package com.example.sorting.controller;

import com.example.sorting.entity.UserDto;
import com.example.sorting.repository.UserRepository;
import com.example.sorting.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {


    private final UserService userService;
    private UserRepository userRepository;

    public  UserController(UserService userService, UserRepository userRepository){
        this.userService = userService;
        this.userRepository = userRepository;
    }


    @GetMapping("/home")
    public String home() {
        return "index";  // This assumes your index.html is in the static folder
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model){
        model.addAttribute("registerRequest", new UserDto());
        return "register_page";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model){
        model.addAttribute("loginRequest", new UserDto());
        return "login_page";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute UserDto userDto ){
        System.out.println("register request: " + userDto);
        UserDto registeredUser = userService.registerUser(
                userDto.getFirstName(), userDto.getLastName(), userDto.getMatric(), userDto.getModeOfEntry(),
                userDto.getYearOfEntry(), userDto.getEmail(),userDto.getPassword(), userDto.getDob(),
                userDto.getFaculty(), userDto.getDepartment(), userDto.getGender(), userDto.getLevel(),
                userDto.getStateOfOrigin(), userDto.getPhoneNumber(),  userDto.getAge(),userDto.getParentPhoneNumber(),
                userDto.getHomeAddress(), userDto.getMedicalId(), userDto.getLocalGovernment()
        );


        return  registeredUser == null ? "error_page" : "redirect:/login";

    }

    @PostMapping("/login")
    public String login(@ModelAttribute UserDto userDto, Model model){
        System.out.println("login request: " + userDto);
        UserDto authenticated = userService.authenticate(  userDto.getMatric(),userDto.getPassword() );
        if (authenticated != null){
            model.addAttribute("userName", authenticated.getFirstName());
            return "personal_page";
        } else{
            return "error_page";
        }

    }





        // Static credentials for admin
        private final String ADMIN_USERNAME = "admin";
        private final String ADMIN_PASSWORD = "password123";

        // Get the login page
        @GetMapping("/admin/login")
        public String getAdminLoginPage(Model model) {
            // Add empty admin request to the model to bind form data
            model.addAttribute("adminRequest", new AdminRequest());
            return "admin_login";
        }

        // Post the login data
        @PostMapping("/admin/login")
        public String login(@ModelAttribute AdminRequest adminRequest, Model model) {
            // Check if the provided username and password match the static ones
            if (ADMIN_USERNAME.equals(adminRequest.getUsername()) && ADMIN_PASSWORD.equals(adminRequest.getPassword())) {
                // Correct credentials, navigate to the dashboard page
                return "redirect:/students";
            } else {
                // Incorrect credentials, show an error message
                model.addAttribute("errorMessage", "Invalid username or password.");
                return "admin_login";
            }
        }

        // AdminRequest is a simple class to hold username and password from the form
        public static class AdminRequest {
            private String username;
            private String password;

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }
        }

    @GetMapping("/students/filter")
    public String filterStudents(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String faculty,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String mode,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer level,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            Model model) {

        // Convert empty strings to null
        department = (department == null || department.isEmpty()) ? null : department;
        faculty = (faculty == null || faculty.isEmpty()) ? null : faculty;
        gender = (gender == null || gender.isEmpty()) ? null : gender;
        mode = (mode == null || mode.isEmpty()) ? null : mode;
        state = (state == null || state.isEmpty()) ? null : state;

        // log the parameters for debugging
        System.out.println("Filter params: department=" + department + ", faculty=" + faculty + ", gender=" + gender
                + ", mode=" + mode + ", year=" + year
                + ", level=" + level + ", state=" + state
                + ", minAge=" + minAge + ", maxAge=" + maxAge);

        List<UserDto> filteredStudents = userService.filterUserDto(department, faculty, gender,
                mode, year,
                level, state, minAge, maxAge);
        model.addAttribute("students", filteredStudents);
        return "students"; // Thymeleaf template 'students.html'


    }

    @GetMapping("/students")
    public String getAllStudents(Model model) {
        List<UserDto> students = userService.getAllUserDto(); // This should return a list, not a single object
        System.out.println("Fetched students: " + students);
        model.addAttribute("students", students); // Add the list to the model
        return "students"; // Return Thymeleaf template
    }





}