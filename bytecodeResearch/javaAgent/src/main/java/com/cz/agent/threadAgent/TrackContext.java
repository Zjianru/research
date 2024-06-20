package com.cz.agent.threadAgent;

/**
 * code desc
 *
 * @author Zjianru
 */
public class TrackContext {

    private static final ThreadLocal<String> trackContext = new ThreadLocal<>();

    public static void clear() {
        trackContext.remove();
    }

    public static String getLinkId() {
        return trackContext.get();
    }

    public static void setLinkId(String linkId) {
        trackContext.set(linkId);
    }

}
