package com.enonic.cms.core.jcr;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

class JcrRepositoryImpl
    implements JcrRepository
{
    private Repository repository;

    JcrRepositoryImpl( Repository repository )
    {
        this.repository = repository;
    }

    public Repository getRepository()
    {
        return repository;
    }

    public JcrSession login()
    {
        try
        {
            Session session = repository.login();
            return new JcrSessionImpl( session, this );
        }
        catch ( RepositoryException e )
        {
            throw new RepositoryRuntimeException( e );
        }
    }
}
