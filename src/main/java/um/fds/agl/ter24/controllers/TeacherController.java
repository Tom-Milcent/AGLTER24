package um.fds.agl.ter24.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import um.fds.agl.ter24.services.TERManagerService;
import um.fds.agl.ter24.entities.Teacher;
import um.fds.agl.ter24.forms.TeacherForm;
import um.fds.agl.ter24.services.TeacherService;

@Controller
public class TeacherController {

    @Autowired
    private TeacherService teacherService;
    @Autowired
    private TERManagerService terManagerService;

    @GetMapping("/listTeachers")
    public Iterable<Teacher> getTeachers(Model model) {
        Iterable<Teacher> teachers=teacherService.getTeachers();
        model.addAttribute("teachers", teachers);
        return teachers;
    }
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping(value = { "/addTeacher" })
    public String showAddTeacherPage(Model model) {

        TeacherForm teacherForm = new TeacherForm();
        model.addAttribute("teacherForm", teacherForm);

        return "addTeacher";
    }

    @PostMapping(value = { "/addTeacher"})
    public String addTeacher(Model model, @ModelAttribute("TeacherForm") TeacherForm teacherForm) {
        Teacher t;
        if(teacherService.findById(teacherForm.getId()).isPresent()){
            // teacher already existing : update
            t = teacherService.findById(teacherForm.getId()).get();
            t.setFirstName(teacherForm.getFirstName());
            t.setLastName(teacherForm.getLastName());
        } else {
            // teacher not existing : create
            t=new Teacher(teacherForm.getFirstName(), teacherForm.getLastName(), terManagerService.getTERManager());
        }
        teacherService.saveTeacher(t);
        return "redirect:/listTeachers";

    }

    @GetMapping(value = {"/showTeacherUpdateForm/{id}"})
    public String showTeacherUpdateForm(Model model, @PathVariable(value = "id") long id){

        TeacherForm teacherForm = new TeacherForm(id, teacherService.findById(id).get().getFirstName(), teacherService.findById(id).get().getLastName());
        model.addAttribute("teacherForm", teacherForm);
        return "updateTeacher";
    }

    @GetMapping(value = {"/deleteTeacher/{id}"})
    public String deleteTeacher(Model model, @PathVariable(value = "id") long id){
        teacherService.deleteTeacher(id);
        return "redirect:/listTeachers";
    }

}
