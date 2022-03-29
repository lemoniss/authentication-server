package com.maxlength.aggregate.repository;

import com.maxlength.aggregate.entity.AccountEntity;
import com.maxlength.spec.enums.Yesno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    AccountEntity findByUsernameAndDelYn(String username, Yesno delYn);

    AccountEntity findByIdAndDelYn(Long id, Yesno delYn);

    @Query(value = "select a.* from account_mst a inner join account_service cs on a.id = cs.account_id "
        + "and cs.service_id = (select id from service_mst where name = :service) "
        + "where a.username = :username and a.del_yn = :delYn and a.provider = :provider", nativeQuery = true)
    AccountEntity findByUsernameAdnDelYnAndProviderAndServiceName(String username, String delYn, String provider, String service);

}