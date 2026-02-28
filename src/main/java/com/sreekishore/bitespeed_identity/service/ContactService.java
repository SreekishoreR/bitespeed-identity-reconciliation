package com.sreekishore.bitespeed_identity.service;

import com.sreekishore.bitespeed_identity.dto.*;
import com.sreekishore.bitespeed_identity.entity.*;
import com.sreekishore.bitespeed_identity.repository.ContactRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ContactService{

    private final ContactRepository repository;

    public ContactService(ContactRepository repository){
        this.repository = repository;
    }

    public IdentifyResponse identify(IdentifyRequest request){

        if(request.getEmail() == null && request.getPhoneNumber() == null){
            throw new RuntimeException("Email or PhoneNumber is required");
        }

        List<Contact> matchedContacts = repository.findMatchingContacts(request.getEmail(),request.getPhoneNumber());

        if(matchedContacts.size() == 0){
            return createPrimaryContact(request);
        }

        List<Contact>allContacts = new ArrayList<>();
        Set<Long>ids = new HashSet<>();

        for(Contact c : matchedContacts){
            ids.add(c.getId());

            if(c.getLinkedId() != null){
                ids.add(c.getLinkedId());
            }
        }

        allContacts = repository.findAllById(ids);

        Contact primary = findOldestPrimary(allContacts);

        updateExtraPrimaries(allContacts, primary);

        if(isNewInformation(request, allContacts)){
            createSecondaryContact(request, primary);
        }

        return buildResponse(primary);
    }


    private IdentifyResponse createPrimaryContact(IdentifyRequest request){

        Contact contact = new Contact();
        contact.setEmail(request.getEmail());
        contact.setPhoneNumber(request.getPhoneNumber());
        contact.setLinkPrecedence(LinkPrecedence.PRIMARY);

        repository.save(contact);

        return buildResponse(contact);
    }


    private Contact findOldestPrimary(List<Contact> contacts){

        Contact oldest = null;

        for(Contact c : contacts){
            if(c.getLinkPrecedence() == LinkPrecedence.PRIMARY){

                if(oldest == null){
                    oldest = c;
                }else{
                    if(c.getCreatedAt().isBefore(oldest.getCreatedAt())){
                        oldest = c;
                    }
                }
            }
        }

        return oldest;
    }


    private void updateExtraPrimaries(List<Contact> contacts, Contact primary){

        for(Contact c : contacts){

            if(c.getLinkPrecedence() == LinkPrecedence.PRIMARY && !c.getId().equals(primary.getId())){
            	c.setLinkPrecedence(LinkPrecedence.SECONDARY);
                c.setLinkedId(primary.getId());
                repository.save(c);
            }
        }
    }


    private boolean isNewInformation(IdentifyRequest request, List<Contact> contacts){

        boolean emailExists = false;
        boolean phoneExists = false;

        for(Contact c : contacts){

            if(request.getEmail() != null && request.getEmail().equals(c.getEmail())){
                emailExists = true;
            }

            if(request.getPhoneNumber() != null && request.getPhoneNumber().equals(c.getPhoneNumber())){
                phoneExists = true;
            }
        }

        return !(emailExists && phoneExists);
    }


    private void createSecondaryContact(IdentifyRequest request, Contact primary){

        Contact secondary = new Contact();
        secondary.setEmail(request.getEmail());
        secondary.setPhoneNumber(request.getPhoneNumber());
        secondary.setLinkedId(primary.getId());
        secondary.setLinkPrecedence(LinkPrecedence.SECONDARY);

        repository.save(secondary);
    }


    private IdentifyResponse buildResponse(Contact primary){

        List<Contact> secondaries = repository.findByLinkedId(primary.getId());

        List<String> emails = new ArrayList<>();
        List<String> phoneNumbers = new ArrayList<>();
        List<Long> secondaryIds = new ArrayList<>();

        if(primary.getEmail() != null) {
            emails.add(primary.getEmail());
        }

        if(primary.getPhoneNumber() != null) {
            phoneNumbers.add(primary.getPhoneNumber());
        }

        for(Contact c : secondaries) {
            if(c.getEmail() != null &&
                    !emails.contains(c.getEmail())) {
                emails.add(c.getEmail());
            }

            if(c.getPhoneNumber() != null && !phoneNumbers.contains(c.getPhoneNumber())){
                phoneNumbers.add(c.getPhoneNumber());
            }

            secondaryIds.add(c.getId());
        }

        IdentifyResponse.ContactResponse contactResponse = new IdentifyResponse.ContactResponse(primary.getId(),emails,
        																					    phoneNumbers,
        																					    secondaryIds
        																					    );

        return new IdentifyResponse(contactResponse);
    }
}