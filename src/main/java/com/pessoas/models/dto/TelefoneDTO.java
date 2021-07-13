package com.pessoas.models.dto;

import lombok.*;

import javax.persistence.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TelefoneDTO {

    @Getter
    @AllArgsConstructor
    public enum TipoTelefone {
        CASA("Casa"),
        CELULAR("Celular"),
        LOJA("Loja");
        private final String descricao;
    }

    private Long id;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTelefone tipo = TipoTelefone.CASA;

    @Column(nullable = false, unique = true)
    private String numero;
}
