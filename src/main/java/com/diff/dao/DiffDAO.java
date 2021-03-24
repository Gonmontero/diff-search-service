package com.diff.dao;

import com.diff.entities.Diff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiffDAO extends JpaRepository<Diff, String> {}
