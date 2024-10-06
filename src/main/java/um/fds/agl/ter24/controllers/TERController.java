package um.fds.agl.ter24.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.context.support.BeanDefinitionDsl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import um.fds.agl.ter24.entities.TER;
import um.fds.agl.ter24.entities.Teacher;
import um.fds.agl.ter24.forms.TERForm;
import um.fds.agl.ter24.repositories.TeacherRepository;
import um.fds.agl.ter24.services.TERService;
import um.fds.agl.ter24.services.TeacherService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class TERController {

    @Autowired
    private TERService terService;

    @Autowired
    private TeacherService teacherService;

    public TERController(TERService terService, TeacherService teacherService) {
        this.terService = terService;
        this.teacherService = teacherService;
    }

    @GetMapping("/listTERs")
    public Iterable<TER> getTERs(Model model) {
        model.addAttribute("TER", terService.getTERs());
        return terService.getTERs();
    }
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_TEACHER')")
    @GetMapping(value = { "/addTER" })
    public String showAddTERPage(Model model, Authentication authentication) {
        TERForm TERForm = new TERForm();
        model.addAttribute("TERForm", TERForm);
        model.addAttribute("teachers", getTeachers(authentication));

        return "addTER";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER') or (@teacherService.getTeacher(#TERForm?.teacher).get()?.lastName == authentication.name)")
    @PostMapping(value = { "/addTER"})
    public String addTER(Model model, @ModelAttribute("TERForm") TERForm TERForm) {
        TER TER;
        Teacher teacher = teacherService.getTeacher(TERForm.getTeacher()).get();
        if(terService.findById(TERForm.getId()).isPresent()){
            TER = terService.findById(TERForm.getId()).get();
            TER.setSubject(TERForm.getSubject());
            TER.setTeacher(teacher);
        } else {
            TER = new TER(teacher, TERForm.getSubject());
        }
        terService.saveTER(TER);
        return "redirect:/listTERs";

    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER') or (@teacherService.getTeacher(@TERService.findById(#id).get()?.teacher.id).get()?.lastName == authentication.name)")
    @GetMapping(value = {"/showTERUpdateForm/{id}"})
    public String showTERUpdateForm(Model model, @PathVariable(value = "id") long id, Authentication authentication){
        TERForm TERForm = new TERForm(id, terService.findById(id).get().getTeacher().getId(), terService.findById(id).get().getSubject());
        model.addAttribute("TERForm", TERForm);
        model.addAttribute("teachers", getTeachers(authentication));
        return "updateTER";
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER') or (@teacherService.getTeacher(@TERService.findById(#id).get()?.teacher.id).get()?.lastName == authentication.name)")
    @GetMapping(value = {"/deleteTER/{id}"})
    public String deleteTER(Model model, @PathVariable(value = "id") long id){
        terService.deleteTER(id);
        return "redirect:/listTERs";
    }

    private List<Teacher> getTeachers(Authentication authentication) {
        Iterable<Teacher> teacherIterable = teacherService.getTeachers();
        List<Teacher> teachers = new ArrayList<>();
        if(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER"))) teacherIterable.forEach(teachers::add);
        else {
            teacherIterable.forEach(teacher -> {
                if(Objects.equals(teacher.getLastName(), authentication.getName()))
                    teachers.add(teacher);
            });
        }
        return teachers;
    }
}
