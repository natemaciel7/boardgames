package com.locadora.boardgames.service;

import com.locadora.boardgames.dto.CustomerRequestDTO;
import com.locadora.boardgames.dto.CustomerResponseDTO;
import com.locadora.boardgames.model.Customer;
import com.locadora.boardgames.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    private CustomerRepository repository;
    private CustomerService service;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(CustomerRepository.class);
        service = new CustomerService(repository);
    }

    @Test
    void testCreateCustomerComSucesso() {
        CustomerRequestDTO dto = new CustomerRequestDTO();
        dto.setName("Cliente Teste");
        dto.setCpf("12345678901");
        dto.setPhone("11999999999");

        when(repository.findByCpf("12345678901")).thenReturn(Optional.empty());

        Customer saved = new Customer(1L, "Cliente Teste", "12345678901", "11999999999");
        when(repository.save(any(Customer.class))).thenReturn(saved);

        CustomerResponseDTO response = service.create(dto);

        assertNotNull(response);
        assertEquals("Cliente Teste", response.getName());
        assertEquals("12345678901", response.getCpf());
    }

    @Test
    void testCreateCustomerComCpfDuplicado() {
        CustomerRequestDTO dto = new CustomerRequestDTO();
        dto.setName("Duplicado");
        dto.setCpf("11111111111");
        dto.setPhone("11988888888");

        when(repository.findByCpf("11111111111"))
                .thenReturn(Optional.of(new Customer()));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> service.create(dto));

        assertEquals("CPF already exists", exception.getMessage());
    }

    @Test
    void testFindAllCustomers() {
        Customer customer = new Customer(1L, "Nome", "12345678901", "11999999999");
        when(repository.findAll()).thenReturn(List.of(customer));

        List<CustomerResponseDTO> list = service.findAll();

        assertEquals(1, list.size());
        assertEquals("Nome", list.get(0).getName());
    }
}
