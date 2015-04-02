package com.noe.badger.exception;

public class MalformedAchievementRelationDefinition extends RuntimeException {

    public MalformedAchievementRelationDefinition( final String message ) {
        super(message);
    }

    public MalformedAchievementRelationDefinition( final String message, Throwable cause ) {
        super(message, cause);
    }

    public MalformedAchievementRelationDefinition( final Throwable cause ) {
        super(cause);
    }
}
