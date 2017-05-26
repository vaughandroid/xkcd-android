package testutils;

import org.junit.rules.ExternalResource;

import app.TestApp;


public class TestAppRule extends ExternalResource {

    @Override
    protected void before() throws Throwable {
        TestApp.activityInjector = null;
    }

    @Override
    protected void after() {
        TestApp.activityInjector = null;
    }
}
