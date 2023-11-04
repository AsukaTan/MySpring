package com.mini.event;

/**
 * 发布事件
 */
public interface ApplicationEventPublisher {
    void publishEvent(ApplicationEvent event);
}
