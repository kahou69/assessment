package com.example.assessment.etiqa.dto;

import com.example.assessment.etiqa.model.EmailType;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class CustomerDTO {

    private String firstName;
    private String lastName;
    private Map<EmailType, String> emails = new HashMap<>();
    private List<String> familyMembers;
}
