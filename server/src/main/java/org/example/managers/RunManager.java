package org.example.managers;

import org.example.network.Request;
import org.example.network.Response;
import org.example.utility.UserScanner;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class RunManager  {
    private final CommandManager commandManager;
    public RunManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public Response run(Request request) {
        return commandManager.execute(request);

    }
    }

