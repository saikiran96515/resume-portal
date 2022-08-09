package com.saikiran.resumeportal;

import com.saikiran.resumeportal.models.Education;
import com.saikiran.resumeportal.models.Job;
import com.saikiran.resumeportal.models.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    UserProfileRepository userProfileRepository;
    @GetMapping("/")
    public String home (){

        return "index";
    }

    @GetMapping("/edit")
    public String edit (Model model, Principal principal, @RequestParam(required = false) String add){
        String userId = principal.getName();
        model.addAttribute("userId",userId);
        Optional<UserProfile> userProfileOptional=userProfileRepository.findByUserName(userId);
        userProfileOptional.orElseThrow(() -> new RuntimeException("Not found: " + userId));
        UserProfile userProfile = userProfileOptional.get();
        if("job".equals(add)){
            userProfile.getJobs().add(new Job());
        }else if("education".equals(add))
        {
            userProfile.getEducations().add(new Education());
        }else if("skill".equals(add))
        {
            userProfile.getSkills().add("");
        }
        model.addAttribute("userProfile",userProfile);
        return "profile-edit";
    }

    @PostMapping("/edit")
    public String postEdit(Model model, Principal principal, @ModelAttribute UserProfile userProfile){
        String userName = principal.getName();
        model.addAttribute("userId",userName);
        Optional<UserProfile> userProfileOptional=userProfileRepository.findByUserName(userName);
        userProfileOptional.orElseThrow(() -> new RuntimeException("Not found: " + userName));
        UserProfile savedUserProfile = userProfileOptional.get();
        userProfile.setId(savedUserProfile.getId());
        userProfile.setUserName(userName);
        userProfileRepository.save(userProfile);
        return "redirect:/view/" + userName;
    }

    @GetMapping("/delete")
    public String delete (Model model, Principal principal, @RequestParam String type,@RequestParam int index) {
        String userName = principal.getName();
        model.addAttribute("userId",userName);
        Optional<UserProfile> userProfileOptional=userProfileRepository.findByUserName(userName);
        userProfileOptional.orElseThrow(() -> new RuntimeException("Not found: " + userName));
        UserProfile userProfile = userProfileOptional.get();
        if("job".equals(type)){
            userProfile.getJobs().remove(index);
        }else if("education".equals(type))
        {
            userProfile.getEducations().remove(index);
        }else if("skill".equals(type))
        {
            userProfile.getSkills().remove(index);
        }
        userProfileRepository.save(userProfile);
        return "redirect:/edit/";
    }

    @GetMapping("/view/{userId}")
    public String view (Principal principal ,@PathVariable String userId, Model model){
        System.out.println(principal);
        if(principal !=null && principal.getName()!="")
        {
            boolean currentUsersProfile = principal.getName().equals(userId);
            model.addAttribute("currentUsersProfile",currentUsersProfile);
            System.out.println(currentUsersProfile);
        }
        String userName= principal.getName();
        Optional<UserProfile> userProfileOptional=userProfileRepository.findByUserName(userId);
        userProfileOptional.orElseThrow(() -> new RuntimeException("Not found: " + userId));
        UserProfile userprofile = userProfileOptional.get();
        model.addAttribute("userId",userId);
        model.addAttribute(userprofile);
        System.out.println(userprofile.getJobs());
        return "profile-templates/"+userprofile.getTheme()+"/index";
    }

}
