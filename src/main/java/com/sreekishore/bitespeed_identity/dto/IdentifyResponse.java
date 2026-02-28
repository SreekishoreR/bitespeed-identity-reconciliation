package com.sreekishore.bitespeed_identity.dto;

import java.util.List;

public class IdentifyResponse {

    private ContactResponse contact;

    public IdentifyResponse(ContactResponse contact){
        this.contact = contact;
    }

    public ContactResponse getContact(){
        return contact;
    }

    public static class ContactResponse{

        private Long primaryContactId;
        private List<String> emails;
        private List<String> phoneNumbers;
        private List<Long> secondaryContactIds;

        public ContactResponse(Long primaryContactId,
                               List<String> emails,
                               List<String> phoneNumbers,
                               List<Long> secondaryContactIds){
        	
            this.primaryContactId = primaryContactId;
            this.emails = emails;
            this.phoneNumbers = phoneNumbers;
            this.secondaryContactIds = secondaryContactIds;
            
        }

        public Long getPrimaryContactId(){ 
        	return primaryContactId; 
        }
        
        public List<String> getEmails(){ 
        	return emails; 
        }
        
        public List<String> getPhoneNumbers(){ 
        	return phoneNumbers; 
        }
        
        public List<Long> getSecondaryContactIds(){ 
        	return secondaryContactIds; 
        }
        
    }
}