package br.ufsm.csi.pilacoin.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Bloco {

    @Id
    @GeneratedValue
    private Long id;
    private Long nonce;
    private byte[] hashBlocoAnterior;

    @OneToMany(mappedBy = "bloco")
    private List<Transacao> transacoes;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
