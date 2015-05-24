package net.csongradyp.badger.parser.ini.trigger;

import java.util.List;

public interface ITriggerParser<T> {

    List<T> parse(String[] triggers);
}
