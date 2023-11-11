package io.github.yuanbug.emoji.domain.sequence;

import lombok.Builder;

/**
 * @author yuanbug
 * @since 2023-10-12
 */
@Builder
public record NonEmojiSequence(String content) implements ParsedSequence {

    @Override
    public ParseResultSequenceType getType() {
        return ParseResultSequenceType.NON_EMOJI_TEXT;
    }

    @Override
    public String getContent() {
        return content;
    }
}
