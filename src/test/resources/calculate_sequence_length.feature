Feature: Calculate sequence length

  Scenario: check null sequence length
    Given input null
    When get content length
    Then content length should be 0

  Scenario Outline: check non-null sequence length
    Given input content "<content>"
    When get content length
    Then content length should be <expectedLength>

    Examples:
      | content            | expectedLength |
      # non emoji
      |                    | 0              |
      | emoji              | 5              |
      # simple emoji
      | 😂                 | 1              |
      | 😂😂               | 2              |
      | 🤣👍               | 2              |
      | 😂emoji            | 6              |
      | 😂😂emoji          | 7              |
      | 😂emoji😂          | 7              |
      | 😂emoji😂emoji     | 12             |
      | emoji😂😂emoji😂   | 13             |
      | 😂emoji😂emoji😂   | 13             |
      # one byte emoji
      | ❤                  | 1              |
      | asd❤               | 4              |
      | asd❤❤a             | 6              |
      | ❤asd❤❤a            | 7              |
      | asd❤❤a❤            | 7              |
      | ❤asd❤❤a            | 7              |
      | ❤asd❤❤a❤❤          | 9              |
      # one byte emoji with emoji presentation selector
      | ❤️                 | 1              |
      | ❤️❤️               | 2              |
      | ❤️😂❤️             | 3              |
      | 😂❤️😂❤️😂         | 5              |
      | ❤️asd❤❤️a❤️❤       | 9              |
      # keycap
      | 1️⃣                | 1              |
      | 1️⃣1               | 2              |
      | 11️⃣               | 2              |
      | 11️⃣1              | 3              |
      | 1️⃣a               | 2              |
      | a1️⃣               | 2              |
      | a1️⃣a              | 3              |
      | 1️⃣1️⃣             | 2              |
      | 1️⃣2️⃣3️⃣          | 3              |
      # emoji with modifier
      | 🐈‍⬛               | 1              |
      | 🐈‍⬛🐈             | 2              |
      | 🐈🐈‍⬛             | 2              |
      | black cat🐈‍⬛      | 10             |
      | 🐈‍⬛black cat      | 10             |
      | black🐈‍⬛cat       | 9              |
      # emoji joined by ZWJ
      | 😶‍🌫️             | 1              |
      | 😶‍🌫️😂❤️         | 3              |
      | 😂😶‍🌫️😂❤️😶‍🌫️ | 5              |
      | 🙂‍↔️❤️‍🔥         | 2              |
      | 👨🏿‍🦱            | 1              |
      | 👨🏿‍🦱and👨‍🦳    | 5              |
