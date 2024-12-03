package it.lessons.restcontroller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.lessons.pizzeria.model.Pizza;
import it.lessons.pizzeria.repository.PizzaRepository;
import jakarta.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/api/pizze")
public class PizzeRestController {

    private static final Logger logger = LoggerFactory.getLogger(PizzeRestController.class);

    @Autowired
    private PizzaRepository pizzaRepo;
    
    public PizzeRestController() {
        System.out.println("PizzeRestController caricato!");
    }

    // Endpoint per ottenere tutte le pizze o filtrare per keyword
    @GetMapping("/")
    public ResponseEntity<List<Pizza>> index(@RequestParam(name = "keyword", required = false) String keyword) {
        try {
            if (keyword != null && !keyword.isBlank()) {
                List<Pizza> pizze = pizzaRepo.findByNomeContaining(keyword);
                return new ResponseEntity<>(pizze, HttpStatus.OK);
            } else {
                List<Pizza> pizze = pizzaRepo.findAll();
                return ResponseEntity.ok(pizze);
            }
        } catch (Exception e) {
            logger.error("Errore durante il recupero delle pizze", e);
            return ResponseEntity.badRequest().build();
        }
    }

    // Endpoint per aggiornare una pizza
    @PutMapping("/{id}")
    public ResponseEntity<Pizza> update(@PathVariable Long id, @RequestBody Pizza pizza) {
        try {
            Optional<Pizza> byId = pizzaRepo.findById(id);
            if (byId.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Pizza dbPizza = byId.get();
            dbPizza.setDescrizione(pizza.getDescrizione());
            pizzaRepo.save(dbPizza);

            return ResponseEntity.ok(dbPizza);
        } catch (Exception e) {
            logger.error("Errore durante l'aggiornamento della pizza", e);
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint per eliminare una pizza
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePizza(@PathVariable Long id) {
        try {
            if (!pizzaRepo.existsById(id)) {
                return ResponseEntity.notFound().build();
            }

            pizzaRepo.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Errore durante la cancellazione della pizza", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    
    // Endpoint per creare una pizza
    @PostMapping
    public Pizza create(@Valid @RequestBody Pizza pizza) {
    	return pizzaRepo.save(pizza);
    }
}
