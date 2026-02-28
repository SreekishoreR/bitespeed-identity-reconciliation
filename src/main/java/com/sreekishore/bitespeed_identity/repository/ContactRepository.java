package com.sreekishore.bitespeed_identity.repository;

import com.sreekishore.bitespeed_identity.entity.Contact;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    @Query("""
           SELECT c FROM Contact c
           WHERE (:email IS NOT NULL AND c.email = :email)
              OR (:phone IS NOT NULL AND c.phoneNumber = :phone)
           """)
    List<Contact> findMatchingContacts(
            @Param("email") String email,
            @Param("phone") String phone
    );

    List<Contact> findByLinkedId(Long linkedId);
}