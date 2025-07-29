package br.com.wkallil.repositories;

import br.com.wkallil.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
}
