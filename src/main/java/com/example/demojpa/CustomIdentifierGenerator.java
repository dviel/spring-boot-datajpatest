/*
 * Copyrights Music Work 2007-2016.
 * @author <a href="mailto:dv@music-work.com">Damien Viel</a>
 */

package com.example.demojpa;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.Random;

public class CustomIdentifierGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
        return new Random().nextLong();
    }

}
