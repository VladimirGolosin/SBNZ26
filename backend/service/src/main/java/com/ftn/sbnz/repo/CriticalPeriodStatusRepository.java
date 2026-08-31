package com.ftn.sbnz.repo;

import com.ftn.sbnz.model.CriticalPeriodStatus;
import com.ftn.sbnz.model.CultureName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CriticalPeriodStatusRepository extends JpaRepository<CriticalPeriodStatus, CultureName> {
}