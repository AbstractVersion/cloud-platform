/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kafka.template.repository;

import com.kafka.template.entity.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author onelove
 */
public interface BookRepository extends PagingAndSortingRepository<Book, Integer> {
//      Book findBy_id(Integer id);
}
