package io.github.yuanbug.emoji.domain.parse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author yuanbug
 * @since 2023-10-12
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmojiParseConfig {

    /**
     * `❤\x{FE0F}` is parsed to emoji "❤\x{FE0F}" when turned on,
     * otherwise it is parsed to emoji "❤" and non-emoji text "\x{FE0F}"
     */
    @Builder.Default
    public final boolean combineFollowingSelector = true;

}
