package com.maxlength.aggregate.repository;

import com.maxlength.aggregate.entity.ServiceEntity;
import com.maxlength.spec.enums.Yesno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    ServiceEntity findByNameAndDelYn(String name, Yesno delYn );
}