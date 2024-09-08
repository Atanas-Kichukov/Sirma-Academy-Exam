package com.example.Football.repository;

import com.example.Football.model.entity.PlayerDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerDTO,Long> {

}
