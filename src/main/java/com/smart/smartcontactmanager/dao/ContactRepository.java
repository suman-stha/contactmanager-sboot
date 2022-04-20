package com.smart.smartcontactmanager.dao;

import java.util.List;

import com.smart.smartcontactmanager.entities.Contact;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContactRepository extends JpaRepository<Contact, Integer> {

    // pagination.....
    @Query("from Contact as c where c.user.id =:userId")
    // currentPage-page
    // Contact per page-5
    public List<Contact> findContactsByUser(@Param("userId") int userId);

}
