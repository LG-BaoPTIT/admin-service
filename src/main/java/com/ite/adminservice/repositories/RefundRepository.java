package com.ite.adminservice.repositories;

import com.ite.adminservice.entities.Refund;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefundRepository extends MongoRepository<Refund,String> {
}
