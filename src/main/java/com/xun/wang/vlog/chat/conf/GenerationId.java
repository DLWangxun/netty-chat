package com.xun.wang.vlog.chat.conf;

import java.io.Serializable;
import java.util.Properties;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.n3r.idworker.Sid;



public class GenerationId implements IdentifierGenerator {

    
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return getId();
    }


    /**
     * 	该方法需要是线程安全的
     * @return
     */
    public Long getId() {
        synchronized (GenerationId.class) {
            return Sid.next();
        }
    }
}
