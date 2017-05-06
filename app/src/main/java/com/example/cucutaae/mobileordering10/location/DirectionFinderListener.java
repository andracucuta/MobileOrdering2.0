package com.example.cucutaae.mobileordering10.location;

import java.util.List;

/**
 * Created by cucut on 5/6/2017.
 */

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}