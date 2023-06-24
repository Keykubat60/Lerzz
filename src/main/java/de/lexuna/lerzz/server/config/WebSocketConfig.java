package de.lexuna.lerzz.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.session.data.mongo.config.annotation.web.http.EnableMongoHttpSession;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Websocket config class to implement a Websocket
 */
@Configuration
@EnableWebSocketMessageBroker
@EnableMongoHttpSession
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Method to register STOMP endpoints.
     *
     * @param registry the StompEndpointRegistry
     */
    @Override
    public final void registerStompEndpoints(
            final StompEndpointRegistry registry) {
        registry.addEndpoint("/lerzz").withSockJS();
    }

    /**
     *  Method to configure the message broker
     *
     * @param registry the MessageBrokerRegistry
     */
    @Override
    public final void configureMessageBroker(
            final MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
    }


}
