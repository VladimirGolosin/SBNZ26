package com.ftn.sbnz.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.ftn.sbnz.model.models.Action;
import com.ftn.sbnz.model.models.Culture;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/demo")
public class DemoController {

    private final KieContainer kieContainer;
    private final List<Culture> cultures = new ArrayList<>();
    private LocalDate currentDate;

    public DemoController(KieContainer kieContainer) {
        this.kieContainer = kieContainer;

        // --- Luk ---
        LocalDate today = LocalDate.now();
        List<Action> lukActions = List.of(
                new Action("Navodnjavanje", null, LocalDate.of(today.getYear(), 5, 7)),
                new Action("Primena zaštite", null, LocalDate.of(today.getYear(), 6, 7)),
                new Action("Uklanjanje korova", null, LocalDate.of(today.getYear(), 7, 7))
        );
        cultures.add(new Culture("Luk", new ArrayList<>(lukActions), LocalDate.of(today.getYear(), 4, 1), 0));

        // --- Krompir ---
        LocalDate datePlanted = LocalDate.of(today.getYear(), 4, 20);
        List<Action> krompirActions = List.of(
                new Action("Navodnjavanje", null, datePlanted.plusDays(17)),
                new Action("Đubrenje", null, datePlanted.plusDays(37)),
                new Action("Zagrtanje", null, datePlanted.plusDays(57))
        );
        cultures.add(new Culture("Krompir", new ArrayList<>(krompirActions), datePlanted, 0));

        // inicijalni datum
        this.currentDate = today;
    }

    @GetMapping("/step/{step}")
    public List<Culture> runStep(@PathVariable int step) {
        KieSession kSession = kieContainer.newKieSession("ksession-rules");

        // ubacujemo globalni datum
        kSession.setGlobal("currentDate", currentDate);

        // simulacija akcija po koracima
        switch(step) {
            case 1:
                // Korak 1: prva akcija za luk i krompir je izvršena
                cultures.forEach(c -> {
                    c.getActions().get(0).setDateDone(currentDate);
                    kSession.insert(c);
                });
                currentDate = currentDate.plusDays(10); // pomeramo vreme
                break;

            case 2:
                // Korak 2: druga akcija
                // Luk: na vreme, Krompir: nije
                cultures.forEach(c -> {
                    if (c.getName().equals("Luk")) {
                        c.getActions().get(1).setDateDone(currentDate);
                    }
                    // krompir ne radimo, ostaje null
                    kSession.insert(c);
                });
                currentDate = currentDate.plusDays(20);
                break;

            case 3:
                // Korak 3: treća akcija
                // Luk: na vreme, Krompir: sada je obavljeno, ali status već -1
                cultures.forEach(c -> {
                    if (c.getName().equals("Luk")) {
                        c.getActions().get(2).setDateDone(currentDate);
                    } else if (c.getName().equals("Krompir")) {
                        c.getActions().get(2).setDateDone(currentDate);
                    }
                    kSession.insert(c);
                });
                currentDate = currentDate.plusDays(5);
                break;

            default:
                kSession.dispose();
                return cultures;
        }

        kSession.fireAllRules();
        kSession.dispose();

        return cultures;
    }
}

