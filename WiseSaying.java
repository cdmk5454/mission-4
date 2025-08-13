package org.example;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WiseSaying {
    int id;
    String quote;
    String author;
    boolean flag;

    public WiseSaying(int id, String quote, String author, boolean flag) {
        this.id = id;
        this.quote = quote;
        this.author = author;
        this.flag = flag;
    }

}
