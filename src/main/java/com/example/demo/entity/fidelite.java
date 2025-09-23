package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
public class Fidelite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Client client;

    private int points = 0;

    public Fidelite() {}

    public Fidelite(Client client) {
        this.client = client;
        this.points = 0;
    }

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }
}
