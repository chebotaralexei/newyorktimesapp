package ru.chebotar.newyorktimesapp.data.database;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class ListMapping<TFrom, TTo> {

    @NonNull
    private final Mapping<TFrom, TTo> mapping;

    public ListMapping(@NonNull final Mapping<TFrom, TTo> mapping) {
        this.mapping = mapping;
    }

    @NonNull
    public List<TTo> map(@NonNull final List<TFrom> list) {
        final ArrayList<TTo> arrayList = new ArrayList<>(list.size());
        for (final TFrom from : list) {
            arrayList.add(mapping.map(from));
        }
        return arrayList;
    }
}
