package com.example.assessment.etiqa.service;

import com.example.assessment.etiqa.dto.CustomerDTO;
import com.example.assessment.etiqa.exception.InvalidEmailException;
import com.example.assessment.etiqa.exception.NotFoundException;
import com.example.assessment.etiqa.model.Customer;
import com.example.assessment.etiqa.model.EmailType;
import com.example.assessment.etiqa.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository custRepo;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    //checks the emails in the map with email types
    protected void validateEmails(Map<EmailType, String> emails) {

        //ensure at least one required email type is present and not empty
        boolean hasRequired = Arrays.stream(EmailType.values())
                .filter(EmailType::isRequired)
                .anyMatch(type ->
                        emails.containsKey(type) && StringUtils.hasText(emails.get(type)));

        if (!hasRequired) {
            log.error("At least one email must be provided: OFFICE or PERSONAL.");
            throw new InvalidEmailException("At least one email must be provided: OFFICE or PERSONAL.");
        }

        for (Map.Entry<EmailType, String> entry : emails.entrySet()) {
            String email = entry.getValue();

            if(!EnumSet.allOf(EmailType.class).contains(entry.getKey())) {
                log.error("Invalid email type provided : " + entry.getKey());
                throw new InvalidEmailException("Invalid email type provided : " + entry.getKey());
            }

            //checks for empty email fields
            if(!StringUtils.hasText(email)) {
                log.error(entry.getKey() + " email is empty.");
                throw new InvalidEmailException(entry.getKey() + " email is empty.");
            }

            //checks for invalid email patterns
            if(!EMAIL_PATTERN.matcher(email).matches()) {
                log.error("Invalid format for " + entry.getKey()+ " email: " + email) ;
                throw new InvalidEmailException("Invalid format for " + entry.getKey() + " email: " + email);
            }
        }
    }

    public CustomerDTO mapToCustomerDTO (Customer customer) {
        return CustomerDTO.builder()
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .emails(customer.getEmails())
                .familyMembers(customer.getFamilyMembers())
                .build();
    }

    public Customer mapToEntity (CustomerDTO customerDTO) {
        return Customer.builder()
                .firstName(customerDTO.getFirstName())
                .lastName(customerDTO.getLastName())
                .emails(customerDTO.getEmails())
                .familyMembers(customerDTO.getFamilyMembers())
                .build();

    }

    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        validateEmails(customerDTO.getEmails());
        custRepo.save(mapToEntity(customerDTO));
        return customerDTO;
    }

    public List<Customer> getAllCustomers() {
        return custRepo.findAll();
    }

    public Customer getCustomerById(Long id) {
        return custRepo.findById(id).orElseThrow(() -> {
            log.error("Customer not found with id : " + id);
            return new NotFoundException("Customer Not found with id : " + id);
        });
    }

    public Customer updateCustomer(Long id, Customer customer) {
        Customer existing = getCustomerById(id);
        validateEmails(customer.getEmails());


//        existing.setFirstName(customer.getFirstName());
//        existing.setLastName(customer.getLastName());
//        existing.setEmails(customer.getEmails());
//        existing.setFamilyMembers(customer.getFamilyMembers());

        BeanUtils.copyProperties(customer, existing, "id");

        return custRepo.save(existing);
    }

    public void deleteCustomer(Long id) {
        getCustomerById(id);
        custRepo.deleteById(id);
    }

}
