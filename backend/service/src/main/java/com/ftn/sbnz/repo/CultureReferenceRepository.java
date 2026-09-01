package com.ftn.sbnz.repo;

import com.ftn.sbnz.model.CultureReference;
import com.ftn.sbnz.model.CultureName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CultureReferenceRepository extends JpaRepository<CultureReference, CultureName> {
}