package com.grobo.notifications.feed;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class FeedViewModel extends AndroidViewModel {

    private FeedRepository feedRepository;
    private LiveData<List<FeedItem>> allFeeds;

    public FeedViewModel(@NonNull Application application) {
        super(application);
        feedRepository = new FeedRepository(application);
        allFeeds = feedRepository.loadAllFeed();
    }

    LiveData<List<FeedItem>> loadAllFeeds() {
        return allFeeds;
    }

    public void insert(FeedItem feedItem) {
        feedRepository.insert(feedItem);
    }

    FeedItem getFeedById(String id) {
        return feedRepository.getFeedById(id);
    }

    int getFeedCount(String id) {
        return feedRepository.getFeedCount(id);
    }

    long getMaxEventId() {
        return feedRepository.getMaxEventId();
    }
}
