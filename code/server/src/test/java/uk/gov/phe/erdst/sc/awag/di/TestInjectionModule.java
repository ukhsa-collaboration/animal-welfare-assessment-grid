package uk.gov.phe.erdst.sc.awag.di;

import uk.gov.phe.erdst.sc.awag.servlets.utils.RequestConverter;
import uk.gov.phe.erdst.sc.awag.servlets.utils.impl.RequestConverterImpl;

import com.google.inject.AbstractModule;

public class TestInjectionModule extends AbstractModule
{

    @Override
    protected void configure()
    {
        bind(RequestConverter.class).to(RequestConverterImpl.class);
    }

}
