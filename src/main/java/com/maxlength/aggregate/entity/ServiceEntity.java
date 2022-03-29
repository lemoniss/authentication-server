package com.maxlength.aggregate.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.maxlength.spec.enums.Yesno;
import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
@Table(name = "service_mst")
public class ServiceEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name= "name", length = 50)
    private String name;

    @Column(name= "contents", length = 2000)
    private String contents;

    @Column(name= "del_yn", length = 1, columnDefinition = "char")
    @Enumerated(EnumType.STRING)
    private Yesno delYn;

}