package com.example.Football.repository;

import com.example.Football.model.entity.PlayerDTO;
import com.example.Football.model.entity.RecordsDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordsRepository extends JpaRepository<RecordsDto,Long> {
    List<RecordsDto> findByPlayerId(PlayerDTO playerId);
}
