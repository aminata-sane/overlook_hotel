package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "fidelites")
public class Fidelite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int points = 0;

    // Chaque fidélité est associée à un client
    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
}
