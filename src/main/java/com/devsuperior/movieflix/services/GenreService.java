package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.dto.GenreDTO;
import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.repositories.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GenreService {

    @Autowired
    GenreRepository repository;

    @Transactional(readOnly = true)
    public List<GenreDTO> findAll() {
        List<Genre> list = repository.findAll();
        return list.stream().map(this::copyEntityToDto).toList();
    }

    private GenreDTO copyEntityToDto(Genre entity) {
        GenreDTO dto = new GenreDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }
}
