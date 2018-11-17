package ru.chebotar.newyorktimesapp.presetation.feed;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import ru.chebotar.newyorktimesapp.data.network.models.NewsDTO;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface FeedView extends MvpView {

    void showData(NewsDTO feed);

    void showLoading(boolean b);

    void showError();
}
