package ma.emsi.SpringMvc.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration  // cet classe a priori traiter au demarage de l'app

// Personnaliser la config du spring security
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
	private DataSource datasource;  // injecter le datasource 'BD' courant pour appeler les users qui se trouve dans bd pour authentification
	
	//indiquer a spring security les utilisateurs qui ont le droit de se connectrer avec ces roles
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
		
	//	auth.inMemoryAuthentication().withUser("user1").password("{noop}1234").roles("USER"); // stocker user qui a le droit de connecter au memoire
	//	auth.inMemoryAuthentication().withUser("user2").password("{noop}1234").roles("USER");   // {noop} : pour ne pas crypter mot de passe
	//	auth.inMemoryAuthentication().withUser("admin").password("{noop}1234").roles("ADMIN", "USER"); 
		
		// Appeler la methode
		PasswordEncoder passwordEncoder = passwordEncoder();
		System.out.println("************************");
		System.out.println(passwordEncoder.encode("1234"));
		System.out.println("************************");
		/*
		auth.inMemoryAuthentication().withUser("user1").password(passwordEncoder.encode("1234")).roles("USER");
		auth.inMemoryAuthentication().withUser("user2").password(passwordEncoder.encode("1234")).roles("USER");
		auth.inMemoryAuthentication().withUser("admin").password(passwordEncoder.encode("1234")).roles("ADMIN", "USER");
			*/
		// Authentification en utilisant BD
		auth.jdbcAuthentication().dataSource(datasource).usersByUsernameQuery("SELECT username as principal, password as credentials, active from users where username = ?") // pour recuperer les utilisateures
		.authoritiesByUsernameQuery("select username as principal, role as role from users_roles where username= ?")   // recuperer les roles
		.passwordEncoder(passwordEncoder).rolePrefix("ROLE_");
	}
	
	// Les 2 mzthodes qu'il faut redefinir pour personaliser config
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		http.formLogin().loginPage("/login");               //utiliser le formulaire d'authentification personnaliser
	//	http.httpBasic();              // utilis formulaire d'authentification basic javascripte (fenetre) (ce form vient de navigateur)
		
		http.authorizeRequests().antMatchers("/save**/**", "/delete**/**", "/form**/**").hasRole("ADMIN");  // tous les requetes http dont le path qui commence avec save et delete ont le role ADMIN
		//http.authorizeRequests().anyRequest().authenticated();         // Tous les requetes http necessite une authentfication et ont le droit de faire tous
		
		http.authorizeRequests().antMatchers("/patients**/**").hasRole("USER");
		//http.authorizeRequests().antMatchers("/user**/**", "/connect**/**").permitAll();  // autoriser a tous l'access a 'user' sans authentifiation
		
		http.csrf(); // activer le mecanisme csrf contre les attaques csrf(il est activé par défaut) : consite a donner a l'utilateur un token par serveur SPRINGau moment de l'ouverture de session , ce qui permet a idzntifier cet sesion et verifier est ce qu'il est juste
	//	http.csrf().disable();   desactiver ce mecanisme
		
		//personaliser le message d'erreur et le diriger vers une page
		http.exceptionHandling().accessDeniedPage("/noAuthorized");
	}
	
	//Permet encoder mot de passe / Par defaut il est encoder par md5 mais il est devienne vulnerable
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	

}
