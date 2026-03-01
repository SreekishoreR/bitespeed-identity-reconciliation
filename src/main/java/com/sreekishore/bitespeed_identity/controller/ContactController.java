package com.sreekishore.bitespeed_identity.controller;

import com.sreekishore.bitespeed_identity.dto.*;
import com.sreekishore.bitespeed_identity.service.ContactService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ContactController{

    private final ContactService contactService;

    public ContactController(ContactService contactService){
        this.contactService = contactService;
    }

    @PostMapping("/identify")
    public ResponseEntity<IdentifyResponse> identify(@RequestBody IdentifyRequest request){
        return ResponseEntity.ok(contactService.identify(request));
    }
    
    @GetMapping("/health")
    public String health(){
        return "Service is running";
    }
    
}