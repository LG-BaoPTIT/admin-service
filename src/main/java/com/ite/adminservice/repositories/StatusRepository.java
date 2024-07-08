package com.ite.adminservice.repositories;

import com.ite.adminservice.entities.Status;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends MongoRepository<Status,String> {
    Status findStatusByStatusId(String statusId);
    Status findStatusByStatusName(String name);
}
