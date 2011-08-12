package com.enonic.cms.core.jcr;

import java.util.Map;

public interface ImportDataCallbackHandler
{

    void processDataEntry( Map<String, Object> data );

}
