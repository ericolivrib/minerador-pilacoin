package br.ufsm.csi.pilacoin.repository;

import br.ufsm.csi.pilacoin.model.PilaCoin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PilaCoinRepository extends JpaRepository<PilaCoin, Long> {
}
