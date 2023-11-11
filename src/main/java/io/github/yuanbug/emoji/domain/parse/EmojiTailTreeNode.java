package io.github.yuanbug.emoji.domain.parse;

import io.github.yuanbug.emoji.domain.sequence.EmojiSequence;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yuanbug
 * @since 2023/10/17
 */
public class EmojiTailTreeNode {

    @Getter
    private EmojiSequence data;

    private final Map<Character, EmojiTailTreeNode> subNodes = new HashMap<>(4);

    public void addSequence(EmojiSequence sequence) {
        EmojiTailTreeNode cursor = this;
        String chars = sequence.chars();
        int length = chars.length();
        for (int i = 0; i < length; i++) {
            cursor = cursor.subNodes.computeIfAbsent(chars.charAt(i), k -> new EmojiTailTreeNode());
        }
        cursor.data = sequence;
    }

    public EmojiTailTreeNode goDown(char ch) {
        return subNodes.get(ch);
    }

}
