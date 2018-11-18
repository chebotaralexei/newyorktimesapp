package ru.chebotar.newyorktimesapp.data.database;


import androidx.annotation.NonNull;

public abstract class Mapping<TFrom, TTo> {

    @NonNull
    public abstract TTo map(@NonNull TFrom from);
}
