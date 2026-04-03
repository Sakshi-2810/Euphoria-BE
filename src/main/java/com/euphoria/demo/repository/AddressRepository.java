package com.euphoria.demo.repository;

import com.euphoria.demo.model.Address;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AddressRepository extends MongoRepository<Address, String> {
    List<Address> findByUserEmail(String email);
}
