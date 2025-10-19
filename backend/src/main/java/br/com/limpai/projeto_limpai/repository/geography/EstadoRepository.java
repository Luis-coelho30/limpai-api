package br.com.limpai.projeto_limpai.repository.geography;

import br.com.limpai.projeto_limpai.model.geography.Estado;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoRepository extends CrudRepository<Estado, Long> {
}
