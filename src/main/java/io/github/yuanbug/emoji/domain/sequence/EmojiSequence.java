package io.github.yuanbug.emoji.domain.sequence;

import lombok.Builder;

/**
 * @author yuanbug
 * @since 2023-10-12
 */
@Builder
public record EmojiSequence(
        String chars,
        String code,
        String name,
        String group,
        String subGroup
) implements ParsedSequence {

    @Override
    public ParseResultSequenceType getType() {
        return ParseResultSequenceType.STANDARD_EMOJI;
    }

    @Override
    public String getContent() {
        return chars;
    }

    public EmojiSequence.EmojiSequenceBuilder copy() {
        return EmojiSequence.builder()
                .chars(chars)
                .code(code)
                .name(name)
                .group(group)
                .subGroup(subGroup);
    }

}
