package ms.parade.interfaces.common.util;

public class TraceIdHolder {
    private static final ThreadLocal<String> traceIdHolder = new ThreadLocal<>();

    public static void setTraceId(String traceId) {
        traceIdHolder.set(traceId);
    }

    public static String getTraceId() {
        return traceIdHolder.get();
    }

    public static void clear() {
        traceIdHolder.remove();
    }
}