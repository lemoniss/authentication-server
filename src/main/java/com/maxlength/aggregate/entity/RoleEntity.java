package com.maxlength.aggregate.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
@Table(name = "role_mst")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class RoleEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name= "name", length= 50, nullable= false)
    private String name;

    @Column(name= "del_yn", length = 1, columnDefinition = "char")
    @Enumerated(EnumType.STRING)
    private Yesno delYn;

    @OneToMany(mappedBy = "roleEntity")
    List<AccountRoleEntity> accountRoleEntityList = new ArrayList<>();
}
