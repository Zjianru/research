package com.cz.agent.threadAgent;

import java.util.Stack;

/**
 * 事务管理器类，用于管理当前线程中的事务轨迹。
 * 使用ThreadLocal来保持每个线程的事务栈，以便于跟踪当前线程的事务流程。
 *
 * @author Zjianru
 */
public class TrackManager {

    /**
     * 使用ThreadLocal存储当前线程的事务栈，确保每个线程的事务栈独立。
     */
    private static final ThreadLocal<Stack<String>> track = new ThreadLocal<>();

    /**
     * 创建一个新的事务片段，并将其加入到当前线程的事务栈中。
     * 如果当前线程还没有事务栈，则创建一个新的事务栈。
     * 如果栈为空，尝试从TrackContext中获取当前的链接ID，如果获取不到，则设置一个默认的链接ID。
     *
     * @return 当前线程的事务片段ID。
     */
    public static String createSpan() {
        Stack<String> stack = track.get();
        if (stack == null) {
            stack = new Stack<>();
            track.set(stack);
        }

        String linkId;
        if (stack.isEmpty()) {
            linkId = TrackContext.getLinkId();
            if (linkId == null) {
                linkId = "NVL";
                TrackContext.setLinkId(linkId);
            }
        } else {
            linkId = stack.peek();
            TrackContext.setLinkId(linkId);
        }
        return linkId;
    }

    /**
     * 创建一个入口事务片段，并将其推入到当前线程的事务栈中。
     * 先调用createSpan方法来确保当前线程的事务栈和链接ID被正确初始化。
     *
     * @return 入口事务片段ID。
     */
    public static String createEntrySpan() {
        String span = createSpan();
        Stack<String> stack = track.get();
        stack.push(span);
        return span;
    }

    /**
     * 获取当前线程的退出事务片段，并将其从事务栈中弹出。
     * 如果当前线程的事务栈为空，则清除TrackContext中的链接ID，并返回null。
     *
     * @return 当前线程的退出事务片段ID，或者null如果栈为空。
     */
    public static String getExitSpan() {
        Stack<String> stack = track.get();
        if (stack == null || stack.isEmpty()) {
            TrackContext.clear();
            return null;
        }
        return stack.pop();
    }

    /**
     * 获取当前线程的当前事务片段，即事务栈的顶部元素。
     * 如果当前线程的事务栈为空，则返回null。
     *
     * @return 当前线程的当前事务片段ID，或者null如果栈为空。
     */
    public static String getCurrentSpan() {
        Stack<String> stack = track.get();
        if (stack == null || stack.isEmpty()) {
            return null;
        }
        return stack.peek();
    }


}

