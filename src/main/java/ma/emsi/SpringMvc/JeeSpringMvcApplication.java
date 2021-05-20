package ma.emsi.SpringMvc;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ma.emsi.SpringMvc.dao.PatientRepository;
import ma.emsi.SpringMvc.entities.Patient;

@SpringBootApplication
public class JeeSpringMvcApplication implements CommandLineRunner {
	@Autowired
	private PatientRepository patientRepository;

	public static void main(String[] args) {
		SpringApplication.run(JeeSpringMvcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		//patientRepository.save(new Patient(null, "Ismail", new Date(), false, 4));
		//patientRepository.save(new Patient(null, "EL MOUSAID", new Date(), false, 5));
		//patientRepository.save(new Patient(null, "Samir", new Date(), false, 7));
		
		patientRepository.findAll().forEach(p->{
			System.out.println(p.getName());
		});
		
	}

}
