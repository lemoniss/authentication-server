package com.maxlength.aggregate.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.maxlength.spec.enums.Provider;
import com.maxlength.spec.enums.Yesno;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account_mst", indexes = @Index(name = "idx_account_mst_username", columnList = "username"))
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class AccountEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @Column(name= "username", length = 50)
    private String username;

    @Column(name= "password", length = 200)
    private String password;

    @Column(name= "provider", length = 10)
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Column(name= "del_yn", length = 1, columnDefinition = "char")
    @Enumerated(EnumType.STRING)
    private Yesno delYn;

    @OneToMany(mappedBy = "accountEntity")
    private List<AccountRoleEntity> accountRoleEntityList = new ArrayList<>();

}