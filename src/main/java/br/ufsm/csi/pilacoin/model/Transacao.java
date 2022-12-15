package br.ufsm.csi.pilacoin.model;

import javax.persistence.*;

@Entity
public class Transacao {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_bloco")
    private Bloco bloco;

    @ManyToOne
    @JoinColumn(name = "id_pila")
    private PilaCoin pilaCoin;

}
