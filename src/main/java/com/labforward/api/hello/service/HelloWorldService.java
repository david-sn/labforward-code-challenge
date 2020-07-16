package com.labforward.api.hello.service;

import com.labforward.api.core.validation.EntityValidator;
import com.labforward.api.hello.domain.Greeting;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class HelloWorldService {

    public static final String GREETING_NOT_FOUND = "Greeting Not Found";

    public static String DEFAULT_ID = "default";

    public static String DEFAULT_MESSAGE = "Hello World!";

    private final Map<String, Greeting> greetings;

    private final EntityValidator entityValidator;

    public HelloWorldService(EntityValidator entityValidator) {
        this.entityValidator = entityValidator;

        this.greetings = new HashMap<>(1);
        save(getDefault());
    }

    /**
     * created default greeting
     * @return default greeting
     */
    private static Greeting getDefault() {
        return new Greeting(DEFAULT_ID, DEFAULT_MESSAGE);
    }

    /**
     * @param request the greeting need to be created
     * @return saved Greeting
     */
    public Greeting createGreeting(Greeting request) {
        entityValidator.validateCreate(request);

        request.setId(UUID.randomUUID().toString());
        return save(request);
    }

    /**
     * @param id the greeting need to be retrieved
     * @return Optional as box maybe Greeting not found for retrieved
     */
    public Optional<Greeting> getGreeting(String id) {
        Greeting greeting = greetings.get(id);
        if (greeting == null) {
            return Optional.empty();
        }
        return Optional.of(greeting);
    }

    /**
     * @return default greeting
     */
    public Optional<Greeting> getDefaultGreeting() {
        return getGreeting(DEFAULT_ID);
    }

    /**
     * update exist greeting in database
     * @param id need to be updated
     * @param newGreeting need to be update message
     * @return Optional as box maybe Greeting not found for updating
     */
    public Optional<Greeting> updateGreeting(String id, Greeting newGreeting) {
        entityValidator.validateUpdate(newGreeting);

        Optional<Greeting> existingGreeting = getGreeting(id);
        if (existingGreeting.isPresent()) {
            newGreeting.setId(id);
            return Optional.of(save(newGreeting));
        } else {
            return Optional.empty();
        }
    }

    /**
     * get all exists greeting in database
     * @return list All greets
     */
    public List<Greeting> getAllGreetings() {
        List<Greeting> data = new ArrayList<>(greetings.size());
        if (!greetings.isEmpty()) {
            this.greetings.entrySet().stream().forEach(greet -> data.add(greet.getValue()));
        }
        return data;
    }

    /**
     * deleting service for delete existing greeting
     * @param id greeting Id
     * @return Optional as box maybe Greeting not found
     */
    public Optional<Greeting> deleteGreeting(String id) {
        Optional<Greeting> existingGreeting = getGreeting(id);
        if (existingGreeting.isPresent()) {
            return Optional.of(delete(existingGreeting.get()));
        } else {
            return Optional.empty();
        }
    }

    /**
     * internal function for saving new data
     * @param greeting need to be saved as new
     * @return saved greeting
     */
    private Greeting save(Greeting greeting) {
        this.greetings.put(greeting.getId(), greeting);

        return greeting;
    }

    /**
     * @param greeting need to be deleted
     * @return delete greeting
     */
    private Greeting delete(Greeting greeting) {
        return this.greetings.remove(greeting.getId());
    }
}
