package com.locadora.boardgames.service;

import com.locadora.boardgames.dto.CustomerRequestDTO;
import com.locadora.boardgames.dto.CustomerResponseDTO;
import com.locadora.boardgames.model.Customer;
import com.locadora.boardgames.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository repository;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public List<CustomerResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(c -> new CustomerResponseDTO(c.getId(), c.getName(), c.getCpf(), c.getPhone()))
                .collect(Collectors.toList());
    }

    public Optional<CustomerResponseDTO> findById(Long id) {
        return repository.findById(id)
                .map(c -> new CustomerResponseDTO(c.getId(), c.getName(), c.getCpf(), c.getPhone()));
    }

    public CustomerResponseDTO create(CustomerRequestDTO dto) {
        if (repository.findByCpf(dto.getCpf()).isPresent()) {
            throw new RuntimeException("CPF already exists");
        }

        Customer customer = new Customer(null, dto.getName(), dto.getCpf(), dto.getPhone());
        Customer saved = repository.save(customer);
        return new CustomerResponseDTO(saved.getId(), saved.getName(), saved.getCpf(), saved.getPhone());
    }
}
