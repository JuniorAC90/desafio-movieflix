package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.dto.MovieDetailsDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovieService {

    @Autowired
    MovieRepository repository;

    @Transactional(readOnly = true)
    public MovieDetailsDTO findById(Long id) {
        Movie entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return copyEntityToDto(entity);
    }

    private MovieDetailsDTO copyEntityToDto(Movie entity) {
        MovieDetailsDTO dto = new MovieDetailsDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setSubTitle(entity.getSubTitle());
        dto.setYear(entity.getYear());
        dto.setImgUrl(entity.getImgUrl());
        dto.setSynopsis(entity.getSynopsis());
        dto.setGenre(dto.getGenre());
        return dto;
    }
}
