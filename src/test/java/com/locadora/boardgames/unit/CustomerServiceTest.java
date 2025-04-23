package com.locadora.boardgames.unit;

import com.locadora.boardgames.dtos.CustomerDTO;
import com.locadora.boardgames.models.Customer;
import com.locadora.boardgames.repositories.CustomerRepository;
import com.locadora.boardgames.services.CustomerService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    private CustomerService customerService;
    private CustomerRepository customerRepository;

    @BeforeEach
    void setup() {
        customerRepository = mock(CustomerRepository.class);
        customerService = new CustomerService(customerRepository);
    }

    @Test
    void shouldReturnAllCustomers() {
        when(customerRepository.findAll()).thenReturn(List.of(new Customer()));
        assertEquals(1, customerService.getAllCustomers().size());
    }

    @Test
    void shouldReturnCustomerById() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(new Customer()));
        assertNotNull(customerService.getCustomerById(1L));
    }

    @Test
    void shouldThrowWhenCustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> customerService.getCustomerById(1L));
    }

    @Test
    void shouldThrowWhenCpfExists() {
        CustomerDTO dto = new CustomerDTO("Ana", "12345678900", "48999999999");
        when(customerRepository.findByCpf("12345678900")).thenReturn(Optional.of(new Customer()));
        assertThrows(EntityExistsException.class, () -> customerService.createCustomer(dto));
    }

    @Test
    void shouldCreateCustomer() {
        CustomerDTO dto = new CustomerDTO("Ana", "12345678900", "48999999999");
        when(customerRepository.findByCpf(dto.cpf())).thenReturn(Optional.empty());
        when(customerRepository.save(any())).thenReturn(new Customer(1L, dto.name(), dto.cpf(), dto.phone()));

        Customer customer = customerService.createCustomer(dto);
        assertEquals("Ana", customer.getName());
    }
}
