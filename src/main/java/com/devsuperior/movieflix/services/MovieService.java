package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.dto.MovieDetailsDTO;
import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.repositories.GenreRepository;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovieService {

    @Autowired
    MovieRepository repository;

    @Autowired
    GenreRepository genreRepository;

    @Autowired
    GenreService genreService;

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

        Genre genre = genreRepository.findById(entity.getId()).orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        dto.setGenre(genreService.copyEntityToDto(genre));

        return dto;
    }
}
