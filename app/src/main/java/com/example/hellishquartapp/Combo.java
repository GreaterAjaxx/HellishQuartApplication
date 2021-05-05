package com.example.hellishquartapp;

public class Combo {
    private String title;
    private String inputs;
    private boolean liked;

    public Combo() {
        // empty constructor
    }

    public Combo(String title, String inputs, boolean liked) {
        this.title = title;
        this.inputs = inputs;
        this.liked = liked;
    }

    public String getTitle() {
        return title;
    }

    public String getInputs() {
        return inputs;
    }

    public boolean getLiked() {
        return liked;
    }

}
