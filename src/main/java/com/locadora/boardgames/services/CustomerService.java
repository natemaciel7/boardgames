package com.locadora.boardgames.services;

import com.locadora.boardgames.dtos.CustomerDTO;
import com.locadora.boardgames.models.Customer;
import com.locadora.boardgames.repositories.CustomerRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
    }

    public Customer createCustomer(CustomerDTO dto) {
        customerRepository.findByCpf(dto.cpf()).ifPresent(c -> {
            throw new EntityExistsException("CPF já cadastrado");
        });

        Customer customer = new Customer(null, dto.name(), dto.cpf(), dto.phone());
        return customerRepository.save(customer);
    }
}
