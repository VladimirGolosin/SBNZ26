package com.ftn.sbnz.leservice;

import org.springframework.stereotype.Service;

@Service
public class WeatherModeService {

    public enum Mode {
        SIMULATED,
        PREDEFINED
    }

    private Mode mode = Mode.SIMULATED;

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Mode getMode() {
        return mode;
    }

    public void reset() {
        mode = Mode.SIMULATED;
    }
}