package io.github.yuanbug.emoji.domain.sequence;

import io.github.yuanbug.emoji.domain.constants.EmojiConstants;

/**
 * @author yuanbug
 * @since 2023/11/11
 */
public record TextPresentation(EmojiSequence prefix) implements ParsedSequence {

    @Override
    public ParseResultSequenceType getType() {
        return ParseResultSequenceType.TEXT_PRESENTATION;
    }

    @Override
    public String getContent() {
        return prefix.getContent() + EmojiConstants.SELECTOR_TEXT_PRESENTATION;
    }
}
