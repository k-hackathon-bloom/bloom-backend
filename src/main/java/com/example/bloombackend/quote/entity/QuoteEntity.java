package com.example.bloombackend.quote.entity;

import com.example.bloombackend.quote.controller.response.QuoteResponse;
import jakarta.persistence.*;

@Entity
@Table(name = "quotes")
public class QuoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String quote;

    @Column()
    private String author;

    protected QuoteEntity() {
    }

    public QuoteEntity(String quote, String author) {
        this.quote = quote;
        this.author = author;
    }

    public QuoteResponse toResponse() {
        return new QuoteResponse(quote, author);
    }
}