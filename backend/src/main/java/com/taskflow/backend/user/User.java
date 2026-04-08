package com.taskflow.backend.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data//tramite lombok genera automaticamente getters, setters, toString, equals e hashCode
@NoArgsConstructor //tramite lombok genera automaticamente un constructor senza args
@AllArgsConstructor//tramite lombok genera automaticamente un constructor con tutti gli args
@Builder//tramite lombok genera automaticamente un builder pattern per creare oggetti User in modo fluente
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;


}
