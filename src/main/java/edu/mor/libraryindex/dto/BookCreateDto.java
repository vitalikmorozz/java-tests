package edu.mor.libraryindex.dto;

import java.time.LocalDateTime;
import java.util.List;

public record BookCreateDto (String title, String description, List<String> authors, LocalDateTime publishedAt) {

}
