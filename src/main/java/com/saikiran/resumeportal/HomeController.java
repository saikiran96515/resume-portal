package com.saikiran.resumeportal;

import com.saikiran.resumeportal.models.Job;
import com.saikiran.resumeportal.models.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
        Optional<UserProfile> profileOptional = userProfileRepository.findByUserName("einstein");
        profileOptional.orElseThrow(() -> new RuntimeException("Not found: "));
        UserProfile profile1 = profileOptional.get();
        Job job1 = new Job();
        job1.setCompany("Company1");
        job1.setDesignation("Designation");
        job1.setStartDate(LocalDate.of(2020,1,1));
        job1.setEndDate(LocalDate.of(2020,3,3));
        job1.setId(1);
        Job job2 = new Job();
        job2.setCompany("Company2");
        job2.setDesignation("Designation");
        job2.setStartDate(LocalDate.of(2019,1,1));
        job2.setEndDate(LocalDate.of(2021,3,3));
        job2.setId(2);
        profile1.getJobs().clear();
        profile1.getJobs().add(job1);
        profile1.getJobs().add(job2);
        userProfileRepository.save(profile1);
        return "profile";
    }

    @GetMapping("/edit")
    public String edit (){
        return "edit page";
    }

    @GetMapping("/view/{userId}")
    public String view (@PathVariable String userId, Model model){
        Optional<UserProfile> userProfileOptional=userProfileRepository.findByUserName(userId);
        userProfileOptional.orElseThrow(() -> new RuntimeException("Not found: " + userId));
        UserProfile userprofile = userProfileOptional.get();
        model.addAttribute("userId",userId);
        model.addAttribute(userprofile);
        System.out.println(userprofile.getJobs());
        return "profile-templates/"+userprofile.getTheme()+"/index";
    }

}
