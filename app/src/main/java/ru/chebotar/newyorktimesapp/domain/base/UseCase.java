package ru.chebotar.newyorktimesapp.domain.base;


import androidx.annotation.NonNull;
import io.reactivex.Flowable;

public abstract class UseCase<P extends UseCase.Params, R extends UseCase.Result> {

    public abstract Flowable<R> run(@NonNull P params);

    public abstract static class Params {
    }

    public abstract static class Result {
    }
}

