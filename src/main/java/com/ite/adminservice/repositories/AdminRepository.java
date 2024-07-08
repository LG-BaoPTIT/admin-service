package com.ite.adminservice.repositories;

import com.ite.adminservice.entities.Admin;
import com.ite.adminservice.entities.EStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends MongoRepository<Admin, String> {
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    boolean existsByRoleGroupId(String roleGroupId);
    Admin findAdminByEmail(String email);
    Admin findAdminByAdminId(String adminId);
    Page<Admin> findAll( Pageable pageable);

//    @Query("{ 'status': ?0, '_id': { $ne: ?1 } }")
//    Page<Admin> findByStatusAndNotId(EStatus status, String id, Pageable pageable);
    //    Page<Admin> findByNameContainingIgnoreCaseOrPhoneContainingIgnoreCase(String name, String phone, Pageable pageable);
//    Page<Admin> findByNameContainingIgnoreCaseAndStatusOrPhoneContainingIgnoreCaseAndStatus(
//            String name, EStatus status1,
//            String phone, EStatus status2,
//            Pageable pageable);
//    @Query("{$and:[{$or:[{'email': {$regex: ?0, $options: 'i'}}, {'phone': {$regex: ?0, $options: 'i'}}]}, {'status': ?1}, {'_id': {$ne: ?2}}]}")
//    Page<Admin> findByKeywordAndStatusAndNotId(String keyword, EStatus status, String id, Pageable pageable);
//
//    @Query("{$or:[{'email': {$regex: ?0, $options: 'i'}}, {'phone': {$regex: ?0, $options: 'i'}}], '_id': {$ne: ?1}}")
//    Page<Admin> findByKeywordAndNotId(String keyword, String id, Pageable pageable);
    List<Admin> findByRoleGroupId(String roleGroupId);
    @Query("{ 'status': ?0 }")
    Page<Admin> findByStatus(EStatus status, Pageable pageable);

    @Query("{$and:[{$or:[{'email': {$regex: ?0, $options: 'i'}}, {'phone': {$regex: ?0, $options: 'i'}}]}, {'status': ?1}]}")
    Page<Admin> findByKeywordAndStatus(String keyword, EStatus status, Pageable pageable);

    @Query("{$or:[{'email': {$regex: ?0, $options: 'i'}}, {'phone': {$regex: ?0, $options: 'i'}}]}")
    Page<Admin> findByKeyword(String keyword, Pageable pageable);



}
