package uk.gov.phe.erdst.sc.awag.utils;

import uk.gov.phe.erdst.sc.awag.di.TestInjectionModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public final class GuiceHelper
{
    private GuiceHelper()
    {
    }

    public static void injectTestDependencies(Object injectionTarget)
    {
        Injector injector = Guice.createInjector(new TestInjectionModule());
        injector.injectMembers(injectionTarget);
    }
}
