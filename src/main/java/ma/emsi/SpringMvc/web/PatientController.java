package ma.emsi.SpringMvc.web;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ma.emsi.SpringMvc.dao.PatientRepository;
import ma.emsi.SpringMvc.entities.Patient;

@Controller
public class PatientController {
	@Autowired
	private PatientRepository patientRepository;
	
	@GetMapping(path = "/index")
	public String index() {
		return "index";
	}
	
	
	// pagination
	@GetMapping(path = "/patients")
	public String list(Model model,
			@RequestParam(name = "page", defaultValue = "0")int page,
			@RequestParam(name = "size", defaultValue = "5")int size,
			@RequestParam(name = "keyword", defaultValue = "")String mc) {
		Page<Patient> pagePatients = patientRepository.findByNameContains(mc, PageRequest.of(page, size));
		model.addAttribute("patients", pagePatients.getContent());
		model.addAttribute("pages", new int[pagePatients.getTotalPages()]);   // savoir nombre des pages et stoket dans l'index du tableau
		model.addAttribute("currentPage", page); // stocker la valeur du page courant dans currentPage
		model.addAttribute("keyword", mc);
		model.addAttribute("size", size);
		return "patients";
	}

	@GetMapping(path = "/deletePatient")
	public String delete(Long id, String keyword, int page, int size) {
		patientRepository.deleteById(id);
		return "redirect:/patients?page="+page+"&size="+size+"&keyword="+keyword;
	}
	
	 @GetMapping(path = "/formPatient")
	 public String formPatient(Model model) {
		 model.addAttribute("patient", new Patient());
		 return "formPatient";
	 }
	 
	 @PostMapping("/savePatient")
	 public String savePatient(Model model, @Valid Patient patient, BindingResult bindingResult) {
		if(bindingResult.hasErrors())
			return "formPatient";
		 patientRepository.save(patient);
		 model.addAttribute("patient", patient);
		 return "confirmation"; //retourner vers la vue	 
	 }
	 
	 
	 @GetMapping(path = "/editPatient")
	 public String editPatient(Model model, Long id) {
		Patient p = patientRepository.findById(id).get();
		model.addAttribute("patient", p);
		return "formPatient"; //returner vers la vue	 
	}
	 
	 
	
	 // Lister les patients aux format json
	 @GetMapping("/listPatients")
	 @ResponseBody
	 public List<Patient> listJson(){
		 return patientRepository.findAll();
	 }
	 
	 @GetMapping("/patients/{id}")
	 @ResponseBody
	 public Patient getOne(@PathVariable Long id){
		 return patientRepository.findById(id).get();
	 }
}
