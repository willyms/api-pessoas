package com.pessoas.models;

import com.pessoas.models.dto.*;
import lombok.*;

import javax.persistence.*;




@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Telefone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TelefoneDTO.TipoTelefone tipo = TelefoneDTO.TipoTelefone.CASA;

    @Column(nullable = false, unique = true)
    private String numero;
}
