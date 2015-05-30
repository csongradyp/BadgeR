package net.csongradyp.badger.parser.api.trigger;

import java.util.List;

public interface ITriggerParser<T> {

    List<T> parse(List<String> triggers);

}
