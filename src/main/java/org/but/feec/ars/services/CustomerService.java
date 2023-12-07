package org.but.feec.ars.services;

import org.but.feec.ars.api.CustomerCreateView;
import org.but.feec.ars.data.CustomerRepository;

import static org.but.feec.ars.services.Argon2FactoryService.ARGON2;

public class CustomerService {
    private CustomerRepository customerRepository;

    public CustomerService(CustomerRepository personRepository) {
        this.customerRepository = personRepository;
    }

    public void createCustomer(CustomerCreateView customerCreateView){
        char[] originalPassword = customerCreateView.getPassword();
        char[] hashedPassword = hashPassword(originalPassword);
        customerCreateView.setPassword(hashedPassword);

        customerRepository.createCustomer(customerCreateView);
    }

    public char[] hashPassword(char[] password){
        return ARGON2.hash(10, 65536, 1, password).toCharArray();
    }
}
