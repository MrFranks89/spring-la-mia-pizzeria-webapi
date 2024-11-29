package it.lessons.pizzeria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.lessons.pizzeria.model.Offerta;

public interface OfferteRepository extends JpaRepository<Offerta, Long>{


}
