package com.example.demoproject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demoproject.model.Student;
import com.example.demoproject.service.StudentService;

@Controller
@ComponentScan(basePackages = "com.example.demoproject")
public class StudentController {

	@Autowired
	private StudentService studService;
	
	// display list of Students
	@GetMapping("/")
	public String viewHomePage(Model model) {
		return findPaginated(1, "name", "asc", model);		
	}
	
	@GetMapping("/new_student")
	public String new_student(Model model) {
		// create model attribute to bind form data
		Student student = new Student();
		model.addAttribute("student", student);
		return "new_student";
	}
	
	@PostMapping("/saveStudent")
	public String saveStudent(@ModelAttribute("student") Student student) {
		// save Student to database
		studService.saveStudent(student);
		return "redirect:/";
	}
	
	@GetMapping("/showFormForUpdate/{id}")
	public String showFormForUpdate(@PathVariable ( value = "id") int id, Model model) {
		
		// get Student from the service
		Student Student = studService.getStudentByID(id);
		
		// set Student as a model attribute to pre-populate the form
		model.addAttribute("student", Student);
		return "update_student";
	}
	
	@GetMapping("/deleteStudent/{id}")
	public String deleteStudent(@PathVariable (value = "id") int id) {
		
		// call delete Student method 
		this.studService.deleteStudent(id);
		return "redirect:/";
	}
	
	
	@GetMapping("/page/{pageNo}")
	public String findPaginated(@PathVariable (value = "pageNo") int pageNo, 
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			Model model) {
		int pageSize = 5;
		
		Page<Student> page = studService.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<Student> ls = page.getContent();
		
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		
		model.addAttribute("ls", ls);
		return "index";
	}
}

