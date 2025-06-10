package com.example.assessment.etiqa.service;

import com.example.assessment.etiqa.exception.InvalidEmailException;
import com.example.assessment.etiqa.exception.CustomerNotFoundException;
import com.example.assessment.etiqa.model.Customer;
import com.example.assessment.etiqa.model.EmailType;
import com.example.assessment.etiqa.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
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
            throw new InvalidEmailException("At least one email must be provided: OFFICE or PERSONAL.");
        }

        for (Map.Entry<EmailType, String> entry : emails.entrySet()) {
            String email = entry.getValue();

            if(!EnumSet.allOf(EmailType.class).contains(entry.getKey())) {
                throw new InvalidEmailException("Invalid email type provided : " + entry.getKey());
            }

            //checks for empty email fields
            if(!StringUtils.hasText(email)) {
                throw new InvalidEmailException(entry.getKey() + " email is empty.");
            }

            //checks for invalid email patterns
            if(!EMAIL_PATTERN.matcher(email).matches()) {
                throw new InvalidEmailException("Invalid format for " + entry.getKey() + " email: " + email);
            }
        }
    }

    public Customer saveCustomer(Customer customer) {
        validateEmails(customer.getEmails());
        return custRepo.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return custRepo.findAll();
    }

    public Customer getCustomerById(Long id) {
        return custRepo.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer Not found with id : " + id));
    }

    public Customer updateCustomer(Long id, Customer customer) {
        Customer existing = getCustomerById(id);

        existing.setFirstName(customer.getFirstName());
        existing.setLastName(customer.getLastName());
        existing.setEmails(customer.getEmails());
        existing.setFamilyMembers(customer.getFamilyMembers());

        return custRepo.save(existing);
    }

    public void deleteCustomer(Long id) {
        custRepo.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer not found with id : " + id));
        custRepo.deleteById(id);
    }
}
