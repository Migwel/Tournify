package net.migwel.tournify.data;

public class GameType {

    private String name;

    public GameType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "GameType{" +
                "name='" + name + '\'' +
                '}';
    }
}
