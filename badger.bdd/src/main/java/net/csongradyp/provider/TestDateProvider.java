package net.csongradyp.provider;

import java.util.Date;
import net.csongradyp.badger.DateProvider;

public class TestDateProvider extends DateProvider {

    private Date stubbedDate;

    public void setStubbedDate(Date stubbedDate) {
        this.stubbedDate = stubbedDate;
    }

    @Override
    public String now() {
        return format(stubbedDate.getTime());
    }

}
