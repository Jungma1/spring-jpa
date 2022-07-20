package jpabook.jpashop.domain;

import static lombok.AccessLevel.PROTECTED;

import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Embeddable
public class Address {

    private String city;

    private String street;
 
    private String zipcode;
}
