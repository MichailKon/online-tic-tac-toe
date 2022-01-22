package ru.MichailKon.webGame;

public class Pair<Type1, Type2> {
    Type1 first;
    Type2 second;

    Pair(Type1 first, Type2 second) {
        this.first = first;
        this.second = second;
    }

    public Type1 getFirst() {
        return first;
    }

    public void setFirst(Type1 first) {
        this.first = first;
    }

    public Type2 getSecond() {
        return second;
    }

    public void setSecond(Type2 second) {
        this.second = second;
    }
}
