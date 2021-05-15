package eu.adainius.newsfocused.test.util;

import org.mockito.MockedStatic;

public interface RunnableStaticMockWithExceptions {
    void run(MockedStatic mock) throws Exception;
}