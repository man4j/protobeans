package repository.crdb;

import org.springframework.data.jpa.repository.JpaRepository;

import model.crdb.CrdbCode;

public interface CrdbCodeRepository extends JpaRepository<CrdbCode, String> {

}
