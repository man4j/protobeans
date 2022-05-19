package repository;


import org.springframework.data.jpa.repository.JpaRepository;

import model.CrdbCode;

public interface CrdbCodeRepository extends JpaRepository<CrdbCode, String> {

}