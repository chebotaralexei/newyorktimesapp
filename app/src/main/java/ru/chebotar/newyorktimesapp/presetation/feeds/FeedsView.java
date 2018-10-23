package ru.chebotar.newyorktimesapp.presetation.feeds;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.chebotar.newyorktimesapp.data.network.models.NewsDTO;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface FeedsView extends MvpView {

    void showData(List<NewsDTO> data);

    void showLoading(boolean b);
}
