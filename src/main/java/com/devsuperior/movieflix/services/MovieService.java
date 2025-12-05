package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.dto.MovieDetailsDTO;
import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.repositories.GenreRepository;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        return copyEntityToMovieDetailsDto(entity);
    }

    @Transactional(readOnly = true)
    public Page<MovieCardDTO> findByGenre(Long genreId, Pageable pageable) {
        if (genreId == 0) {
            return repository.findAll(pageable).map(this::copyEntityToMovieCardDto);
        } else {
            return repository.findByGenreId(genreId, pageable).map(this::copyEntityToMovieCardDto);
        }
    }

    private MovieDetailsDTO copyEntityToMovieDetailsDto(Movie entity) {
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

    private MovieCardDTO copyEntityToMovieCardDto(Movie entity) {
        MovieCardDTO dto = new MovieCardDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setSubTitle(entity.getSubTitle());
        dto.setYear(entity.getYear());
        dto.setImgUrl(entity.getImgUrl());

        return dto;
    }
}
