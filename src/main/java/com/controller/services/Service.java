package com.controller.services;

/**
 * Created by ACH1 on 20/05/2016.
 */

import com.google.common.util.concurrent.ExecutionError;
import com.model.Person;
import com.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



@Component
public class Service {

    @Autowired
    private PersonRepository repository;

    @Value("${message:World!}")
    private String msg;

    public String message() {
        return this.msg;
    }

    public Person getPersonNamed(String name)
    {
        // service processing
        return repository.findByFirstName(name);
    }

    public Person getPersonById(Long id) {
        return repository.findById(id);
    }

    public Person update(Person person)
    {
        try {
            person = repository.save(person);

        }catch (ExecutionError e){
            //e.printStackTrace();
            person = null;
        }

        return person;
    }

    public Person add(Person person)
    {
        Person res ;
        try {

            res = repository.save(new Person(person.getFirstName(),person.getLastName()));
            //res = repository.save(new Person("first name ","last name"));

        }catch (ExecutionError e){
            //e.printStackTrace();
            res = null;
        }

        return res;
    }

    public boolean deletePerson(Long id)
    {
        boolean res ;
        try {
            repository.delete(id);
            res = true;
        }catch (org.springframework.dao.EmptyResultDataAccessException e){
            //e.printStackTrace();
            res = false;
        }

        return res;
    }



}
