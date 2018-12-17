package com.hk.core.test;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

/**
 * @author kevin
 * @date 2018-09-14 15:57
 */
public abstract class LoginBaseTest extends BaseTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private Filter springSecurityFilterChain;

    public WebApplicationContext getContext() {
        return context;
    }

    protected MockMvc getMockMvc() {
        return mockMvc;
    }

    @Override
    public void before() throws Exception {
        super.before();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(springSecurityFilterChain)
                .build();
    }


}
