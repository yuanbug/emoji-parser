package io.github.yuanbug.emoji.domain.sequence;

import lombok.Builder;
import lombok.ToString;

import javax.naming.NoInitialContextException;
import java.util.Collections;

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
        // TODO
        return ParseResultSequenceType.SIMPLE_STICKER;
    }

    @Override
    public String getContent() {
        // TODO
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
