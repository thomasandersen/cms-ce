package com.enonic.cms.core.jcr;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

@Component
public final class JcrNewRepositoryFactory
    implements FactoryBean<JcrRepository>
{

    private JcrRepository repository;

    public JcrRepository getObject()
    {
        return this.repository;
    }

    public Class<?> getObjectType()
    {
        return JcrRepository.class;
    }

    public boolean isSingleton()
    {
        return true;
    }

}
