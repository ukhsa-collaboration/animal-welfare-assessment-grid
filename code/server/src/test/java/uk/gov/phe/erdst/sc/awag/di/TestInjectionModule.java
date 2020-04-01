package uk.gov.phe.erdst.sc.awag.di;

import com.google.inject.AbstractModule;

import uk.gov.phe.erdst.sc.awag.deprecated.RequestConverter;

public class TestInjectionModule extends AbstractModule
{

    @Override
    protected void configure()
    {
        bind(RequestConverter.class).to(RequestConverter.class);
    }

}
