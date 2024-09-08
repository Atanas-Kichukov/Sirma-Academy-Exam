package com.example.Football.repository;

import com.example.Football.model.entity.MatchesDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchesRepository extends JpaRepository<MatchesDTO,Long> {

}
