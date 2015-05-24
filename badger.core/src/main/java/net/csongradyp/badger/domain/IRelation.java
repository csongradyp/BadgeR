package net.csongradyp.badger.domain;

import java.util.Date;

public interface IRelation {

    Boolean evaluate(Long score, Date date, Date time);
}
