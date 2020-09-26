package org.codecraftlabs.kikker.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("File util unit test")
public class FileUtilTest {
    @Test
    void simpleTest() {
        var results = FileUtil.listFiles(".", ".java");
        System.out.println(results);
    }
}
