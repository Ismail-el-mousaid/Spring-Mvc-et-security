package ma.emsi.SpringMvc.security;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {
	@GetMapping("/noAuthorized")
	public String error() {
		return "noAuthorizer";
	}
	
	
	// Personaliser Page Login
	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request) throws ServletException {
		request.logout(); // fermer la session
		return "login";
	}

	
}
