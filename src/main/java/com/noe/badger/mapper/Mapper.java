package com.noe.badger.mapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Mapper<FROM, TO> implements IMapper<FROM, TO> {

    @Override
    public List<TO> map(final Collection<FROM> from) {
        final List<TO> to = new ArrayList<>();
        for (FROM f : from) {
            final TO t = map(f);
            to.add(t);
        }
        return to;
    }

}
