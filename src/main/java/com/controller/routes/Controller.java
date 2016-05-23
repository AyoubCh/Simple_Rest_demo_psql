package com.controller.routes;


import com.controller.services.Service;
import com.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ACH1 on 19/05/2016.
 */

@RestController
@RequestMapping("/persons")
@ResponseBody
public class Controller  {


    @Autowired
    private Service service;


    @RequestMapping( method = RequestMethod.GET)
    public Person getPersonNamed(@RequestParam(value="name"/*, defaultValue="userName"*/ ) String name)
            throws IllegalAccessError
    {
        //Person person = repository.findByFirstName(name);
        Person person = service.getPersonNamed(name);
        if(person != null) return person;
        throw new resourceNotFoundException("Person with name '"+name+"' not found.");
    }


    @RequestMapping( path = "/{id}", method = RequestMethod.GET)
    public Person getPersonId(@PathVariable Long id)
    {
        Person person = service.getPersonById(id);
        if(person != null) return person;
        throw new resourceNotFoundException("Person with id '"+id+"' not found.");

    }

    @RequestMapping( path = "/{id}/delete", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id)
    {
        if( ! service.deletePerson(id)) throw new resourceNotFoundException( "Can't delete Person with id "+id);
        //return new String("{ok}");
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Person update(@RequestBody Person person)
    {
        person = service.update(person);
        if( person == null ) throw new resourceNotFoundException( "Can't update Person with id "+person.getId());
        return person;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Person create (@RequestBody Person person)
    {
        person = service.add(person);
        if(  person == null) throw new resourceNotFoundException( "Can't create Person with name"+person.getId());
        return person;
    }

}


