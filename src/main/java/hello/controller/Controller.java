package hello.controller;


import hello.model.Person;
import hello.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ACH1 on 19/05/2016.
 */
@org.springframework.stereotype.Controller

public class Controller {

    private final PersonRepository repository;

    @Autowired
    public Controller(PersonRepository rep){
        repository = rep;
    }

    @RequestMapping(value = "/persons", method = RequestMethod.GET)
    @ResponseBody
    public Person doSomthing(@RequestParam(value="name", defaultValue="toot" ) String name)
    {
        Person person = repository.findByFirstName(name);
        if(person != null) return person;
        return null;
    }

}
