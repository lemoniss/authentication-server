package com.maxlength.aggregate.entity;

import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import javax.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "account_role", uniqueConstraints = @UniqueConstraint(columnNames = {"account_id", "role_id"}))
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class AccountRoleEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "account_id", insertable = false, updatable = false)
    private Long accountId;

    @Column(name = "role_id", insertable = false, updatable = false)
    private Long roleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "account_id")
    private AccountEntity accountEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "role_id")
    private RoleEntity roleEntity;

}
