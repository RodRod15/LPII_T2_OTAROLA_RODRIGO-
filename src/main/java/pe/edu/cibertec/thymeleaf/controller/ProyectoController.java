package pe.edu.cibertec.thymeleaf.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pe.edu.cibertec.web.model.User;
import pe.edu.cibertec.web.repository.IPersonRepository;
import pe.edu.cibertec.web.repository.IRoleRepository;
import pe.edu.cibertec.web.repository.IUserRepository;
import pe.edu.cibertec.web.service.UserService;

@Controller
public class ProyectoController {

	@Autowired
	private IRoleRepository repos;

	@Autowired
	private IPersonRepository reposPerson;

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private UserService userService;

	@GetMapping("/greeting")
	public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name,
			Model model) {

		model.addAttribute("name", name);
		return "greeting";

	}
	
	@GetMapping("/greeting2")
	public String greeting2(@RequestParam(name = "user", required = false, defaultValue = "World") String name,
			Model model) {

		model.addAttribute("name", name);
		return "greeting2";

	}

	@GetMapping("/listar")
	public String listRole(Model model) {
		try {
			model.addAttribute("ltsRole", repos.findAll());
			model.addAttribute("ltsPerson", reposPerson.findAll());
			model.addAttribute("ltsUser", userRepository.findAll());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "listado";
	}

	@GetMapping("/login")
	public String loginView(Model model) {
		System.out.println("Mostrando login");
		User uNew = new User();
		uNew.setEmail("mail@default.com");
		model.addAttribute("userLogin", uNew);
		return "login";
	}
	
	
	@GetMapping("/login2")
	public String loginView2(Model model) {
		System.out.println("Mostrando login");
		User uNameNew = new User();
		uNameNew.setName("Ingresar nombre de usuario");
		model.addAttribute("userLogin2", uNameNew);
		return "login2";
	}
	

	@PostMapping("/login")
	public String login(@ModelAttribute User user, Model model) {
		System.out.println("Validando login");
		String redirect = "login";
		User u = userService.validateUserByEmailAndPassword(user.getEmail(), user.getPassword());
		if (u != null) {
			u.setLastlogin(new Date());
			System.out.println("Actualizando usuario");
			User updUser = userService.updateUserLogin(u);
			model.addAttribute("userLogin", updUser);
			redirect = "greeting";
		} else {
			model.addAttribute("errors", "Correo o clave incorrectos");
			model.addAttribute("userLogin", new User());
		}

		return redirect;
	}
	
	@PostMapping("/login2")
	public String login2(@ModelAttribute User user, Model model) {
		System.out.println("Validando login");
		String redirect = "login2";
		User u = userService.validateUserByNameAndPassword(user.getName(), user.getPassword());
		if (u != null) {
			u.setLastlogin(new Date());
			System.out.println("Actualizando usuario");
			User updUser = userService.updateUserLogin(u);
			model.addAttribute("userLogin2", updUser);
			redirect = "greeting2";
		} else {
			model.addAttribute("errors", "Usuario o clave incorrectos");
			model.addAttribute("userLogin2", new User());
		}

		return redirect;
	}
	
	
	@PostMapping("/update")
	public String update(@ModelAttribute User user, Model model) {
		String redirect = "actualizar";
		User u = userService.validateUserByName(user.getName());
		if (u != null) {
			u.setName(u.getName());
			User updUser = userService.updateUserName(u);
			model.addAttribute("userNameUpd", updUser);
			redirect = "listar";
		} else {
			model.addAttribute("errors", "Usuario no existe");
			model.addAttribute("userNameUpd", new User());
		}

		return redirect;
	}

	

}
