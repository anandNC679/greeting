package com.example.controller;

import com.example.modal.Greeting;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    private static Greeting save(Greeting greeting) {

        if(bigIntegerGreetingMap==null){
            bigIntegerGreetingMap=new HashMap<BigInteger, Greeting>();
            nextId=BigInteger.ONE;
        }
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
        System.out.println(list);
        return new ResponseEntity<Collection<Greeting>>(list, HttpStatus.OK);
    }
}
