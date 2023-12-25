package org.but.feec.ars.services;

import org.but.feec.ars.data.CustomerRepository;
import org.but.feec.ars.api.CustomerAuthView;

import static org.but.feec.ars.services.Argon2FactoryService.ARGON2;

public class AuthService {
    private CustomerRepository customerRepository;

    public AuthService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    private CustomerAuthView findPersonByEmail(String email) {
        return customerRepository.findCustomerByEmail(email);
    }

    public boolean authenticate(String email, String password){
        if (email == null || email.isEmpty() || password == null || password.isEmpty()){
            return false;
        }
        CustomerAuthView customerAuthView = findPersonByEmail(email);
        if (customerAuthView != null){
            return ARGON2.verify(customerAuthView.getPassword(), password.toCharArray());
        }else {
            return false;
        }

    }
}
