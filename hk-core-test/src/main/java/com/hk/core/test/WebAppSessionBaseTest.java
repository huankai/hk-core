package com.hk.core.test;

import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.HttpSession;

/**
 * @author: kevin
 * @date: 2018-09-14 15:33
 */
@WebAppConfiguration
public abstract class WebAppSessionBaseTest extends LoginBaseTest {

    protected MockHttpSession httpSession;

    @Override
    public void before() throws Exception {
        super.before();
        this.httpSession = (MockHttpSession) getLoginSession();
    }

    private HttpSession getLoginSession() throws Exception {
        MvcResult result = getMockMvc()
                .perform(MockMvcRequestBuilders.post(getLoginUrl())
                        .param("username", getUsername())
                        .param("password", getPassword()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        return result.getRequest().getSession();
    }

    protected String getLoginUrl() {
        return "/login";
    }

    protected abstract String getUsername();

    protected abstract String getPassword();
}
