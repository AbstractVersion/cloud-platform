/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kafka.template.controller;

import com.kafka.template.entity.Book;
import com.kafka.template.entity.LibraryEvent;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.kafka.template.repository.BookRepository;

/**
 *
 * @author onelove
 */
@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository repository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Iterable<Book> getAllPets() {
        return repository.findAll();
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Book addAllPet(@RequestBody Book event) {
        return repository.save(event);
    }

    @RequestMapping(value = "/{eventId}", method = RequestMethod.DELETE)
    public void addAllPet(@PathVariable Integer eventId) {
        repository.deleteById(eventId);
    }

}
