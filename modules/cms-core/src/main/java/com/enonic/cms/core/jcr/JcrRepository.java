package com.enonic.cms.core.jcr;

import javax.jcr.Repository;

public interface JcrRepository
{

    Repository getRepository();

    JcrSession login();

}
