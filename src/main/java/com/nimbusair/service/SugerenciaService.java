package com.nimbusair.service;

import com.nimbusair.entity.Sugerencia;
import com.nimbusair.entity.TipoSugerencia;
import com.nimbusair.repository.SugerenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SugerenciaService {

    @Autowired
    private SugerenciaRepository sugerenciaRepository;

    public Sugerencia crearSugerencia(Sugerencia sugerencia) {
        return sugerenciaRepository.save(sugerencia);
    }

    public List<Sugerencia> obtenerSugerenciasNoRevisadas() {
        return sugerenciaRepository.findByRevisadaFalse();
    }

    public List<Sugerencia> obtenerSugerenciasPorEmail(String email) {
        return sugerenciaRepository.findByEmailOrderByFechaCreacionDesc(email);
    }

    public Sugerencia marcarComoRevisada(Long id) {
        Sugerencia sugerencia = sugerenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sugerencia no encontrada"));
        sugerencia.setRevisada(true);
        return sugerenciaRepository.save(sugerencia);
    }

    public List<Sugerencia> obtenerTodasLasSugerencias() {
        return sugerenciaRepository.findAll();
    }

    public long contarSugerenciasNoRevisadas() {
        return sugerenciaRepository.countByRevisadaFalse();
    }

    public List<Sugerencia> obtenerTodasLasSugerenciasAdmin() {
        return sugerenciaRepository.findAllByOrderByFechaCreacionDesc();
    }
}