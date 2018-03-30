package com.example.controller;

import com.example.modal.Greeting;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * author anand.
 * since on 30/3/18.
 *
 *
 */
@RestController
public class GreetingController {

    private static BigInteger nextId;
    private static Map<BigInteger,Greeting> bigIntegerGreetingMap;

    static {
        Greeting g1=new Greeting();
        g1.setName("Hello World1");
        save(g1);

        Greeting g2=new Greeting();
        g2.setName("Hello World2");
        save(g2);


    }

    private static boolean delete(BigInteger id){
        Greeting deletedGreeting=bigIntegerGreetingMap.remove(id);
        if(deletedGreeting==null)
            return false;
        return true;
    }

    private static Greeting save(Greeting greeting) {

        if(bigIntegerGreetingMap==null){
            bigIntegerGreetingMap=new HashMap<BigInteger, Greeting>();
            nextId=BigInteger.ONE;
        }

        //update...
        if(greeting.getId()!=null){
            Greeting oldGreeting=bigIntegerGreetingMap.get(greeting.getId());
            if(oldGreeting==null)
                return null;
            bigIntegerGreetingMap.remove(oldGreeting.getId());
            bigIntegerGreetingMap.put(greeting.getId(),greeting);
            return greeting;
        }

        //create...
        greeting.setId(nextId);
        nextId=nextId.add(BigInteger.ONE);
        bigIntegerGreetingMap.put(greeting.getId(),greeting);
        return greeting;

    }

    @RequestMapping(
            value = "/api/greetings",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<Collection<Greeting>> getGreetings(){

        Collection<Greeting> list=bigIntegerGreetingMap.values();
        return new ResponseEntity<Collection<Greeting>>(list, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/api/greeting/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    private ResponseEntity<Greeting> getGreeting(@PathVariable("id") BigInteger id){
        Greeting greeting=bigIntegerGreetingMap.get(id);
        if(greeting==null){
            return new ResponseEntity<Greeting>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Greeting>(greeting,HttpStatus.OK);
    }

    @RequestMapping(
            value = "/api/greeting",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    private ResponseEntity<Greeting> createGreeting(@RequestBody Greeting greeting){
        Greeting savedGreeting=save(greeting);
        return new ResponseEntity<Greeting>(savedGreeting,HttpStatus.CREATED);
    }

    @RequestMapping(value = "/api/greeting",
    method = RequestMethod.PUT,
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<Greeting> updateGreeting(@RequestBody Greeting greeting){
        Greeting updatedGreeting=save(greeting);
        if(updatedGreeting==null)
            return new ResponseEntity<Greeting>(HttpStatus.NOT_FOUND);
        else
        return new ResponseEntity<Greeting>(greeting,HttpStatus.OK);
    }


    @RequestMapping(value = "/api/greeting/{id}",
    method = RequestMethod.DELETE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<Greeting> deleteGreeting(@PathVariable("id") BigInteger id,
                                                    @RequestBody Greeting greeting){
        boolean deleted=delete(id);
        if(!deleted)
            return new ResponseEntity<Greeting>(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<Greeting>(HttpStatus.NO_CONTENT);
    }
}
