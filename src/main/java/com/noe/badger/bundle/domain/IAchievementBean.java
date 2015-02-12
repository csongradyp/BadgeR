package com.noe.badger.bundle.domain;

public interface IAchievementBean<T> {

    String PROP_ID = "id";
    String PROP_TITLE_KEY = "titleKey";
    String PROP_TEXT_KEY = "textKey";
    String PROP_TRIGGER = "trigger";
    String PROP_MAX_LEVEL = "maxLevel";

    /**
     * Getter for property 'id'.
     *
     * @return Value for property 'id'.
     */
    String getId();

    /**
     * Setter for property 'id'.
     *
     * @param id Value to set for property 'id'.
     */
    void setId( String id );

    /**
     * Getter for property 'title'.
     *
     * @return Value for property 'title'.
     */
    String getTitleKey();

    /**
     * Setter for property 'title'.
     *
     * @param title Value to set for property 'title'.
     */
    void setTitleKey( String title );

    /**
     * Getter for property 'text'.
     *
     * @return Value for property 'text'.
     */
    String getTextKey();

    /**
     * Setter for property 'text'.
     *
     * @param text Value to set for property 'text'.
     */
    void setTextKey( String text );

    /**
     * Getter for property 'trigger'.
     *
     * @return Value for property 'trigger'.
     */
    T[] getTrigger();

    /**
     * Setter for property 'trigger'.
     *
     * @param trigger Value to set for property 'trigger'.
     */
    void setTrigger( String[] trigger );


    Integer getMaxLevel();

    void setMaxLevel( Integer maxLevels );
}
