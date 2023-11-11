package io.github.yuanbug.emoji.domain.sequence;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yuanbug
 * @since 2023-10-12
 */
@Getter
@AllArgsConstructor
public enum ParseResultSequenceType {

    NON_EMOJI_TEXT(false),

    STANDARD_EMOJI(true),
    TEXT_PRESENTATION(true),
    EMOJI_PRESENTATION(true),
    INVALID_EMOJI(true),
    ;

    public final boolean emoji;

}
