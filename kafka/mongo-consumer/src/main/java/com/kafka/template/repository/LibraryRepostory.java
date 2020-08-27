/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kafka.template.repository;

import com.kafka.template.entity.LibraryEvent;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author onelove
 */
public interface LibraryRepostory extends PagingAndSortingRepository<LibraryEvent, Integer> {

//    LibraryEvent findBy_id(Integer id);
}
