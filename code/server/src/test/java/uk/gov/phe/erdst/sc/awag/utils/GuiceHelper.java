package uk.gov.phe.erdst.sc.awag.utils;

import com.google.inject.Guice;
import com.google.inject.Injector;

public final class GuiceHelper
{
    private GuiceHelper()
    {
    }

    public static void injectTestDependencies(Object injectionTarget)
    {
        Injector injector = Guice.createInjector();
        injector.injectMembers(injectionTarget);
    }
}
