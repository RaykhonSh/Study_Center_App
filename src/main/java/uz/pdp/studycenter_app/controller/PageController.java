package uz.pdp.studycenter_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.pdp.studycenter_app.entity.Groups;
import uz.pdp.studycenter_app.repo.GroupsRepository;

@Controller
@RequestMapping
public class PageController {
    private final GroupsRepository groupsRepository;

    public PageController(GroupsRepository groupsRepository) {
        this.groupsRepository = groupsRepository;
    }

    @GetMapping("/updateGroup/{groupId}")
    public String addGroup(@PathVariable Integer groupId, Model model) {
        model.addAttribute("groupId", groupId);
        Groups groups = groupsRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
        model.addAttribute("group", groups);
        return "addGroup";
    }

    @GetMapping("/addGroup")
    public String addGroup(){
        return "addGroup";
    }

}
