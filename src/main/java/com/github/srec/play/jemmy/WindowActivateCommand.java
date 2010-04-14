package com.github.srec.play.jemmy;

import com.github.srec.play.Command;
import com.github.srec.play.IllegalParametersException;

import static com.github.srec.play.jemmy.JemmyDSL.frame;

public class WindowActivateCommand implements Command {
    @Override
    public String getName() {
        return "window_activate";
    }

    @Override
    public void run(String... params) {
        if (params.length != 1) throw new IllegalParametersException("Missing parameters to window activate");
        frame(params[0]);
    }
}