package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.repositories.UserRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {

    @Autowired
    ReviewRepository repository;

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    UserRepository userRepository;

    @Transactional(readOnly = true)
    public ReviewDTO findById(Long id) {
        Review entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return copyEntityToReviewDto(entity);
    }

    @Transactional
    public ReviewDTO insert(ReviewDTO dto) {
        Review entity = copyDtoToEntity(dto);
        entity = repository.save(entity);
        return copyEntityToReviewDto(entity);
    }

    private ReviewDTO copyEntityToReviewDto(Review entity) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(entity.getId());
        dto.setText(entity.getText());
        dto.setMovieId(entity.getMovie().getId());

        User user = authenticated();
        dto.setUserId(user.getId());
        dto.setUserName(user.getUsername());
        dto.setUserEmail(user.getEmail());

        return dto;
    }

    private Review copyDtoToEntity(ReviewDTO dto) {
        Review entity = new Review();
        entity.setText(dto.getText());

        Movie movie = movieRepository.findById(dto.getMovieId()).orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        entity.setMovie(movie);

        return entity;
    }

    protected User authenticated() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
            String username = jwtPrincipal.getClaim("username");
            return userRepository.findByEmail(username);
        }
        catch (Exception e) {
            throw new UsernameNotFoundException("Invalid user");
        }
    }
}
