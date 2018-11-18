package ru.chebotar.newyorktimesapp.presetation.feed;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.chebotar.newyorktimesapp.data.database.news.NewsWithMultimedia;
import ru.chebotar.newyorktimesapp.data.network.models.NewsDTO;
import ru.chebotar.newyorktimesapp.presetation.base.BackButtonListener;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface FeedView extends MvpView {

    void showData(NewsDTO feed);

    void showLoading(boolean loading);

    void showError();

    void setEditMode(boolean editMode);

    void showMessage(String message);

    void goBack();
}
