package com.maxlength.aggregate.repository;

import com.maxlength.aggregate.entity.ClientDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientDetailsRepository extends JpaRepository<ClientDetailsEntity, Long> {

    ClientDetailsEntity findByClientId(String clientId);
}