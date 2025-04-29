package uz.pdp.studycenter_app.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uz.pdp.studycenter_app.entity.Groups;
import uz.pdp.studycenter_app.repo.GroupsRepository;

import java.util.Optional;

@Controller
@RequestMapping("/group")
public class GroupController {

    private final GroupsRepository groupsRepository;


    public GroupController(GroupsRepository groupsRepository) {
        this.groupsRepository = groupsRepository;
    }

    @GetMapping
    public String getGroups(Model model) {
        model.addAttribute("groups",groupsRepository.findAll());
        return "groups";
    }

    @PostMapping
    public String addGroup(@RequestParam String name,Model model) {
        Optional<Groups> optionalGroups = groupsRepository.findByName(name);
        if (optionalGroups.isPresent()) {
            model.addAttribute("errorMessage","Group already exists");
            model.addAttribute("groups", groupsRepository.findAll()); // Groups ro'yxatini ham qaytaring
            return "addGroup"; // Sahifani qaytarish, modalni ochiq tutish
        }else {
            Groups group = Groups.builder().name(name).build();
            groupsRepository.save(group);
        }
        return "redirect:/group";
    }

    @PostMapping("/update/{groupId}")
    public String updateGroup(@PathVariable Integer groupId, @RequestParam String name) {
        Groups groups = groupsRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
        groups.setName(name);
        groupsRepository.save(groups);
        return "redirect:/group";
    }

    @GetMapping("/delete/{groupId}")
    public String deleteGroup(@PathVariable Integer groupId) {
        groupsRepository.deleteById(groupId);
        return "redirect:/group";
    }
}
