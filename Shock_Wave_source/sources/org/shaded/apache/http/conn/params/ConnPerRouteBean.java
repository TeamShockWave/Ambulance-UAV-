package org.shaded.apache.http.conn.params;

import java.util.HashMap;
import java.util.Map;
import org.shaded.apache.http.annotation.NotThreadSafe;
import org.shaded.apache.http.conn.routing.HttpRoute;

@NotThreadSafe
public final class ConnPerRouteBean implements ConnPerRoute {
    public static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 2;
    private int defaultMax;
    private final Map<HttpRoute, Integer> maxPerHostMap;

    public ConnPerRouteBean(int defaultMax2) {
        this.maxPerHostMap = new HashMap();
        setDefaultMaxPerRoute(defaultMax2);
    }

    public ConnPerRouteBean() {
        this(2);
    }

    public int getDefaultMax() {
        return this.defaultMax;
    }

    public void setDefaultMaxPerRoute(int max) {
        if (max < 1) {
            throw new IllegalArgumentException("The maximum must be greater than 0.");
        }
        this.defaultMax = max;
    }

    public void setMaxForRoute(HttpRoute route, int max) {
        if (route == null) {
            throw new IllegalArgumentException("HTTP route may not be null.");
        } else if (max < 1) {
            throw new IllegalArgumentException("The maximum must be greater than 0.");
        } else {
            this.maxPerHostMap.put(route, Integer.valueOf(max));
        }
    }

    public int getMaxForRoute(HttpRoute route) {
        if (route == null) {
            throw new IllegalArgumentException("HTTP route may not be null.");
        }
        Integer max = this.maxPerHostMap.get(route);
        if (max != null) {
            return max.intValue();
        }
        return this.defaultMax;
    }

    public void setMaxForRoutes(Map<HttpRoute, Integer> map) {
        if (map != null) {
            this.maxPerHostMap.clear();
            this.maxPerHostMap.putAll(map);
        }
    }

    public String toString() {
        return this.maxPerHostMap.toString();
    }
}
