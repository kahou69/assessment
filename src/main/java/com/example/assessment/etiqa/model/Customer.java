package com.example.assessment.etiqa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.LifecycleState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @ElementCollection
    @CollectionTable(name = "customer_emails", joinColumns = @JoinColumn(name = "customer_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "email_type")
    @Column(name = "email_address")
    private Map<EmailType, String> emails = new HashMap<>();

    @ElementCollection
    private List<String> familyMembers;
}
