package com.example.demo.repository;

import com.example.demo.entity.Fidelite;
import com.example.demo.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FideliteRepository extends JpaRepository<Fidelite, Long> {
    Optional<Fidelite> findByClient(Client client);
}
