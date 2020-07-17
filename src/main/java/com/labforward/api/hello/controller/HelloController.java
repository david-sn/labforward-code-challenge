package com.labforward.api.hello.controller;

import com.labforward.api.core.domain.ApiMessage;
import com.labforward.api.core.exception.ResourceNotFoundException;
import com.labforward.api.hello.domain.Greeting;
import com.labforward.api.hello.service.HelloWorldService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class HelloController {

    public static final String GREETING_NOT_FOUND = "Greeting Not Found";
    public static final String GREETING_CREATED = "Greeting Created Successfully";
    public static final String GREETING_LOADED = "Greeting Listed Successfully";
    public static final String GREETING_UPDATED = "Greeting Updated Successfully";
    public static final String GREETING_DELETED = "Greeting Deleted Successfully";



    private final HelloWorldService helloWorldService;

    public HelloController(HelloWorldService helloWorldService) {
        this.helloWorldService = helloWorldService;
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @ResponseBody
    public ApiMessage<Greeting> helloWorld() {
        return getHello(HelloWorldService.DEFAULT_ID);
    }

    @RequestMapping(value = "/hello/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ApiMessage<Greeting> getHello(@PathVariable String id) {
	 return getApiMessage("OK",helloWorldService.getGreeting(id)
                .orElseThrow(() -> new ResourceNotFoundException(GREETING_NOT_FOUND)),GREETING_LOADED);
    }

    @RequestMapping(value = "/hello", method = RequestMethod.POST)
    public ApiMessage<Greeting> createGreeting(@RequestBody Greeting request) {
        return getApiMessage("OK",helloWorldService.createGreeting(request),GREETING_CREATED);
    }

    @RequestMapping(value = "/hello/{id}", method = RequestMethod.PUT)
    public ApiMessage<Greeting> updateGreeting(@PathVariable String id, @RequestBody Greeting request) {
        return getApiMessage("OK",helloWorldService.updateGreeting(id,request)
                 .orElseThrow(() -> new ResourceNotFoundException(GREETING_NOT_FOUND)),GREETING_UPDATED);
    }
    
    
    @RequestMapping(value = "/hellos", method = RequestMethod.GET)
    @ResponseBody
    public ApiMessage<Greeting> getAllGreets() {
	 return getApiMessage("OK",helloWorldService.getAllGreetings(),GREETING_LOADED);
    }
    
    @RequestMapping(value = "/hello/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ApiMessage<Greeting> deleteGreets(@PathVariable String id) {
	 return getApiMessage("OK",helloWorldService.deleteGreeting(id)
                 .orElseThrow(() -> new ResourceNotFoundException(GREETING_NOT_FOUND)),GREETING_DELETED);
    }
    
    
    private <T>ApiMessage getApiMessage(String status, T result,String msg) {
	return new ApiMessage<>(status,result,msg);
    }
}
