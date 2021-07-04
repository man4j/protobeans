package repository.pg;

import org.springframework.data.jpa.repository.JpaRepository;

import model.pg.PgCode;

public interface PgCodeRepository extends JpaRepository<PgCode, String> {

}
