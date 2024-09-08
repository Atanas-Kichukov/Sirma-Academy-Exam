package com.example.Football.repository;

import com.example.Football.model.entity.TeamDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<TeamDTO, Long> {

}
