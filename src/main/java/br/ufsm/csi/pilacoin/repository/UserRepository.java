package br.ufsm.csi.pilacoin.repository;

import br.ufsm.csi.pilacoin.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Usuario, Long> {
}
