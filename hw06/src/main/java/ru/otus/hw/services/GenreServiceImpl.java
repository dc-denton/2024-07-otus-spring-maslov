package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.repositories.mappers.GenreRowMapper;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    private final GenreRowMapper genreRowMapper;

    @Override
    public List<GenreDto> findAll() {
        return genreRepository.findAll().stream()
                .map(genreRowMapper::toDto)
                .toList();
    }
}
