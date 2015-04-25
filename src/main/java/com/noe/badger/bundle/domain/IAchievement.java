package com.noe.badger.bundle.domain;

import java.util.List;

public interface IAchievement {

    String getId();

    String getCategory();

    List<String> getEvent();

    String getTitleKey();

    String getTextKey();

    Integer getMaxLevel();

}