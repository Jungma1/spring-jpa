package jpabook.jpashop.domain;

import static lombok.AccessLevel.PROTECTED;

import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class Address {

    private String city;

    private String street;

    private String zipcode;
}
