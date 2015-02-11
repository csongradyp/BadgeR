package com.noe.badger.domain;

public interface Achievement {

    String PROP_ID = "id";
    String PROP_FTITLE = "title";
    String PROP_TEXT = "text";
    String PROP_TRIGGER = "trigger";

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
    String getTitle();

    /**
     * Setter for property 'title'.
     *
     * @param title Value to set for property 'title'.
     */
    void setTitle( String title );

    /**
     * Getter for property 'text'.
     *
     * @return Value for property 'text'.
     */
    String getText();

    /**
     * Setter for property 'text'.
     *
     * @param text Value to set for property 'text'.
     */
    void setText( String text );

    /**
     * Getter for property 'trigger'.
     *
     * @return Value for property 'trigger'.
     */
    String[] getTrigger();

    /**
     * Setter for property 'trigger'.
     *
     * @param trigger Value to set for property 'trigger'.
     */
    void setTrigger( String[] trigger );
}
