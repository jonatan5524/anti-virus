package antiVirus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import antiVirus.exceptions.AntiVirusUserException;

@Controller
public class AntiVirusUIController {

	@RequestMapping("/") 
	public String index() {
		return "index"; 
	}
}