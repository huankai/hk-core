package com.hk.core.authentication.security.savedrequest;

import com.hk.commons.util.ArrayUtils;
import com.hk.commons.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.savedrequest.Enumerator;
import org.springframework.security.web.savedrequest.FastHttpDateFormat;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author kevin
 * @date 2018-08-28 10:37
 * @see org.springframework.security.web.savedrequest.SavedRequestAwareWrapper
 */
class SavedRequestAwareWrapper extends HttpServletRequestWrapper {

    protected static final Logger logger = LoggerFactory.getLogger(SavedRequestAwareWrapper.class);

    protected static final TimeZone GMT_ZONE = TimeZone.getTimeZone("GMT");

//    /**
//     * The default Locale if none are specified.
//     */
//    protected static Locale defaultLocale = Locale.getDefault();

    // ~ Instance fields
    // ================================================================================================

    protected SavedRequest savedRequest;

    /**
     * The set of SimpleDateFormat formats to use in getDateHeader(). Notice that because
     * SimpleDateFormat is not thread-safe, we can't declare formats[] as a static
     * variable.
     */
    protected final SimpleDateFormat[] formats = new SimpleDateFormat[3];

    // ~ Constructors
    // ===================================================================================================

    public SavedRequestAwareWrapper(SavedRequest saved, HttpServletRequest request) {
        super(request);
        savedRequest = saved;
        formats[0] = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
        formats[1] = new SimpleDateFormat("EEEEEE, dd-MMM-yy HH:mm:ss zzz", Locale.US);
        formats[2] = new SimpleDateFormat("EEE MMMM d HH:mm:ss yyyy", Locale.US);

        formats[0].setTimeZone(GMT_ZONE);
        formats[1].setTimeZone(GMT_ZONE);
        formats[2].setTimeZone(GMT_ZONE);
    }

    // ~ Methods
    // ========================================================================================================

    @Override
    public long getDateHeader(String name) {
        String value = getHeader(name);

        if (value == null) {
            return -1L;
        }

        // Attempt to convert the date header in a variety of formats
        long result = FastHttpDateFormat.parseDate(value, formats);

        if (result != -1L) {
            return result;
        }

        throw new IllegalArgumentException(value);
    }

    @Override
    public String getHeader(String name) {
        List<String> values = savedRequest.getHeaderValues(name);
        return CollectionUtils.getFirstOrDefault(values).orElse(null);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return new Enumerator<>(savedRequest.getHeaderNames());
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        return new Enumerator<>(savedRequest.getHeaderValues(name));
    }

    @Override
    public int getIntHeader(String name) {
        String value = getHeader(name);
        return Objects.isNull(value) ? -1 : Integer.parseInt(value);
    }

    @Override
    public Locale getLocale() {
        List<Locale> locales = savedRequest.getLocales();
        return CollectionUtils.getFirstOrDefault(locales).orElse(Locale.getDefault());
    }

    @Override
    public Enumeration<Locale> getLocales() {
        List<Locale> locales = savedRequest.getLocales();
        if (CollectionUtils.isEmpty(locales)) {
            // Fall back to default locale
            locales = new ArrayList<>(1);
            locales.add(Locale.getDefault());
        }
        return new Enumerator<>(locales);
    }

    @Override
    public String getMethod() {
        return savedRequest.getMethod();
    }

    /**
     * If the parameter is available from the wrapped request then the request has been
     * forwarded/included to a URL with parameters, either supplementing or overriding the
     * saved request values.
     * <p>
     * In this case, the value from the wrapped request should be used.
     * <p>
     * If the value from the wrapped request is null, an attempt will be made to retrieve
     * the parameter from the saved request.
     */
    @Override
    public String getParameter(String name) {
        var value = super.getParameter(name);
        if (value != null) {
            return value;
        }
        var values = savedRequest.getParameterValues(name);
        return ArrayUtils.getFirstOrDefault(values);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Set<String> names = getCombinedParameterNames();
        Map<String, String[]> parameterMap = new HashMap<>(names.size());
        for (String name : names) {
            parameterMap.put(name, getParameterValues(name));
        }
        return parameterMap;
    }

    private Set<String> getCombinedParameterNames() {
        Set<String> names = new HashSet<>();
        names.addAll(super.getParameterMap().keySet());
        names.addAll(savedRequest.getParameterMap().keySet());
        return names;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return new Enumerator<>(getCombinedParameterNames());
    }

    @Override
    public String[] getParameterValues(String name) {
        var savedRequestParams = savedRequest.getParameterValues(name);
        var wrappedRequestParams = super.getParameterValues(name);

        if (savedRequestParams == null) {
            return wrappedRequestParams;
        }

        if (wrappedRequestParams == null) {
            return savedRequestParams;
        }

        // We have parameters in both saved and wrapped requests so have to merge them
        List<String> wrappedParamsList = Arrays.asList(wrappedRequestParams);
        List<String> combinedParams = new ArrayList<>(wrappedParamsList);

        // We want to add all parameters of the saved request *apart from* duplicates of
        // those already added
        for (var savedRequestParam : savedRequestParams) {
            if (!wrappedParamsList.contains(savedRequestParam)) {
                combinedParams.add(savedRequestParam);
            }
        }

        return combinedParams.toArray(new String[combinedParams.size()]);
    }
}
