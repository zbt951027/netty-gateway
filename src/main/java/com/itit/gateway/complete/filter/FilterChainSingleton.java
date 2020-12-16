package com.itit.gateway.complete.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * 过滤器链单例，用于把过滤器添加到过滤器链中
 *
 * @author 张泊涛
 */
public class FilterChainSingleton {

    private FilterChainSingleton() {
    }

    private enum EnumSingleton {
        /**
         * 懒汉枚举单例
         */
        INSTANCE;
        private FilterChainSingleton instance;

        EnumSingleton() {
            instance = new FilterChainSingleton();
        }

        public FilterChainSingleton getSingleton() {
            return instance;
        }
    }

    static FilterChainSingleton getInstance() {
        return EnumSingleton.INSTANCE.getSingleton();
    }

    /**
     * Request过滤操作链
     */
    private List<RequestFilter> requestFrontFilterList = new ArrayList<>();
    /**
     * Response过滤操作链
     */
    private List<ResponseFilter> responseBackendFilters = new ArrayList<>();

    void registerRequestFrontFilter(RequestFilter requestFrontFilter) {
        this.requestFrontFilterList.add(requestFrontFilter);
    }

    void registerResponseBackendFilter(ResponseFilter responseBackendFilter) {
        this.responseBackendFilters.add(responseBackendFilter);
    }

    List<RequestFilter> getRequestFrontFilterList() {
        return requestFrontFilterList;
    }

    List<ResponseFilter> getResponseBackendFilters() {
        return responseBackendFilters;
    }
}
