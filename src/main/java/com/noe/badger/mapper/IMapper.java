package com.noe.badger.mapper;

import java.util.Collection;
import java.util.List;

public interface IMapper<FROM, TO> {

    List<TO> map( Collection<FROM> from );

    TO map( FROM from );
}
