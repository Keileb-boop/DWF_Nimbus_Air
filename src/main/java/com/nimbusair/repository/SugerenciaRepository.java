package com.nimbusair.repository;

import com.nimbusair.entity.Sugerencia;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SugerenciaRepository extends JpaRepository<Sugerencia, Long> {
    List<Sugerencia> findByRevisadaFalse();

    List<Sugerencia> findByEmailOrderByFechaCreacionDesc(String email);


    List<Sugerencia> findAllByOrderByFechaCreacionDesc();

    long countByRevisadaFalse();
}

