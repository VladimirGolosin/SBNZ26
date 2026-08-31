package com.ftn.sbnz.leservice;

import com.ftn.sbnz.model.CriticalPeriod;
import com.ftn.sbnz.model.CultureName;
import com.ftn.sbnz.repo.CriticalPeriodRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CriticalPeriodService {

    private final CriticalPeriodRepository repository;

    public CriticalPeriodService(CriticalPeriodRepository repository) {
        this.repository = repository;
    }

    public CriticalPeriod create(CultureName cultureName, LocalDate dateFrom, LocalDate dateTo) {
        CriticalPeriod period = new CriticalPeriod();
        period.setCultureName(cultureName);
        period.setDateFrom(dateFrom);
        period.setDateTo(dateTo);
        return repository.save(period);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}