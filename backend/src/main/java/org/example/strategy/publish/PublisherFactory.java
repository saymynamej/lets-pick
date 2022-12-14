package org.example.strategy.publish;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PublisherFactory {
    private final Map<PublisherSelectType, PublisherSelector> strategies;

    @Autowired
    public PublisherFactory(Set<PublisherSelector> strategies) {
        this.strategies = strategies.stream().collect(Collectors.toMap(
                PublisherSelector::getStrategyType,
                selector -> selector
        ));
    }

    public PublisherSelector getStrategy(int reservedContentSize) {
        return strategies.get(Arrays.stream(PublisherSelectType.values())
                .filter(type -> type.getContentSize() == reservedContentSize)
                .findFirst().orElse(PublisherSelectType.ALL));
    }
}
