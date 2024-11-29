package it.lessons.pizzeria.controllers;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.lessons.pizzeria.model.Offerta;
import it.lessons.pizzeria.model.Pizza;

import it.lessons.pizzeria.repository.OfferteRepository;
import it.lessons.pizzeria.repository.PizzaRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/offerte")
public class OfferteController {

	@Autowired
	private OfferteRepository offerteRepository;

	@Autowired
	private PizzaRepository pizzaRepository;

	@GetMapping("/crea")
	public String creaOfferta(@RequestParam(required = false) Long pizzaId, Model model) {
		Offerta nuovaOfferta = new Offerta();
		if (pizzaId != null) {
			Pizza pizza = pizzaRepository.findById(pizzaId)
					.orElseThrow(() -> new IllegalArgumentException("Pizza non trovata"));
			nuovaOfferta.setPizza(pizza);
		}
		model.addAttribute("offerta", nuovaOfferta);
		model.addAttribute("pizze", pizzaRepository.findAll());
		return "offerte/create";
	}

	@PostMapping("/crea")
	public String creaOfferta(@Valid @ModelAttribute("offerta") Offerta offerta, BindingResult bindingResult,
			@RequestParam(required = false) Long pizzaId, @RequestParam(required = false) Model model) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("pizze", pizzaRepository.findAll());
			return "offerte/create";
		}

		if (pizzaId != null) {
			Pizza pizza = pizzaRepository.findById(pizzaId)
					.orElseThrow(() -> new IllegalArgumentException("Pizza non trovata"));
			offerta.setPizza(pizza);
		}

		offerteRepository.save(offerta);
		return "redirect:/pizze/show/" + offerta.getPizza().getId();
	}

	@GetMapping("/edit/{id}")
	public String editOfferta(@PathVariable Long id, Model model) {
		Offerta offerta = offerteRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Offerta non trovata"));

		model.addAttribute("offerta", offerta);
		model.addAttribute("pizze", pizzaRepository.findAll());
		return "offerte/edit";
	}

	@PostMapping("/edit/{id}")
	public String aggiornaOfferta(@PathVariable Long id, @Valid @ModelAttribute("offerta") Offerta offerta,
			BindingResult bindingResult, @RequestParam(required = false) Long pizzaId,
			@RequestParam(required = false) Model model) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("pizze", pizzaRepository.findAll());
			return "offerte/edit";
		}

		if (pizzaId != null) {
			Pizza pizza = pizzaRepository.findById(pizzaId)
					.orElseThrow(() -> new IllegalArgumentException("Pizza non trovata"));
			offerta.setPizza(pizza);
		}

		offerteRepository.save(offerta);
		return "redirect:/pizze/show/" + offerta.getPizza().getId();
	}

	@PostMapping("/delete/{id}")
	public String deleteOfferta(@PathVariable Long id) {
		Offerta offerta = offerteRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Offerta non trovata"));
		offerteRepository.delete(offerta);
		return "redirect:/pizze/show/" + offerta.getPizza().getId();
	}
}
