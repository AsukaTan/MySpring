package com.mini.event;

import java.io.Serial;
import java.util.EventObject;

/**
 * 监听事件
 * 观察者模式解耦代码提供入口
 */
public class ApplicationEvent extends EventObject {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public ApplicationEvent(Object arg0) {
        super(arg0);
    }
}
